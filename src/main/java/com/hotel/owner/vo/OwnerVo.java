package com.hotel.owner.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.common.CommonResponseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OwnerVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Response")
    public static class OwnerInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        OwnerVo.OwnerInfo data;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "사업자 회원가입 파라미터")
    public static class OwnerSignUpRequest{

    @Schema(description = "이메일", required = true, example = "hotel@naver.com")
    String email;

    @Schema(description = "비밀번호", required = true, example = "123456")
    String password;

    @Schema(description = "사업자 이름", required = true, example = "홍길동")
    String name;

    @Schema(description = "핸드폰번호", required = true, example = "01012345678")
    String phone_num;

    @Schema(description = "사업자번호", required = true, example = "1234567890")
    String business_num;

    @Schema(description = "개업일", required = true, example = "20220810")
    String opening_day;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "사업자 정보")
    public static class OwnerInfo {

        @Schema(description = "이메일", required = true, example = "abc@hotel.com")
        String email;

        @Schema(description = "사업자명", required = true, example = "홍길동")
        String name;

        @Schema(description = "사업자 전화번호", required = true, example = "01012345678")
        String phone_num;

        @Schema(description = "사업자번호", required = true, example = "0123456789")
        String business_num;

        @Schema(description = "개업일", required = true, example = "2022/08/25")
        String opening_day;

    }

    @Data
    @AllArgsConstructor
    @Schema(description = "사업자 탈퇴 사유")
    public static class OwnerWithdrawRequest {
        @Schema(description = "사업자 탈퇴 사유",  required = true, example = "매출없음")
        String reason;

        @JsonIgnore
        String ignores;
    }

}
