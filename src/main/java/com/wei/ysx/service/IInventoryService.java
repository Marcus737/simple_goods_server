package com.wei.ysx.service;

import com.wei.ysx.entity.Inventory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品库存 服务类
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
public interface IInventoryService extends IService<Inventory> {


    /**
     * 查询所有订单
     * @param currentPage
     * @param size
     * @return
     */
    List listPage(Integer currentPage, Integer size);


}
