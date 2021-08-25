package com.wei.ysx.mapper;

import com.wei.ysx.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.ysx.entity.Response;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 判断该商品是否存在数据库中
     * @param code
     * @return
     */
    boolean isGoodsExist(@Param(value = "code") String code);

    /**
     * 根据条形码获取商品信息
     * @param code
     * @return
     */
    Goods getGoodsByCode(@Param(value = "code") String code);

    /**
     * 使商品下架
     * @param code
     * @return
     */
    boolean disableGoods(@Param(value = "code") String code);

    /**
     * 使商品上架
     * @param code
     * @return
     */
    boolean enableGoods(String code);

    /**
     * 根据物品code更新物品信息
     * @param goods
     * @return
     */
    boolean updateGoodsByCode(Goods goods);
}
