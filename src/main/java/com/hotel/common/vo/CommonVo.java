package com.hotel.common.vo;

import com.hotel.common.CommonResponseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommonVo {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "휴대폰 인증 확인 응답값")
    public static class VerifyPhoneAuthResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        Boolean data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "휴대폰 인증 요청 파라미터")
    public static class PhoneAuthRequest {
        @Schema(description = "휴대폰 번호",  required = true, example = "01012345678")
        String phone_num;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "휴대폰 인증 확인 파라미터")
    public static class VerifyPhoneAuthRequest {
        @Schema(description = "휴대폰 번호",  required = true, example = "01012345678")
        String phone_num;

        @Schema(description = "인증번호 - 6자리 String",  required = true, example = "123456")
        String auth_num;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "이메일 중복 확인 응답값")
    public static class EmailDuplicateCheckResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        Boolean data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "이메일 중복 확인 파라미터")
    public static class EmailDuplicateCheckRequest {
        @Schema(description = "일반회원, 사업자 구분값 - 0: 일반회원 1: 사업자",  required = true, example = "1")
        Integer role;

        @Schema(description = "이메일 번호",  required = true, example = "abc@hotel.com")
        String email;
    }
}
