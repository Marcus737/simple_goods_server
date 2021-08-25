package com.wei.ysx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Goods对象", description="商品表")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品名字")
    private String goodsName;

    @ApiModelProperty(value = "条形码")
    private String code;

    @ApiModelProperty(value = "价格")
    private Float price;

    @ApiModelProperty(value = "厂商")
    private String manuName;

    @ApiModelProperty(value = "规格")
    private String spec;

    @ApiModelProperty(value = "商标/品牌")
    private String trademark;

    @ApiModelProperty(value = "图片地址")
    private String img;

    @ApiModelProperty(value = "货物类型")
    private String goodsType;

    @ApiModelProperty(value = "备注信息")
    private String note;

    @ApiModelProperty(value = "厂商地址")
    private String manuAddress;

    @ApiModelProperty(value = "1可用/0不可用")
    private boolean enable;

}
