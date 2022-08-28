package com.hotel.company.vo;

import com.hotel.common.CommonResponseVo;
import io.micrometer.core.lang.Nullable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class HotelSearchVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "검색바 자동완성 검색결과")
    public static class SearchBarResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        HotelSearchData data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "사용자 검색어,조건에 해당하는 호텔 리스트")
    public static class SearchListResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<HotelSearchList> data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "여행지 목록과 해당 여행지의 호텔 갯수")
    public static class TouristSpotInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<TouristSpotInfo> data;
    }

    @Data
    @AllArgsConstructor
    @ApiModel(value = "검색바 Request")
    public static class SearchBarRequest {
        @ApiModelProperty(value = "메인페이지 검색바 사용자 검색어", example = "서울", required = true)
        String text;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "여행지 정보")
    public static class TouristSpotInfo {
        @Schema(description = "여행지명",  required = true, example = "서울")
        String tourist_spot_name;

        @Schema(description = "여행지 이미지",  required = true, example = "https://aws.bucket/")
        String image;

        @Schema(description = "호텔 갯수",  required = true, example = "2500")
        Integer hotel_count;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "메인페이지 검색바 - 사용자 검색어에 해당하는 키워드 리스트")
    public static class HotelSearchData {
        @Schema(description = "검색어에 해당되는 지역(시,도) 리스트",  required = false, example = "[서울]")
        @Nullable
        List<String> region_list;

        @Schema(description = "검색어에 해당되는 호텔의 주소 리스트",  required = false, example = "[노보텔 엠비시티 서울 용산]")
        @Nullable
        List<String> hotel_address_list;

        @Schema(description = "검색어에 해당되는 호텔명 리스트",  required = false, example = "[서울신라호텔, 밀레니엄 힐튼 서울]")
        @Nullable
        List<String> hotel_name_list;

        @Schema(description = "검색어에 해당되는 장소 리스트",  required = false, example = "[서울역, 서울스퀘어]")
        @Nullable
        List<String> place_list;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "호텔 검색 리스트 Request")
    public static class HotelSearchListRequest {
        @ApiParam(value = "사용자가 선택한 검색어 ", example = "서울", required = true)
        String text;

        @ApiModelProperty(value = "사용자가 선택한 검색어 타입 -  \t " +
                "0: 검색어에 해당되는 지역(시,도) 리스트 \t " +
                "1: 검색어에 해당되는 호텔의 주소 리스트 \t " +
                "2: 검색어에 해당되는 호텔명 리스트 \t "+
                "3: 검색어에 해당되는 장소 리스트 \t ", example = "1")
        Integer search_type;

        @ApiParam(value = "예약범위 - 시작일", example = "2022/07/01", required = false)
        @Nullable
        Date reservation_start_date;

        @ApiParam(value = "예약범위 - 종료일", example = "2022/07/03", required = false)
        @Nullable
        Date reservation_end_date;

        @ApiParam(value = "투숙인원", example = "2", required = false)
        @Nullable
        Integer people_count;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "사용자가 요청한 검색어, 조건에 일치하는 호텔의 리스트")
    public static class HotelSearchList {
        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;

        @Schema(description = "호텔 메인 이미지",  required = true, example = "https://aws.bucket/")
        String image;

        @Schema(description = "호텔명",  required = true, example = "신라스테이 서초점")
        String name;

        @Schema(description = "호텔 영문명",  required = true, example = "Shilla Stay Seocho")
        String eng_name;

        @Schema(description = "호텔 성급",  required = true, example = "5")
        Integer star;

        @Schema(description = "해당 호텔 최소가격",  required = true, example = "130000")
        Integer minimum_price;

        @Schema(description = "호텔 태그 구분번호 리스트",  required = false, example = "[1, 2]")
        @Nullable
        List<Integer> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔 검색 리스트 필터 파라미터")
    public static class HotelSearchListFilterRequest {
        @Schema(description = "호텔 구분번호 리스트",  required = true, example = "[12345, 45678]")
        List<Integer> hotel_num;

        @Schema(description = "가격범위 - 최소가", required = false, example = "10000")
        @Nullable
        Integer minimum_price;

        @Schema(description = "가격범위 - 최대가", required = false, example = "200000")
        @Nullable
        Integer maximum_price;

        @Schema(description = "호텔 성급", example = "5", required = false)
        @Nullable
        Integer star;

        @Schema(description = "호텔 태그 구분번호 리스트",  required = false, example = "[1, 3, 5]")
        @Nullable
        List<Integer> tags;

        @Schema(description = "위도, 경도 값. 0번째 배열 : x, 1번째 배열 : y",  required = false, example = "[123.546, 10.48]")
        @Nullable
        List<Float> location;

        @Schema(description = "정렬 구분번호 - 0: 호텔등급순 1: 가격높은순 2: 가격낮은순", required = true, example = "3")
        Integer rank_num;
    }

}
