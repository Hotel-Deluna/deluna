package com.hotel.owner.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "사업자 회원가입 파라미터")
public class OwnerSignUpParamVo {
    @ApiModelProperty(value = "이메일",  required = true, example = "hotel@naver.com")
    String email;

    @ApiModelProperty(value = "비밀번호", required = true, example = "123456")
    String password;

    @ApiModelProperty(value = "사업자 이름", required = true, example = "홍길동")
    String name;

    @ApiModelProperty(value = "핸드폰번호", required = true, example = "01012345678")
    String phone_num;

    @ApiModelProperty(value = "사업자번호", required = true, example = "1234567890")
    String business_num;

    @ApiModelProperty(value = "개업일", required = true, example = "20220810")
    String opening_day;

}
