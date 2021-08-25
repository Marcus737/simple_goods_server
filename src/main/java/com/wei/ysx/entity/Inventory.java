package com.wei.ysx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品库存
 * </p>
 *
 * @author Marcus
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Inventory对象", description="商品库存")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品库存id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @ApiModelProperty(value = "该商品剩余数")
    private Integer value;

    @ApiModelProperty(value = "商品信息")
    @TableField(exist = false)
    private Goods goods;

}
