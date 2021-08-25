package com.wei.ysx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.ysx.entity.Bill;
import com.wei.ysx.entity.Inventory;
import com.wei.ysx.entity.Response;
import com.wei.ysx.mapper.InventoryMapper;
import com.wei.ysx.service.IInventoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品库存 服务实现类
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

    @Autowired
    InventoryMapper inventoryMapper;

    /**
     * 查询所有订单
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public List listPage(Integer currentPage, Integer size) {
        Page<Inventory> page = new Page<>(currentPage, size);
        IPage<Inventory> iPage =  inventoryMapper.listPage(page);
        ArrayList<Object> res = new ArrayList<>();
        res.add(iPage.getTotal());
        res.add(iPage.getRecords());
        return res;
    }


}
