package com.wei.ysx.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.ysx.entity.Bill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
public interface BillMapper extends BaseMapper<Bill> {

    /**
     * 打印所有订单
     * @return
     */
    List<Bill> listBill();

    /**
     * 打印所有订单
     * @param page
     * @return
     */
    IPage<Bill> listBillByPage(Page<Bill> page);

    String getIncome();
}
