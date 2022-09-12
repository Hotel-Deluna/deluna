package com.hotel.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.common.CommonResponseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
        @Schema(description = "휴대폰 번호",  required = true, example = "01068774919")
        String phone_num;

        @JsonIgnore
        String auth_num; // 휴대폰 DB저장할때 쓰기위해 추가
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
        @Schema(description = "일반회원, 사업자 구분값 - 1: 일반회원 2: 사업자",  required = true, example = "1")
        Integer role;

        @Schema(description = "이메일 번호",  required = true, example = "abc@hotel.com")
        String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "공통코드 응답값")
    public static class CommonCodeResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        Object data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "공통코드")
    public static class CommonCode {
        @Schema(description = "코드번호")
        Integer code;

        @Schema(description = "코드 이름")
        String name;
    }

}
