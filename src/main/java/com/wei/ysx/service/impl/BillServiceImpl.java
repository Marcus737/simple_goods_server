package com.wei.ysx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.ysx.entity.*;
import com.wei.ysx.mapper.BillMapper;
import com.wei.ysx.service.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {


    private String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    @Autowired
    IGoodsService goodsService;

    @Autowired
    BillMapper billMapper;

    @Autowired
    IInventoryService inventoryService;


    /**
     * 创建订单
     * @param goodsCodes
     * @param goodsNums
     * @return
     */
    @Override
    @Transactional
    public Response createBill(String[] goodsCodes, Integer[] goodsNums) {

        Bill bill = new Bill();
        bill.setId(getUUID());
        bill.setState(0);
        bill.setCreateTime(LocalDateTime.now());
        float totalPrice = 0.0f;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < goodsCodes.length; i++) {
            //当前商品code
            String code = goodsCodes[i];
            //购买的商品数
            int curGoodsNum = goodsNums[i];

            //判断该商品存不存在
            boolean exist = goodsService.isGoodsExist(code);
            if (!exist){
                throw new RuntimeException("商品code:" + code + "不存在");
            }
            Goods one = goodsService.getOne(new QueryWrapper<Goods>().eq("code", code).select("id", "price"));
            sb.append(code).append(":").append(curGoodsNum).append(",");
            Inventory goodsNum = inventoryService.getOne(new QueryWrapper<Inventory>().eq("goods_id", one.getId()).select("value"));
            inventoryService.update(new UpdateWrapper<Inventory>().eq("goods_id", one.getId()).set("value", goodsNum.getValue() - curGoodsNum));
            //计算总价格
            totalPrice += one.getPrice();

        }
        bill.setTotalPrice(totalPrice);
        bill.setGoodsCodeAndNum(sb.toString());
        boolean isSuccess = this.save(bill);
        if (!isSuccess){
            return Response.error("创建订单失败");
        }
        return Response.success("创建订单成功！",bill.getId());
    }


    /**
     * 查询所有订单 分页
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public List listBillByPage(Integer currentPage, Integer size) {
        //开启分页
        Page<Bill> page = new Page<>(currentPage, size);
        IPage<Bill> billIPage  = billMapper.listBillByPage(page);

        List<Bill> bills = billIPage.getRecords();
        for (Bill bill : bills) {
            ArrayList<GoodsWithNum> goodsWithNums = new ArrayList<>();
            String goodsIdAndNum = bill.getGoodsCodeAndNum();
            Map<String, List<String>> codeListAndNumList = getCodeListAndNumList(goodsIdAndNum);
            List<String> codeList = codeListAndNumList.get("codeList");
            List<String> numList = codeListAndNumList.get("numList");
            for (int i = 0; i < codeList.size(); i++) {
                Goods goodsByCode = goodsService.getGoodsByCode(codeList.get(i));
                GoodsWithNum goodsWithNum = new GoodsWithNum();
                goodsWithNum.setGoods(goodsByCode);
                goodsWithNum.setNum(numList.get(i));
                goodsWithNums.add(goodsWithNum);
            }
            bill.setGoodsWithNumList(goodsWithNums);
        }

        ArrayList<Object> res = new ArrayList<>();
        res.add(billIPage.getTotal());
        res.add(billIPage.getRecords());
        return res;
    }

    @Override
    public String getIncome() {
        return billMapper.getIncome();
    }

    private Map<String, List<String>> getCodeListAndNumList(String goodsCodeAndNum) {
        if (goodsCodeAndNum.endsWith(",")){
            goodsCodeAndNum = goodsCodeAndNum.substring(0, goodsCodeAndNum.length() - 1);
        }
        HashMap<String, List<String>> codeListAndNumList = new HashMap<>();
        ArrayList<String> codeList = new ArrayList<>();
        ArrayList<String> numList = new ArrayList<>();
        String[] items = goodsCodeAndNum.split(",");
        for (String item : items) {
            String[] codeAndNum = item.split(":");
            codeList.add(codeAndNum[0].replaceAll(" ", ""));
            numList.add(codeAndNum[1].replaceAll(" ", ""));
        }
        codeListAndNumList.put("codeList", codeList);
        codeListAndNumList.put("numList", numList);
        return codeListAndNumList;
    }

}
