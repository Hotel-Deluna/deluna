package com.hotel.company.vo;

import com.hotel.common.CommonResponseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class HotelInfoVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "사업자 소유 호텔 리스트 데이터")
    public static class OwnerHotelListResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<HotelInfoVo.OwnerHotel> data;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "사업자 호텔등록 파라미터")
    public class RegisterHotelRequest {
        @Schema(description = "한글 호텔명",  required = true, example = "신라스테이")
        String name;

        @Schema(description = "영문 호텔명", required = true, example = "Shilla Stay")
        String eng_name;

        @Schema(description = "호텔 성급", required = true, example = "5")
        Integer star;

        @Schema(description = "호텔 전화번호", required = true, example = "0212345678")
        String phone_num;

        @Schema(description = "호텔 정보", required = true, example = "신라스테이 강남점은...")
        String hotel_info;

        @Schema(description = "호텔 규정", required = true, example = "대욕장 이용안내...")
        String hotel_rule;

        @Schema(description = "태그(부가시설/서비스) 리스트", required = true, example = "[1,2,3]")
        List<Integer> tags;

        @Schema(description = "성수기 리스트", required = true)
        List<HotelInfoVo.PeakSeason> peak_season_list;

        @Schema(description = "멀티파트파일 이미지 리스트", required = true)
        List<MultipartFile> image;
    }

    @Data
    @NoArgsConstructor
    @Schema(description = "사업자 소유 호텔 리스트")
    public static class OwnerHotelList {

        @Schema(description = "사업자 소유 호텔 리스트", required = true)
        List<OwnerHotel> hotel_list;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "사업자 소유 호텔 정보")
    public static class OwnerHotel {

        @Schema(description = "호텔구분값", required = true, example = "123456")
        Integer hotel_num;

        @Schema(description = "호텔명", required = true, example = "신라스테이 강남점")
        String name;

        @Schema(description = "호텔 전화번호", required = true, example = "0212345678")
        String phone_num;

        @Schema(description = "호텔 메인 이미지", required = true, example = "https://aws.bucket/....")
        String image;

    }

    @Data
    @AllArgsConstructor
    @Schema(description = "호텔의 성수기 정보")
    public class PeakSeason {

        @Schema(description = "성수기 시작일", example = "2022/07/25")
        String peak_season_start;

        @Schema(description = "성수기 종료일", example = "2022/09/01")
        String peak_season_end;
    }

}
