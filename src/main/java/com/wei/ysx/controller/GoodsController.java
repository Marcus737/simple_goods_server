package com.wei.ysx.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wei.ysx.entity.Goods;
import com.wei.ysx.entity.Response;
import com.wei.ysx.service.IGoodsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@RestController
@RequestMapping("//goods")
public class GoodsController {


    @Autowired
    IGoodsService goodsService;

    @GetMapping("/listGoods")
    @ApiOperation(value = "获取全部商品信息")
    public Response listGoods(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "1000000000") Integer size){

        return Response.success(goodsService.listGoods(currentPage, size));
    }

    @ApiOperation(value = "手动添加商品")
    @PostMapping("/addGoodsByManual")
    public Response addGoodsByManual(@RequestBody Goods goods){
        return goodsService.addGoodsByManual(goods);
    }


    @GetMapping("/getGoodsInfoByCode/code={code}")
    @ApiOperation(value = "根据条形码数字获取商品信息")
    public Response getGoodsInfoByCode(@PathVariable String code){
        if ("".equals(code.trim())){
            return Response.error("code不能为空");
        }
        return goodsService.getGoodsInfoByCode(code);
    }

    @GetMapping("/disableGoods/code={code}")
    @ApiOperation(value = "使商品下架")
    public Response disableGoods(@PathVariable String code){

        return goodsService.disableGoods(code);
    }

    @GetMapping("/enableGoods/code={code}")
    @ApiOperation(value = "使商品上架")
    public Response enableGoods(@PathVariable String code){
        return goodsService.enableGoods(code);
    }


    @PutMapping("/updateGoodsByCode")
    @ApiOperation(value = "根据物品code更新物品信息")
    public Response updatePrice(@RequestBody Goods goods){
        if (null == goods.getCode()){
            return Response.error("商品条形码不能为空");
        }
        boolean exist = goodsService.isGoodsExist(goods.getCode());
        if (!exist){
            return Response.error("该商品不存在，请前往添加商品页面添加此商品");
        }
        goods.setId(null);
        boolean success = goodsService.update(goods, new UpdateWrapper<Goods>().eq("code", goods.getCode()));
        if (success){
            goodsService.clearGoodsRedis();
            return Response.success("更新成功");
        }
        return Response.error("更新失败");
    }
}
