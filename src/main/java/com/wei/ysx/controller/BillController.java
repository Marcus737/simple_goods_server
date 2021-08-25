package com.wei.ysx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wei.ysx.entity.Bill;
import com.wei.ysx.entity.CreateBillArgument;
import com.wei.ysx.entity.Response;
import com.wei.ysx.service.IBillService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@RestController
@RequestMapping("//bill")
public class BillController {

    @Autowired
    IBillService billService;

    @ApiOperation("获取当天收入")
    @GetMapping("/getIncome")
    public Response getIncome(){
        String income = billService.getIncome();
        return Response.success("ok",income);
    }

    @PutMapping ("/createBill")
    @ApiOperation("创建订单")
    public Response createBill(@RequestBody CreateBillArgument argument){
        if (null == argument.getGoodsCodes() || null == argument.getNums()
                || argument.getGoodsCodes().length != argument.getNums().length){
            return Response.error("订单参数有误");
        }
        return billService.createBill(argument.getGoodsCodes(), argument.getNums());
    }

    @GetMapping("/list")
    @ApiOperation("查询所有订单")
    public Response list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "1000000000") Integer size){

        List bills =  billService.listBillByPage(currentPage, size);

        return Response.success(bills);
    }

    @GetMapping("/noPayBills")
    @ApiOperation("未支付的订单")
    public Response noPayBills(){
        return Response.success(2);
    }

    @GetMapping("/pay/{billId}")
    @ApiOperation("使订单支付")
    public Response pay(@PathVariable String billId){
        return Response.success("订单已修改成已支付",billService.update(new UpdateWrapper<Bill>().eq("id", billId).set("state", 1)));
    }
    @GetMapping("/cancelPay/{billId}")
    @ApiOperation("使订单未支付")
    public Response cancelPay(@PathVariable String billId){
        return Response.success("订单已修改成未支付", billService.update(new UpdateWrapper<Bill>().eq("id", billId).set("state", 0)));
    }
}
