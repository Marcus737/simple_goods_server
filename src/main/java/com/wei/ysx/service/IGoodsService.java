package com.wei.ysx.service;

import com.wei.ysx.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.ysx.entity.Response;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 根据条形码数字获取商品信息
     * @param code
     * @return
     */
    Response getGoodsInfoByCode(String code);

    /**
     * 使商品下架
     * @param code
     * @return
     */
    Response disableGoods(String code);

    /**
     * 使商品上架
     * @param code
     * @return
     */
    Response enableGoods(String code);

    /**
     * 判断该商品是否存在数据库中
     * @param code
     * @return
     */
    boolean isGoodsExist(String code);

    /**
     * 根据物品code更新物品信息
     * @param goods
     * @return
     */
    Response updateGoodsByCode(Goods goods);

    /**
     * 根据商品code获取商品信息
     * @param code
     * @return
     */
    Goods getGoodsByCode(String code);

    /**
     * 获取全部商品信息
     * @return
     */
    List<Goods> listGoods(Integer currentPage, Integer size);

    /**
     * 手动添加商品
     * @param goods
     * @return
     */
    Response addGoodsByManual(Goods goods);

    void clearGoodsRedis();
}
