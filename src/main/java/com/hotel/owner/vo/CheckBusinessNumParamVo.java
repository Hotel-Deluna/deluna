package com.hotel.owner.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "사업자 진위확인")
public class CheckBusinessNumParamVo {
    @ApiModelProperty(value = "사업자번호", required = true, example = "1234567890")
    String business_num;

    @ApiModelProperty(value = "개업일", required = true, example = "20220810")
    String opening_day;

}
