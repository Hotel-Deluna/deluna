package com.hotel.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.common.CommonResponseVo;
import com.hotel.common.dto.CommonDto;
import com.hotel.company.vo.HotelInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "공휴일")
    public static class Holiday {
        @Schema(description = "공휴일명")
        String holiday_name;

        @Schema(description = "공휴일 년/월/일")
        Date holiday_date;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "여행지 이미지 등록 파라미터")
    public static class InsertTouristSpotImageRequest extends CommonDto {
        @ApiParam(value = "여행지 구분번호", required = true, example = "1")
        Integer tourist_spot_num;

        @ApiParam(value = "여행지 이미지", required = true, example = "멀티파트 파일 이미지")
        MultipartFile image;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "여행지 등록 파라미터")
    public static class InsertTouristSpotRequest extends CommonDto {
        @ApiParam(value = "여행지명", required = true, example = "서울")
        String name;

        @ApiParam(value = "여행지 이미지", required = false, example = "멀티파트 파일 이미지")
        MultipartFile image;
    }

}
