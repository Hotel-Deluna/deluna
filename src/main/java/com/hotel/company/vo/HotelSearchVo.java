package com.hotel.company.vo;

import com.hotel.common.CommonResponseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class HotelSearchVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Response")
    public static class Response extends CommonResponseVo {
        @Schema(description = "데이터")
        HotelSearchVo.resultData data;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "Search Bar Request")
    public static class SearchBarRequest {
        @Schema(description = "검색바 사용자 검색어")
        String text;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "검색 자동완성")
    public static class resultData {
        @Schema(description = "검색어에 해당되는 지역 리스트",  required = true)
        List<String> region_list;

        @Schema(description = "검색어에 해당되는 호텔의 주소 리스트",  required = true)
        List<String> hotel_address_list;

        @Schema(description = "검색어에 해당되는 호텔명 리스트",  required = true)
        List<String> hotel_name_list;
    }

}
