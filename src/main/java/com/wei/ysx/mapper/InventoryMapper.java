package com.wei.ysx.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.ysx.entity.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品库存 Mapper 接口
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 查询所有订单
     * @return
     */
    List<Inventory> listAll();

    /**
     * 查询所有订单
     * @param currentPage
     * @param size
     * @return
     */
    IPage<Inventory> listPage(Page<Inventory> page);

}
