package com.wei.ysx.service;

import com.wei.ysx.entity.Bill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.ysx.entity.Response;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
public interface IBillService extends IService<Bill> {

    /**
     * 创建订单
     * @param goodsIds
     * @param goodsNums
     * @return
     */
    Response createBill(String[] goodsCodes, Integer[] goodsNums);


    /**
     * 查询所有订单 分页
     * @param currentPage
     * @param size
     * @return
     */
    List listBillByPage(Integer currentPage, Integer size);

    String getIncome();
}
