package com.wei.ysx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsWithNum {

    @ApiModelProperty("商品数量")
    private String num;

    @ApiModelProperty("商品对象")
    private Goods goods;
}
