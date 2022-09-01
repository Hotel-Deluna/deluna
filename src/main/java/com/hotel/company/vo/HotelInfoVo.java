package com.hotel.company.vo;

import com.hotel.common.CommonResponseVo;
import io.micrometer.core.lang.Nullable;
import io.swagger.annotations.ApiModelProperty;
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
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔등록 파라미터")
    public static class RegisterHotelRequest {
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

        @ApiParam(value = "위도, 경도 값. 0번째 배열 : x, 1번째 배열 : y",  required = true, example = "[123.546, 10.48]")
        List<Float> location;

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
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔삭제 파라미터")
    public static class DeleteHotelRequest {
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

        @ApiParam(value = "호텔 정보", required = true, example = "신라스테이 강남점은...")
        String info;

        @ApiParam(value = "호텔 규정", required = true, example = "대욕장 이용안내...")
        String rule;

        @ApiParam(value = "태그(부가시설/서비스) 리스트", required = true, example = "[1,2,3]")
        List<Integer> tags;

        @ApiParam(value = "성수기 리스트", required = true)
        List<PeakSeason> peak_season_list;

        @ApiParam(value = "이미지 리스트 - Object 형식. 특정 순서 이미지가 변경되면 해당 배열 aws 주소를 멀티파트 파일로 교체. ex) 이미지가 2개 있었는데 첫번재 이미지가 수정된다면 [멀티파트 파일 이미지1 , https://aws.bucket/..] ", required = true, example = "[멀티파트 파일 이미지1 , https://aws.bucket/..]")
        List<Object> image;
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
    public static class HotelDetailInfo {

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

        @Schema(description = "호텔 성급", required = true, example = "5")
        Integer star;

        @Schema(description = "호텔 이미지 리스트", required = true, example = "[https://aws.bucket/1, https://aws.bucket/2]")
        List<String> image;

        @Schema(description = "호텔 정보", required = true, example = "신라스테이 강남점은...")
        String info;

        @Schema(description = "호텔 규정", required = true, example = "대욕장 이용안내...")
        String rule;

        @Schema(description = "호텔 성수기 정보 리스트", required = true)
        List<PeakSeason> peak_season_list;

        @Schema(description = "호텔 태그(부가시설/서비스) 구분번호 리스트", required = true, example = "[1,2,3]")
        List<Integer> tags;

        @Schema(description = "객실 정보 리스트", required = true)
        List<RoomInfo> room_list;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실 정보")
    public static class RoomInfo {

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

        @Schema(description = "가격정보 - 성수기 평일", required = true, example = "350000")
        Integer p_weekday_price;

        @Schema(description = "가격정보 - 성수기 주말", required = true, example = "450000")
        Integer p_weekend_price;

        @Schema(description = "가격정보 - 평일", required = true, example = "150000")
        Integer weekday_price;

        @Schema(description = "가격정보 - 주말", required = true, example = "250000")
        Integer weekend_price;

        @Schema(description = "예약가능방 갯수", required = true, example = "3")
        Integer reservable_room_count;

        @Schema(description = "객실 사용 가능 여부 - 객실 사용금지상태일때 false", required = true, example = "true")
        Boolean available_yn;

        @Schema(description = "예약불가 시작일", required = true, example = "2022/08/01")
        Date room_closed_start;

        @Schema(description = "예약불가 종료일", required = true, example = "2022/08/03")
        Date room_closed_end;

        @Schema(description = "객실 태그", required = true)
        List<Integer> tags;

        @Schema(description = "호실 정보", required = true)
        List<RoomDetailInfo> room_detail_info = new ArrayList<>();

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

        @Schema(description = "성수기 시작일")
        Date peak_season_start;

        @Schema(description = "성수기 종료일")
        Date peak_season_end;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실 상세정보 파라미터")
    public static class RoomInfoRequest {
        @Schema(description = "호텔 구분번호",  required = true, example = "12345")
        Integer hotel_num;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실 상세정보리스트 파라미터")
    public static class RoomInfoListRequest {
        @Schema(description = "객실 구분번호",  required = true, example = "12345")
        Integer room_num;
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
    public static class RegisterRoomRequest {
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

        @ApiParam(value = "공휴일 가격 상태 - 공휴일을 성수기 가격취급할건지 결정. 0: 비성수기 주말가격, 1: 성수기 주말가격",  required = true, example = "1")
        Integer holiday_price_status;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "객실 수정 파라미터")
    public static class EditRoomRequest {
        @ApiParam(value = "객실번호",  required = true, example = "123456")
        Integer room_num;

        @ApiParam(value = "객실명",  required = true, example = "신라스테이")
        String name;

        @ApiParam(value = "이미지 리스트 - Object 형식. 특정 순서 이미지가 변경되면 해당 배열 aws 주소를 멀티파트 파일로 교체. ex) 이미지가 2개 있었는데 첫번재 이미지가 수정된다면 [멀티파트 파일 이미지1 , https://aws.bucket/..] ", required = true, example = "[멀티파트 파일 이미지1 , https://aws.bucket/..]")
        List<Object> image;

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

        @ApiParam(value = "공휴일 가격 상태 - 공휴일을 성수기 가격취급할건지 결정. 0: 비성수기 주말가격, 1: 성수기 주말가격",  required = true, example = "1")
        Integer holiday_price_status;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 등록 파라미터")
    public static class RegisterRoomDetailRequest {
        @Schema(description = "호실명", required = true, example = "101호")
        String name;

        @Schema(description = "호실 사용금지 시작일", required = false, example = "2022/08/01")
        Date room_closed_start;

        @Schema(description = "호실 사용금지 해제일", required = false, example = "2022/08/03")
        Date room_closed_end;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 정보")
    public static class RoomDetailInfo {

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

        @Schema(description = "호실 상태값 - 0: 예약가능 1: 예약불가능 ", required = true, example = "1")
        Integer status;

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
        DeleteRoomInfo data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "객실삭제 추가정보 - 객실정보 + 최종예약날짜 정보 제공")
    public static class DeleteRoomInfo extends RoomInfo {
        @Schema(description = "최종예약날짜",  required = true, example = "2022/08/01")
        Date last_reservation_date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 추가 파라미터")
    public static class AddRoomDetailRequest {
        @Schema(description = "객실 구분번호", required = true, example = "12345")
        Integer room_num;

        @Schema(description = "호실명", required = true, example = "101호")
        String name;

        @Schema(description = "호실 사용금지 시작일", required = false, example = "2022/08/01")
        Date room_closed_start;

        @Schema(description = "호실 사용금지 해제일", required = false, example = "2022/08/03")
        Date room_closed_end;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 정보수정 파라미터")
    public static class EditRoomDetailRequest {
        @Schema(description = "호실 구분번호", required = true, example = "12345")
        Integer room_detail_num;

        @Schema(description = "호실명", required = true, example = "101호")
        String name;

        @Schema(description = "호실 사용금지 시작일", required = false, example = "2022/08/01")
        String room_closed_start;

        @Schema(description = "호실 사용금지 해제일", required = false, example = "2022/08/03")
        String room_closed_end;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호실 삭제 추가정보 응답값")
    public static class DeleteRoomDetailInfoResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        DeleteRoomDetailInfo data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실 삭제 추가정보 파라미터")
    public static class DeleteRoomDetailRequest {
        @Schema(description = "호실 구분번호", required = true, example = "12345")
        Integer room_detail_num;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호실삭제 추가정보 - 호실정보 + 최종예약날짜 정보 제공")
    public static class DeleteRoomDetailInfo extends RoomDetailInfo {

        @Schema(description = "최종예약날짜",  required = true, example = "2022/08/01")
        Date last_reservation_date;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "호텔 예약정보 조회 응답값")
    public static class HotelReservationListResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        List<HotelReservation> data;
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
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "호텔 예약정보")
    public static class HotelReservation {
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

        @Schema(description = "예약일",  required = true, example = "2022/08/01")
        Date reservation_date;

        @Schema(description = "예약상태 - 0: 예약확정 1: 예약취소 2: 이용완료",  required = true, example = "0")
        Integer reservation_status;
    }
}
