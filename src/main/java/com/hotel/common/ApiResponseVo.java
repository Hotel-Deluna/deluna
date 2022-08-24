package com.hotel.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@ApiModel(description = "통합 응답 Vo")
public class ApiResponseVo {
    @ApiModelProperty(value = "결과", required = true, example = "OK")
    String result;

    @ApiModelProperty(value = "메시지", required = true, example = "가입완료")
    String message;

    @ApiModelProperty(value = "데이터", required = true)
    Object data = new HashMap<>(); // default 값 = {}

}
