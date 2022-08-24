package com.hotel.owner.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "회원가입 응답")
public class OwnerSignUpResponseVo {

    @ApiModelProperty(value = "이름", required = true, example = "홍길동")
    String name;

}
