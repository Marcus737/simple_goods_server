package com.wei.ysx.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
public class CreateBillArgument {

    String[] goodsCodes;
    Integer[] nums;
}
