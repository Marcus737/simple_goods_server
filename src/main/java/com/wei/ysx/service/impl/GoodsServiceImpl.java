package com.wei.ysx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wei.ysx.entity.Goods;
import com.wei.ysx.entity.Inventory;
import com.wei.ysx.entity.Response;
import com.wei.ysx.mapper.GoodsMapper;
import com.wei.ysx.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.ysx.service.IInventoryService;
import com.wei.ysx.utils.JsonParser;
import com.wei.ysx.utils.URLConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    public static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Value("${goods_prefix}")
    String goodsPrefix;

    private static final String allGoodsList = "allGoodsList";

    private ArrayList<String> goodsList = new ArrayList<>();

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IInventoryService inventoryService;



    private final String url ="http://route.showapi.com/66-22?showapi_appid=572140&showapi_sign=2196b3a9cefa4ae3bcaeb2e5aab56c3e&code=";


    /** 考虑用redis存货品数据
     * 根据条形码数字获取商品信息
     *
     * 先去数据库查，如果数据库中没有，再联网查询
     *
     * @param code
     * @return
     */
    @Override
    public Response getGoodsInfoByCode(String code) {
        Goods goods;
        //先从缓存中获取
        goods = getGoodsFromRedis(code);
        if (null == goods){
            //从数据库中查
            boolean exist = goodsMapper.isGoodsExist(code);
            if (exist){
                //数据中存在此数据
                goods = goodsMapper.getGoodsByCode(code);
                //存到缓存里
                saveToRedis(goods);
            }else {
                //数据库中不存在，从云端获取
                String tempUrl = url + code;
                String json = URLConnectionUtils.sendRequest(tempUrl);
                try {
                    goods = JsonParser.parseGoods(json, Goods.class);
                    if (goods.getGoodsName() == null){
                        return Response.error("云端暂无该商品信息！,请核对条形码");
                    }
                    //添加到数据库中
                    try {
                        goods.setEnable(true);
                        goodsMapper.insert(goods);
                        Inventory inventory = new Inventory();
                        inventory.setGoodsId(goods.getId());
                        inventoryService.save(inventory);
                        //清除缓存
                        clearGoodsRedis();
                        //存到缓存里
                        saveToRedis(goods);
                    }catch (Exception e){
                        logger.error("goods添加到数据库失败！" + e.getMessage());
                    }
                }catch (JsonProcessingException e){
                    logger.error("解析goods的json字符串失败！" + e.getMessage());
                    return Response.error("云端获取信息失败,请联系管理员");
                }
            }
        }
        return Response.success(goods);
    }


    /**
     * 获取全部商品信息
     * @return
     */
    public List listGoods(Integer currentPage, Integer size){

        if (currentPage <= 0){
            return null;
        }

        ValueOperations ops = redisTemplate.opsForValue();
        //从缓存获取
        List<Goods> o = null;
        o = (List<Goods>)ops.get(allGoodsList);
        if (null == o){
            //从数据库中查
            o = this.list();
            ops.set(allGoodsList, o);
        }
        if (null == o){
            return null;
        }
        ArrayList<Object> res = new ArrayList<>();
        int total = o.size();
        res.add(total);
        int maxPage =  total % size == 0 ? total / size : (total / size) + 1;
        if (currentPage > maxPage){
            if (o.size() > size){
                res.add(o.subList(total - size, total));
            }else {
                res.add(o);
            }
        }else {
            int begin = (currentPage - 1) * size;
            int end = Math.min(currentPage * size, total) ;
            res.add(o.subList(begin, end));
        }

        return res;
    }

    /**
     * 手动添加商品
     * @param goods
     * @return
     */
    @Override
    public Response addGoodsByManual(Goods goods) {
        if (StringUtils.isEmpty(goods.getGoodsName())){
            return Response.error("商品名不能为空");
        }
        if (null == goods.getPrice()){
            return Response.error("价格不能为空");
        }
        if (null == goods.getCode()){
            goods.setCode(getRandomCode());
        }
        goods.setEnable(true);
        boolean isSuccess = this.save(goods);
        if (isSuccess){
            Inventory i = new Inventory();
            i.setGoodsId(goods.getId());
            i.setValue(0);
            inventoryService.save(i);
            clearGoodsRedis();
            return Response.success("添加成功");
        }


        return Response.error("添加失败");
    }


    private String getRandomCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(0, 13);
    }

    /**
     * 清楚redis中goods的缓存
     */
    public void clearGoodsRedis(){
        ValueOperations ops = redisTemplate.opsForValue();
        RedisOperations<String, Goods> operations = ops.getOperations();
        for (String s : goodsList) {
            operations.delete(s);
        }
        operations.delete(allGoodsList);
        goodsList.clear();
    }

    /**
     * 把商品保存到缓存
     * @param goods
     */
    private void saveToRedis(Goods goods) {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(goodsPrefix + goods.getCode(), goods);
        goodsList.add(goodsPrefix + goods.getCode());
    }

    /**
     * 从缓存获取商品
     * @param code
     * @return
     */
    private Goods getGoodsFromRedis(String code) {
        ValueOperations ops = redisTemplate.opsForValue();
        return (Goods) ops.get(goodsPrefix + code);
    }

    /**
     * 使商品下架
     * @param code
     * @return
     */
    @Override
    public Response disableGoods(String code) {
        if (goodsMapper.disableGoods(code)){
            clearGoodsRedis();
            return Response.success("商品下架成功");
        }
        return Response.error("商品下架失败");
    }

    /**
     * 使商品上架
     * @param code
     * @return
     */
    @Override
    public Response enableGoods(String code) {

        if (goodsMapper.enableGoods(code)){
            clearGoodsRedis();
            return Response.success("商品上架成功");
        }
        return Response.error("商品上架失败");
    }

    /**
     * 判断该商品是否存在数据库中
     * @param code
     * @return
     */
    @Override
    public boolean isGoodsExist(String code) {
        return goodsMapper.isGoodsExist(code);
    }

    /**
     * 根据物品code更新物品信息
     * @param goods
     * @return
     */
    @Override
    public Response updateGoodsByCode(Goods goods) {
        boolean success = goodsMapper.updateGoodsByCode(goods);
        if (success){
            //更新缓存
            clearGoodsRedis();
            return Response.success("更新成功");
        }
        return Response.error("更新失败");
    }

    /**
     * 根据商品code获取商品信息
     * @param code
     * @return
     */
    @Override
    public Goods getGoodsByCode(String code) {
        Goods goods;
        //先从缓存中获取
        goods = getGoodsFromRedis(code);
        if (null == goods) {
            //从数据库中查
            boolean exist = goodsMapper.isGoodsExist(code);
            if (exist) {
                //数据中存在此数据
                goods = goodsMapper.getGoodsByCode(code);
                saveToRedis(goods);
            }
        }
        return goods;
    }
}
