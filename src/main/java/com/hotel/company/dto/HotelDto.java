package com.hotel.company.dto;

import com.hotel.common.dto.CommonDto;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class HotelDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "호텔 테이블")
    public static class HotelTable extends CommonDto {

        @ApiParam(value = "호텔 번호", required = true, example = "5")
        Integer hotel_num;

        @ApiParam(value = "사업자 회원 번호", required = true, example = "5")
        Integer business_user_num;

        @ApiParam(value = "호텔 전화번호", required = true, example = "0212345678")
        String phone_num;

        @ApiParam(value = "주소 - 지번주소기준", required = true, example = "서울시 강남구")
        String address;

        @ApiParam(value = "위도 값",  required = true)
        Double latitude;

        @ApiParam(value = "경도 값",  required = true)
        Double longitude;

        @ApiParam(value = "한글 호텔명",  required = true, example = "신라스테이")
        String name;

        @ApiParam(value = "영문 호텔명", required = true, example = "Shilla Stay")
        String eng_name;

        @ApiParam(value = "호텔 정보", required = true, example = "신라스테이 강남점은...")
        String hotel_info;

        @ApiParam(value = "호텔 규정", required = false, example = "대욕장 이용안내...")
        String hotel_rule;

        @ApiParam(value = "호텔 성급", required = true, example = "5")
        Integer hotel_star;

        @ApiParam(value = "공휴일 가격 상태", required = true)
        Integer holiday_price_status;

        @ApiParam(value = "카카오 API의 지역 구분정보(region_1depth_name) - 특별시,도 정보", required = true)
        String region_code;

        @ApiParam(value = "카카오 API의 지역 구분정보(region_2depth_name) - 시,구 정보", required = true)
        String region_address;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "성수기 테이블")
    public static class PeekSeasonTable extends CommonDto {

        @Schema(description = "성수기 시작일", required = true)
        Date peak_season_std;

        @Schema(description = "성수기 종료일", required = true)
        Date peak_season_end;

        @ApiParam(value = "호텔번호", required = true)
        Integer hotel_num;

        @ApiParam(value = "성수기 번호", required = true)
        Integer peak_num;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 태그 참조 테이블")
    public static class HotelTagsTable extends CommonDto {

        @ApiParam(value = "호텔 번호", required = true)
        Integer hotel_num;

        @ApiParam(value = "호텔 태그 번호", required = true)
        Integer hotel_tag_num;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "이미지 테이블")
    public static class ImageTable extends CommonDto {

        @ApiParam(value = "이미지 번호", required = true)
        Integer image_num;

        @ApiParam(value = "구분값", required = true)
        Integer select_type;

        @ApiParam(value = "고유값", required = true)
        Integer primary_key;

        @ApiParam(value = "사진명", required = true)
        String picture_name;

        @ApiParam(value = "버킷주소", required = true)
        String bucket_url;

        @ApiParam(value = "사진순서", required = true)
        Integer picture_sequence;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "여행지 테이블")
    public static class TouristSpotTable extends CommonDto {

        @ApiParam(value = "여행지 번호", required = true)
        Integer tourist_spot_num;

        @ApiParam(value = "여행지 이름", required = true)
        String name;

        @ApiParam(value = "해당 여행지 호텔 갯수", required = true)
        Integer count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "특정 호실 예약날짜")
    public static class RoomDetailReservationDate {
        @ApiParam(value = "입실일", required = true)
        Date st_date;

        @ApiParam(value = "퇴실일", required = true)
        Date ed_date;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 예약 리스트")
    public static class HotelReservationList {
        @ApiParam(value = "예약 구분번호", required = true)
        Integer reservation_num;

        @ApiParam(value = "예약자", required = true)
        String reservation_name;

        @ApiParam(value = "예약자 핸드폰 번호", required = true)
        String reservation_phone;

        @ApiParam(value = "객실 이름", required = true)
        String room_name;

        @ApiParam(value = "입실일", required = true)
        Date st_date;

        @ApiParam(value = "퇴실일", required = true)
        Date ed_date;

        @ApiParam(value = "호실번호", required = false)
        Integer room_detail_num;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 검색바 조회 결과")
    public static class HotelSearchBarData {
        @ApiParam(value = "호텔명", required = true)
        String name;

        @ApiParam(value = "영문 호텔명", required = true)
        String eng_name;

        @ApiParam(value = "주소", required = true)
        String address;

        @ApiParam(value = "지역코드", required = true)
        String region_code;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 검색 - 예약범위 조회")
    public static class SelectHotelReservationListForSearch {
        @ApiParam(value = "호텔 구분번호", required = true)
        Integer hotel_num;

        @ApiParam(value = "예약범위 - 시작일", required = true)
        Date reservation_start_date;

        @ApiParam(value = "예약범위 - 종료일", required = true)
        Date reservation_end_date;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "예약희망 범위에 해당 호텔에 예약가능한 호실 조회")
    public static class selectDatePeriodCheckRoomDetailInfoParam {
        @ApiParam(value = "호실 번호", required = true)
        List<Integer> room_detail_num;

        @ApiParam(value = "예약범위 - 시작일", required = true)
        Date start_dt;

        @ApiParam(value = "예약범위 - 종료일", required = true)
        Date end_dt;

    }


}
