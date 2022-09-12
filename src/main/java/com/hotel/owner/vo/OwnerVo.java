package com.hotel.owner.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.common.CommonResponseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class OwnerVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Response")
    public static class OwnerInfoResponse extends CommonResponseVo {
        @Schema(description = "사업자 정보 데이터")
        OwnerVo.OwnerInfo data;
    }

    @Data
    @NoArgsConstructor
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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
    @Schema(description = "개업일", required = true, example = "2022/08/10")
    Date opening_day;

    @JsonIgnore
    String is_delete;

    @JsonIgnore
    Date dt_update;

    @JsonIgnore
    String insert_user;

    @JsonIgnore
    String update_user;

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
        Date opening_day;

        @JsonIgnore
        Integer business_user_num;

        @JsonIgnore
        String update_user;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "사업자 탈퇴 사유")
    public static class OwnerWithdrawRequest {
        @Schema(description = "사업자 탈퇴 사유 코드",  required = true, example = "매출없음")
        Integer reason;

        @JsonIgnore
        Integer business_user_num;

        @JsonIgnore
        String insert_user;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "사업자 번호 진위여부 조회 파라미터")
    public static class OwnerVerifyRequest {
        @Schema(description = "사업자 이름", required = true, example = "홍길동")
        String name;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
        @Schema(description = "개업일", required = true, example = "2022/08/10")
        Date opening_day;

        @Schema(description = "사업자번호", required = true, example = "0123456789")
        String business_num;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "사업자 번호 진위여부 조회 응답값")
    public static class OwnerVerifyResponse extends CommonResponseVo {
        @Schema(description = "사업자 정보 데이터")
        Boolean data;
    }

}
