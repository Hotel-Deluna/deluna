package com.hotel.company.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.common.CommonResponseVo;
import com.hotel.common.dto.CommonDto;
import io.micrometer.core.lang.Nullable;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelInfoVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "사업자 소유 호텔 리스트 응답값")
    public static class OwnerHotelListResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<HotelDetailInfo> data;

        @Schema(description = "조회된 데이터 총 갯수", required = true, example = "5")
        Integer total_cnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 상세정보 응답값")
    public static class HotelDetailInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        HotelDetailInfo data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실 상세정보 응답값")
    public static class RoomInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        RoomInfo data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실 상세정보 리스트 응답값")
    public static class RoomInfoListResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<RoomInfo> data;

        @Schema(description = "조회된 데이터 총 갯수", required = true, example = "5")
        Integer total_cnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실명 중복체크 응답값")
    public static class CheckDuplicateRoomNameResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        boolean data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "사업자 호텔 리스트 조회 파라미터 ")
    public static class OwnerHotelListRequest {
        @Schema(description = "검색어 - 호텔명",  required = false, example = "신라")
        String text;

        @Schema(description = "페이지당 데이터 갯수", required = false, example = "5")
        Integer page_cnt;

        @Schema(description = "요청 페이지", required = false, example = "3")
        Integer page;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔등록 파라미터")
    public static class RegisterHotelRequest extends CommonDto {
        @ApiParam(value = "한글 호텔명",  required = true, example = "신라스테이")
        String name;

        @ApiParam(value = "영문 호텔명", required = true, example = "Shilla Stay")
        String eng_name;

        @ApiParam(value = "호텔 성급", required = true, example = "5")
        Integer star;

        @ApiParam(value = "호텔 전화번호", required = true, example = "0212345678")
        String phone_num;

        @ApiParam(value = "주소 - 지번주소기준", required = true, example = "서울시 강남구")
        String address;

        @ApiParam(value = "경도, 위도 값. 0번째 배열 : x, 1번째 배열 : y",  required = true, example = "[123.546, 10.48]")
        List<Double> location;

        @ApiParam(value = "카카오 API의 지역 구분정보(region_1depth_name) - 특별시,도 정보", required = true, example = "서울시")
        String region_1depth_name;

        @ApiParam(value = "카카오 API의 지역 구분정보(region_2depth_name) - 시,구 정보", required = true, example = "강남구")
        String region_2depth_name;

        @ApiParam(value = "호텔 정보", required = true, example = "신라스테이 강남점은...")
        String info;

        @ApiParam(value = "호텔 규정", required = false, example = "대욕장 이용안내...")
        @Nullable
        String rule;

        @ApiParam(value = "태그(부가시설/서비스) 리스트", required = false, example = "[1,2,3]")
        @Nullable
        List<Integer> tags;

        @ApiParam(value = "성수기 리스트", required = false)
        @Nullable
        List<PeakSeason> peak_season_list;

        @ApiParam(value = "멀티파트파일 이미지 리스트", required = false, example = "[멀티파트 파일 이미지1 , 멀티파트 파일 이미지2]")
        @Nullable
        List<MultipartFile> image;

        // 호텔 등록시 insert에 필요한 정보
        @JsonIgnore
        @ApiParam(value = "사업자 번호",  required = false)
        Integer business_user_num;

        @JsonIgnore
        @ApiParam(value = "위도 값",  required = false)
        Double latitude;

        @JsonIgnore
        @ApiParam(value = "경도 값",  required = false)
        Double longitude;

        @JsonIgnore
        @ApiParam(value = "공휴일 가격 상태", required = false)
        Integer holiday_price_status;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔삭제 파라미터")
    public static class DeleteHotelRequest extends CommonDto{
        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;

        @Schema(description = "호텔 삭제 사유",  required = true, example = "파산")
        String reason;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔정보 수정 파라미터")
    public static class EditInfoHotelRequest {
        @ApiParam(value = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;

        @ApiParam(value = "한글 호텔명",  required = true, example = "신라스테이")
        String name;

        @ApiParam(value = "영문 호텔명", required = true, example = "Shilla Stay")
        String eng_name;

        @ApiParam(value = "호텔 성급", required = true, example = "5")
        Integer star;

        @ApiParam(value = "호텔 전화번호", required = true, example = "0212345678")
        String phone_num;

        @ApiParam(value = "주소 - 지번주소기준", required = true, example = "서울시 강남구")
        String address;

        @ApiParam(value = "경도, 위도 값. 0번째 배열 : x, 1번째 배열 : y",  required = true, example = "[123.546, 10.48]")
        List<Double> location;

        @ApiParam(value = "카카오 API의 지역 구분정보(region_1depth_name) - 특별시,도 정보", required = true, example = "서울시")
        String region_1depth_name;

        @ApiParam(value = "카카오 API의 지역 구분정보(region_2depth_name) - 시,구 정보", required = true, example = "강남구")
        String region_2depth_name;

        @ApiParam(value = "호텔 정보", required = true, example = "신라스테이 강남점은...")
        String info;

        @ApiParam(value = "호텔 규정", required = true, example = "대욕장 이용안내...")
        String rule;

        @ApiParam(value = "태그(부가시설/서비스) 리스트", required = true, example = "[1,2,3]")
        List<Integer> tags;

        @ApiParam(value = "성수기 리스트", required = true)
        List<PeakSeason> peak_season_list;

        @ApiParam(value = "이미지 리스트 - 이미지 멀티파트 파일 리스트", required = true, example = "[멀티파트 파일 이미지1 , 멀티파트 파일 이미지2]")
        List<MultipartFile> image;

        // 호텔 수정시 update에 필요한 정보
        @JsonIgnore
        @ApiParam(value = "사업자 번호",  required = false)
        Integer business_user_num;

        @JsonIgnore
        @ApiParam(value = "위도 값",  required = false)
        Double latitude;

        @JsonIgnore
        @ApiParam(value = "경도 값",  required = false)
        Double longitude;

        @JsonIgnore
        @ApiParam(value = "변경자",  required = false)
        String update_user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 상세정보 파라미터")
    public static class HotelDetailInfoRequest {
        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔 상세 정보")
    public static class HotelDetailInfo extends CommonDto {

        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;

        @Schema(description = "한글 호텔명",  required = true, example = "신라스테이")
        String name;

        @Schema(description = "영문 호텔명", required = true, example = "Shilla Stay")
        String eng_name;

        @Schema(description = "전화번호", required = true, example = "0212345678")
        String phone_num;

        @Schema(description = "주소", required = true, example = "서울특별시 강남구")
        String address;

        @Schema(description = "경도, 위도 값. 0번째 배열 : x, 1번째 배열 : y",  required = true, example = "[123.546, 10.48]")
        List<Double> location;

        @Schema(description = "카카오 API의 지역 구분정보(region_1depth_name) - 특별시,도 정보", required = true, example = "서울시")
        String region_1depth_name;

        @Schema(description = "카카오 API의 지역 구분정보(region_2depth_name) - 시,구 정보", required = true, example = "강남구")
        String region_2depth_name;

        @Schema(description = "호텔 성급", required = true, example = "5")
        Integer star;

        @Schema(description = "호텔 이미지 리스트", required = true, example = "[https://aws.bucket/1, https://aws.bucket/2]")
        List<String> image;

        @Schema(description = "호텔 정보", required = true, example = "신라스테이 강남점은...")
        String info;

        @Schema(description = "호텔 규정", required = false, example = "대욕장 이용안내...")
        String rule;

        @Schema(description = "호텔 성수기 정보 리스트", required = false)
        List<PeakSeason> peak_season_list;

        @Schema(description = "호텔 태그(부가시설/서비스) 구분번호 리스트", required = false, example = "[1,2,3]")
        List<Integer> tags;

        @Schema(description = "객실 정보 리스트", required = false)
        List<RoomInfo> room_list;

        // DB
        @JsonIgnore
        @ApiParam(value = "사업자 번호",  required = false)
        Integer business_user_num;

        @JsonIgnore
        @ApiParam(value = "위도 값",  required = false)
        Double latitude;

        @JsonIgnore
        @ApiParam(value = "경도 값",  required = false)
        Double longitude;

        @JsonIgnore
        @ApiParam(value = "공휴일 가격 상태", required = false)
        Integer holiday_price_status;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실 정보")
    public static class RoomInfo extends CommonDto {
        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;

        @Schema(description = "객실 구분번호",  required = true, example = "12345")
        Integer room_num;

        @Schema(description = "객실명",  required = true, example = "신라스테이")
        String name;

        @Schema(description = "객실 이미지 리스트", required = true, example = "[https://aws.bucket/1, https://aws.bucket/2]")
        List<String> image;

        @Schema(description = "객실 최소인원", required = true, example = "2")
        Integer minimum_people;

        @Schema(description = "객실 최대인원", required = true, example = "3")
        Integer maximum_people;

        @Schema(description = "더블배드 갯수", required = true, example = "1")
        Integer double_bed_count;

        @Schema(description = "싱글배드 갯수", required = true, example = "1")
        Integer single_bed_count;

        @Schema(description = "체크인 시간", required = true, example = "11:00")
        String check_in_time;

        @Schema(description = "체크아웃 시간", required = true, example = "15:00")
        String check_out_time;

        @Schema(description = "금일 가격 - 해당 날짜 성수기, 비성수기, 평일, 주말 구분해서 제공", required = true, example = "150000")
        Integer price;

        @Schema(description = "가격정보 - 성수기 평일", required = false, example = "350000")
        Integer p_weekday_price;

        @Schema(description = "가격정보 - 성수기 주말", required = false, example = "450000")
        Integer p_weekend_price;

        @Schema(description = "가격정보 - 평일", required = true, example = "150000")
        Integer weekday_price;

        @Schema(description = "가격정보 - 주말", required = true, example = "250000")
        Integer weekend_price;

        @Schema(description = "예약가능방 갯수", required = true, example = "3")
        Integer reservable_room_count;

        @Schema(description = "객실 태그", required = false)
        List<Integer> tags;

        @Schema(description = "호실 정보", required = false)
        List<RoomDetailInfo> room_detail_info = new ArrayList<>();

        @Schema(description = "객실 삭제 예정일", required = false, example = "2022/08/03")
        Date delete_date;

        @Schema(description = "최종예약날짜", required = false, example = "2022/08/03")
        Date last_reservation_date;

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

        @Schema(description = "호텔 메인 이미지 S3값", required = true, example = "https://aws.bucket/....")
        String image;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔의 성수기 정보")
    public static class PeakSeason {

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
        @Schema(description = "성수기 시작일", required = false)
        Date peak_season_start;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
        @Schema(description = "성수기 종료일", required = false)
        Date peak_season_end;

        @JsonIgnore
        @Schema(description = "성수기 번호", required = false)
        Integer peak_num;

        @JsonIgnore
        @Schema(description = "호텔 번호", required = false)
        Integer hotel_num;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실 상세정보 파라미터")
    public static class RoomInfoRequest {
        @Schema(description = "객실 구분번호",  required = true, example = "12345")
        Integer room_num;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실 상세정보리스트 파라미터")
    public static class RoomInfoListRequest {
        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;

        @Schema(description = "페이지당 데이터 갯수", required = false, example = "5")
        Integer page_cnt;

        @Schema(description = "요청 페이지", required = false, example = "3")
        Integer page;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실명 중복체크 파라미터")
    public static class CheckDuplicateRoomNameRequest {
        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;

        @Schema(description = "객실명",  required = true, example = "스탠다드 더블")
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실 등록 파라미터")
    public static class RegisterRoomRequest extends CommonDto {
        @ApiParam(value = "호텔 구분번호", required = true, example = "12345")
        Integer hotel_num;

        @ApiParam(value = "객실명",  required = true, example = "신라스테이")
        String name;

        @ApiParam(value = "객실 이미지 리스트", required = true, example = "[멀티파트 파일 이미지1, 멀티파트 파일 이미지2]")
        List<MultipartFile> image;

        @ApiParam(value = "객실 최소인원", required = true, example = "2")
        Integer minimum_people;

        @ApiParam(value = "객실 최대인원", required = true, example = "3")
        Integer maximum_people;

        @ApiParam(value = "더블배드 갯수", required = true, example = "1")
        Integer double_bed_count;

        @ApiParam(value = "싱글배드 갯수", required = true, example = "1")
        Integer single_bed_count;

        @ApiParam(value = "체크인 시간", required = true, example = "11:00")
        String check_in_time;

        @ApiParam(value = "체크아웃 시간", required = true, example = "15:00")
        String check_out_time;

        @ApiParam(value = "가격정보 - 성수기 평일", required = true, example = "350000")
        Integer p_weekday_price;

        @ApiParam(value = "가격정보 - 성수기 주말", required = true, example = "450000")
        Integer p_weekend_price;

        @ApiParam(value = "가격정보 - 평일", required = true, example = "150000")
        Integer weekday_price;

        @ApiParam(value = "가격정보 - 주말", required = true, example = "250000")
        Integer weekend_price;

        @ApiParam(value = "객실 태그", required = true)
        List<Integer> tags;

        @ApiParam(value = "호실 정보", required = true)
        List<RegisterRoomDetailRequest> room_detail_list;

        @ApiParam(value = "공휴일 가격 상태 - 공휴일을 성수기 가격취급할건지 결정. 1: 비성수기 주말가격, 2: 성수기 주말가격",  required = true, example = "1")
        Integer holiday_price_status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실 수정 파라미터")
    public static class EditRoomRequest extends CommonDto {
        @ApiParam(value = "호텔 구분번호", required = true, example = "12345")
        Integer hotel_num;

        @ApiParam(value = "객실번호",  required = true, example = "123456")
        Integer room_num;

        @ApiParam(value = "객실명",  required = true, example = "신라스테이")
        String name;

        @ApiParam(value = "이미지 리스트 - 이미지 멀티파트 파일 리스트", required = true, example = "[멀티파트 파일 이미지1 , 멀티파트 파일 이미지2]")
        List<MultipartFile> image;

        @ApiParam(value = "객실 최소인원", required = true, example = "2")
        Integer minimum_people;

        @ApiParam(value = "객실 최대인원", required = true, example = "3")
        Integer maximum_people;

        @ApiParam(value = "더블배드 갯수", required = true, example = "1")
        Integer double_bed_count;

        @ApiParam(value = "싱글배드 갯수", required = true, example = "1")
        Integer single_bed_count;

        @ApiParam(value = "체크인 시간", required = true, example = "11:00")
        String check_in_time;

        @ApiParam(value = "체크아웃 시간", required = true, example = "15:00")
        String check_out_time;

        @ApiParam(value = "가격정보 - 성수기 평일", required = true, example = "350000")
        Integer p_weekday_price;

        @ApiParam(value = "가격정보 - 성수기 주말", required = true, example = "450000")
        Integer p_weekend_price;

        @ApiParam(value = "가격정보 - 평일", required = true, example = "150000")
        Integer weekday_price;

        @ApiParam(value = "가격정보 - 주말", required = true, example = "250000")
        Integer weekend_price;

        @ApiParam(value = "객실 태그", required = true, example = "[1,3,5]")
        List<Integer> tags;

        @ApiParam(value = "공휴일 가격 상태 - 공휴일을 성수기 가격취급할건지 결정. 1: 비성수기 주말가격, 2: 성수기 주말가격",  required = true, example = "1")
        Integer holiday_price_status;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 등록 파라미터")
    public static class RegisterRoomDetailRequest extends CommonDto {
        @Schema(description = "객실 구분번호", required = true)
        Integer room_num;

        @Schema(description = "호실명", required = true, example = "101호")
        String name;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
        @Schema(description = "호실 사용금지 시작일", required = false, example = "2022/08/01")
        Date room_closed_start;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
        @Schema(description = "호실 사용금지 해제일", required = false, example = "2022/08/03")
        Date room_closed_end;

        // DB
        @JsonIgnore
        @Schema(description = "객실상태값", required = false, hidden = true)
        Integer room_detail_status;

        @JsonIgnore
        @Schema(description = "호실 삭제 예정일", required = false, example = "2022/08/03", hidden = true)
        Date delete_date;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 정보")
    public static class RoomDetailInfo extends CommonDto {
        @Schema(description = "호실 구분번호",  required = true, example = "12345")
        Integer room_detail_num;

        @Schema(description = "호실명",  required = true, example = "101호")
        String name;

        @Schema(description = "호실 사용금지 시작일", required = false, example = "2022/08/01")
        Date room_closed_start;

        @Schema(description = "호실 사용금지 해제일", required = false, example = "2022/08/03")
        Date room_closed_end;

        @Schema(description = "호실 삭제 예정일", required = false, example = "2022/08/31")
        Date delete_date;

        @Schema(description = "호실 상태값 - 1: 예약가능 2: 예약불가능 ", required = true, example = "1")
        Integer status;

        @Schema(description = "호실 사용 가능 여부 - 호실 사용금지상태일때 false", required = true, example = "true")
        Boolean available_yn;

        @JsonIgnore
        @Schema(description = "객실 구분번호",  required = true, example = "12345")
        Integer room_num;

        @JsonIgnore
        @Schema(description = "예약 상태 구분번호",  required = true, example = "12345")
        Integer reservation_status;

        @Schema(description = "최종예약날짜",  required = true, example = "2022/08/01")
        Date last_reservation_date;

        @Schema(description = "객실명",  required = false, example = "스탠다드 트윈룸")
        String room_name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실 삭제 파라미터")
    public static class DeleteRoomRequest {
        @Schema(description = "객실번호",  required = true, example = "123456")
        Integer room_num;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실삭제 추가정보 응답값")
    public static class DeleteRoomInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        RoomInfo data;
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Schema(description = "객실삭제 추가정보 - 객실정보 + 최종예약날짜 정보 제공")
//    public static class DeleteRoomInfo extends RoomInfo {
//        @Schema(description = "최종예약날짜",  required = true, example = "2022/08/01")
//        Date last_reservation_date;
//    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Schema(description = "호실 추가 파라미터")
//    public static class AddRoomDetailRequest extends CommonDto {
//        @Schema(description = "객실 구분번호", required = true, example = "12345")
//        Integer room_num;
//
//        @Schema(description = "호실명", required = true, example = "101호")
//        String name;
//
//        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
//        @Schema(description = "호실 사용금지 시작일", required = false, example = "2022/08/01")
//        Date room_closed_start;
//
//        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
//        @Schema(description = "호실 사용금지 해제일", required = false, example = "2022/08/03")
//        Date room_closed_end;
//
//        @JsonIgnore
//        @Schema(description = "객실 상태값", required = true)
//        Integer room_detail_status;
//
//    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 이용불가설정 파라미터")
    public static class DisableSettingRoomDetailRequest extends CommonDto {
        @Schema(description = "호실 구분번호", required = true, example = "12345")
        Integer room_detail_num;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
        @Schema(description = "호실 사용금지 시작일", required = true, example = "2022/08/01")
        Date room_closed_start;

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
        @Schema(description = "호실 사용금지 해제일", required = true, example = "2022/08/03")
        Date room_closed_end;

        @JsonIgnore
        @Schema(description = "객실 상태값", required = true, hidden = true)
        Integer room_detail_status;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호실 삭제 추가정보 응답값")
    public static class DeleteRoomDetailInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        RoomDetailInfo data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 삭제 추가정보 파라미터")
    public static class DeleteRoomDetailRequest {
        @Schema(description = "호실 구분번호", required = true, example = "12345")
        Integer room_detail_num;
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Schema(description = "호실삭제 추가정보 - 호실정보 + 최종예약날짜 정보 제공")
//    public static class DeleteRoomDetailInfo extends RoomDetailInfo {
//
//        @Schema(description = "최종예약날짜",  required = true, example = "2022/08/01")
//        Date last_reservation_date;
//    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 예약정보 조회 응답값")
    public static class HotelReservationListResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<HotelReservationDetailInfo> data;

        @Schema(description = "조회된 데이터 총 갯수", required = true, example = "5")
        Integer total_cnt;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔 예약정보 리스트 조회 파라미터")
    public static class HotelReservationListRequest {
        @Schema(description = "호텔 구분번호", required = true, example = "12345")
        Integer hotel_num;

        @Schema(description = "예약번호", required = false, example = "12345")
        Integer reservation_num;

        @Schema(description = "예약자명",  required = false, example = "홍길동")
        String customer_name;

        @Schema(description = "예약자 핸드폰번호",  required = false, example = "01012345678")
        String customer_phone_num;

        @Schema(description = "예약일자",  required = false, example = "2022/09/01")
        Date reservation_date;

        @Schema(description = "정렬 구분번호 - 1: 전체 2: 예약확정 3: 예약취소 4: 이용완료", required = true, example = "3")
        Integer rank_num;

        @Schema(description = "페이지당 데이터 갯수", required = false, example = "5")
        Integer page_cnt;

        @Schema(description = "요청 페이지", required = false, example = "3")
        Integer page;

        @JsonIgnore
        @Schema(description = "호실 정보", required = false, hidden = true)
        List<Integer> room_detail_num_list;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔 예약정보")
    public static class HotelReservationInfo {
        @Schema(description = "조회된 데이터 총 갯수", required = true, example = "5")
        Integer total_cnt;

        @Schema(description = "호텔 예약정보 리스트",  required = true)
        List<HotelReservationDetailInfo> hotel_reservation_info_list;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔 예약정보")
    public static class HotelReservationDetailInfo {
        @Schema(description = "예약번호",  required = true, example = "123456")
        Integer reservation_num;

        @Schema(description = "호텔명",  required = true, example = "신라스테이")
        String hotel_name;

        @Schema(description = "객실명",  required = true, example = "스탠다드 트윈룸")
        String room_name;

        @Schema(description = "예약자명",  required = true, example = "홍길동")
        String customer_name;

        @Schema(description = "예약자 핸드폰번호",  required = true, example = "01012345678")
        String customer_phone_num;

        @Schema(description = "예약일",  required = true, example = "2022/08/01 ~ 2022/08/02")
        String reservation_date;

        @Schema(description = "예약상태 - 1: 예약확정 2: 예약취소 3: 이용완료",  required = true, example = "1")
        Integer reservation_status;

        // DB
        @JsonIgnore
        @ApiParam(value = "입실일", required = true)
        Date st_date;

        @JsonIgnore
        @ApiParam(value = "퇴실일", required = true)
        Date ed_date;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "태그 참조")
    public static class Tags extends CommonDto {

        @ApiParam(value = "pk 번호", required = true)
        Integer pk_num;

        @ApiParam(value = "태그 번호", required = true)
        Integer tag_num;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "이미지 정보")
    public static class ImageInfo extends CommonDto {

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
    @Schema(description = "삭제 테이블 정보")
    public static class DeleteTable {

        @ApiParam(value = "PK", required = true)
        Integer pk;

        @ApiParam(value = "사유", required = false)
        Integer reason;

        @ApiParam(value = "생성일", required = false)
        Date dt_inert;

        @ApiParam(value = "생성자", required = false)
        String insert_user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "삭제 예약 파라미터")
    public static class UpdateDeleteDateRequest {

        @ApiParam(value = "PK", required = true)
        List<Integer> pk;

        @ApiParam(value = "삭제 예약일", required = true)
        Date delete_date;
    }
}
