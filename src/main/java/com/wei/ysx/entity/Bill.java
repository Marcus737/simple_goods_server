package com.wei.ysx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Order对象", description="订单表")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private String id;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "YYYY-MM-dd  hh:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单状态:0未支付,1已支付")
    private Integer state;

    @ApiModelProperty(value = "总价格")
    private Float totalPrice;

    @ApiModelProperty(value = "商品id和数量")
    private String goodsCodeAndNum;

    @ApiModelProperty(value = "带商品数量的商品")
    @TableField(exist = false)
    private List<GoodsWithNum> goodsWithNumList;

}
