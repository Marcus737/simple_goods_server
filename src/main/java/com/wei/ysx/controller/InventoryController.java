package com.wei.ysx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wei.ysx.entity.Inventory;
import com.wei.ysx.entity.Response;
import com.wei.ysx.service.IGoodsService;
import com.wei.ysx.service.IInventoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品库存 前端控制器
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@RestController
@RequestMapping("//inventory")
public class InventoryController {

    @Autowired
    IGoodsService goodsService;

    @Autowired
    IInventoryService inventoryService;

    @ApiOperation(value = "查询所有库存")
    @GetMapping("/list")
    public Response list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "1000000000") Integer size){
        List  inventoryList = inventoryService.listPage(currentPage, size);
        return Response.success(inventoryList);
    }

    @ApiOperation(value = "更新库存")
    @GetMapping("/updateInventory/goodsId={goodsId}&value={value}")
    public Response updateInventory(@PathVariable Integer goodsId, @PathVariable Integer value){
        if (null == goodsId || null == value){
            return Response.error("更新库存的参数不能为空");
        }
        if (value < 0){
            value = 0;
        }
        Integer isExist = goodsService.query().eq("id", goodsId).count();
        if (isExist != 1){
            return Response.error("该商品不存在，请添加此商品");
        }
        boolean b = inventoryService.update(new UpdateWrapper<Inventory>().eq("goods_id", goodsId).set("value", value));
        if (b){
           return Response.success("更新库存成功");
        }
        return Response.error("更新库存失败");
    }

    @ApiOperation(value = "批量更新库存")
    @PutMapping("/updateInventory/")
    public Response updateInventory(Integer[] goodsIds, Integer[] values){
        if (null == goodsIds || null == values || goodsIds.length != values.length){
            return Response.error("更新库存的参数有误");
        }

        for (int i = 0; i < goodsIds.length; i++) {
            Integer isExist = goodsService.query().eq("id", goodsIds[i]).count();
            if (isExist != 1){
                return Response.error("该商品不存在，请添加此商品");
            }
            boolean b = inventoryService.update(new UpdateWrapper<Inventory>().eq("goods_id", goodsIds[i]).set("value", values[i]));
            if (!b){
                Response.error("批量更新库存失败");
            }
        }
        return Response.success("批量更新库存成功");
    }


}
