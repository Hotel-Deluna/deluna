package com.hotel.company.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.common.CommonResponseVo;
import io.micrometer.core.lang.Nullable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
        List<HotelSearchInfo> data;

        @Schema(description = "조회된 데이터 총 갯수", required = true, example = "5")
        Integer total_cnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "여행지 목록과 해당 여행지의 호텔 갯수")
    public static class TouristSpotInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<HotelSearchVo.TouristSpotInfo> data;

        @Schema(description = "조회된 데이터 총 갯수", required = true, example = "5")
        Integer total_cnt;
    }

    @Data
    @AllArgsConstructor
    @ApiModel(value = "여행지 정보 Request")
    public static class TouristSpotInfoRequest {
        @Schema(description = "페이지당 데이터 갯수", required = false, example = "5")
        Integer page_cnt;

        @Schema(description = "요청 페이지", required = false, example = "3")
        Integer page;
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
        @Schema(description = "여행지 번호",  required = true)
        Integer tourist_spot_num;

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
        Set<String> region_list;

        @Schema(description = "검색어에 해당되는 호텔의 주소 리스트",  required = false, example = "[노보텔 엠비시티 서울 용산]")
        @Nullable
        Set<String> hotel_address_list;

        @Schema(description = "검색어에 해당되는 호텔명 리스트",  required = false, example = "[서울신라호텔, 밀레니엄 힐튼 서울]")
        @Nullable
        Set<String> hotel_name_list;

        @Schema(description = "검색어에 해당되는 장소 리스트",  required = false, example = "[서울역, 서울스퀘어]")
        @Nullable
        Set<String> place_list;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "호텔 검색 리스트 Request")
    public static class HotelSearchListRequest {
        @ApiParam(value = "사용자가 선택한 검색어 ", example = "서울", required = true)
        String text;

        @ApiModelProperty(value = "사용자가 선택한 검색어 타입 -  \t " +
                "1: 검색어에 해당되는 지역(시,도) 리스트 \t " +
                "2: 검색어에 해당되는 호텔의 주소 리스트 \t " +
                "3: 검색어에 해당되는 호텔명 리스트 \t "+
                "4: 검색어에 해당되는 장소 리스트 \t ", example = "1")
        Integer search_type;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd" , timezone="Asia/Seoul")
        @ApiParam(value = "예약범위 - 시작일", example = "2022/07/01", required = false)
        Date reservation_start_date;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd", timezone="Asia/Seoul")
        @ApiParam(value = "예약범위 - 종료일", example = "2022/07/03", required = false)
        Date reservation_end_date;

        @ApiParam(value = "투숙인원", example = "2", required = false)
        Integer people_count;

        @Schema(description = "페이지당 데이터 갯수", required = false, example = "5")
        Integer page_cnt;

        @Schema(description = "요청 페이지", required = false, example = "3")
        Integer page;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "사용자가 요청한 검색어, 조건에 일치하는 호텔 정보")
    public static class HotelSearchInfo {
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
        List<Integer> tags;

        @Schema(description = "호텔 사용 가능 여부 - 해당 호텔의 모든 객실중 사용가능한 방이 0이면 false", required = true)
        Boolean available_yn;

        @Schema(description = "경도, 위도 값. 0번째 배열 : x, 1번째 배열 : y",  required = true, example = "[123.546, 10.48]")
        List<Double> location;
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
        Integer maximum_price;

        @Schema(description = "호텔 성급", example = "5", required = false)
        Integer star;

        @Schema(description = "호텔 태그 구분번호 리스트",  required = false, example = "[1, 3, 5]")
        List<Integer> hotel_tags;

        @Schema(description = "객실 태그 구분번호 리스트",  required = false, example = "[1, 3, 5]")
        List<Integer> room_tags;

        @Schema(description = "경도, 위도 값. 0번째 배열 : x, 1번째 배열 : y",  required = false, example = "[123.546, 10.48]")
        List<Double> location;

        @Schema(description = "정렬 구분번호 - 1: 호텔등급순 2: 가격높은순 3: 가격낮은순", required = true, example = "3")
        Integer rank_num;

        @Schema(description = "페이지당 데이터 갯수", required = false, example = "5")
        Integer page_cnt;

        @Schema(description = "요청 페이지", required = false, example = "3")
        Integer page;
    }

}
