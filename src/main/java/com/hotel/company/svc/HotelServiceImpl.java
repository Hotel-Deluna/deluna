package com.hotel.company.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.Util;
import com.hotel.company.vo.*;
import io.micrometer.core.lang.Nullable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Override
    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList(@Nullable HotelInfoVo.OwnerHotelListRequest ownerHotelListRequest) {
        HotelInfoVo.OwnerHotelListResponse result = new HotelInfoVo.OwnerHotelListResponse();

        try{
            List<HotelInfoVo.HotelDetailInfo> hotel_list = new ArrayList<>(); // 해당 사업자 정보로 소유호텔 리스트 조회

            HotelInfoVo.HotelDetailInfo hotelDetailInfo = new HotelInfoVo.HotelDetailInfo();

            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);


            HotelInfoVo.RoomInfo room_b = new HotelInfoVo.RoomInfo();
            room_b.setRoom_num(45678);
            room_b.setName("디럭스 더블");
            room_b.setImage(roomImageList);
            room_b.setMinimum_people(2);
            room_b.setMaximum_people(3);
            room_b.setDouble_bed_count(2);
            room_b.setSingle_bed_count(1);
            room_b.setCheck_in_time("15:00");
            room_b.setCheck_out_time("11:00");
            room_b.setPrice(330000);
            room_b.setReservable_room_count(2);
            room_b.setAvailable_yn(false);
            room_b.setTags(roomTagList);

            List<HotelInfoVo.RoomInfo> room_list = new ArrayList<>();
            room_list.add(room_a);
            room_list.add(room_b);

            //성수기
            HotelInfoVo.PeakSeason season_a = new HotelInfoVo.PeakSeason();
            season_a.setPeak_season_start(Util.stringToDateMonth("07/01"));
            season_a.setPeak_season_end(Util.stringToDateMonth("08/31"));

            HotelInfoVo.PeakSeason season_b = new HotelInfoVo.PeakSeason();
            season_b.setPeak_season_start(Util.stringToDateMonth("11/01"));
            season_b.setPeak_season_end(Util.stringToDateMonth("01/31"));

            // 호텔
            List<String> hotelImageList = new ArrayList<>();
            hotelImageList.add("https://aws.bucket/45");
            hotelImageList.add("https://aws.bucket/67");

            List<HotelInfoVo.PeakSeason> hotelPeakSeasonList = new ArrayList<>();
            hotelPeakSeasonList.add(season_a);
            hotelPeakSeasonList.add(season_b);

            List<Integer> hotelTagList = new ArrayList<>();
            hotelTagList.add(4);
            hotelTagList.add(5);

            hotelDetailInfo.setHotel_num(123);
            hotelDetailInfo.setName("신라스테이");
            hotelDetailInfo.setEng_name("Shilla Stay");
            hotelDetailInfo.setAddress("서울특별시 강남구");
            hotelDetailInfo.setPhone_num("0212345678");
            hotelDetailInfo.setStar(5);
            hotelDetailInfo.setImage(hotelImageList);
            hotelDetailInfo.setInfo("서울 강남에 위치한 본 호텔은...");
            hotelDetailInfo.setRule("라멘 서비스 및 대욕장 이용안내...");
            hotelDetailInfo.setPeak_season_list(hotelPeakSeasonList);
            hotelDetailInfo.setTags(hotelTagList);
            hotelDetailInfo.setRoom_list(room_list);

            hotel_list.add(hotelDetailInfo);

            result.setMessage("호텔 리스트 조회 완료");
            result.setData(hotel_list);

        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelRequest) {
        CommonResponseVo result = new CommonResponseVo();
        // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장

        result.setMessage("호텔 등록 완료");
        return result;
    }

    @Override
    public HotelSearchVo.SearchBarResponse TouristSpotInfo() {
        return null;
    }

    @Override
    public HotelSearchVo.SearchBarResponse SearchBar(HotelSearchVo.SearchBarRequest searchBarVoRequest) {
        // 검색어에 일치하는 지역명, 호텔명, 호텔주소 있는지 DB 조회

        /*
         검색어에 가장 가까운 호텔 정보 나타내주는거 가능할듯
         카카오 해당 검색어에 가장 가까운 검색어를 리턴해주는 api가 존재함 대신 공식 api는 아님 https://devtalk.kakao.com/t/api/108598
         검색어 정보 api로 검색어에 가장 가까운 장소 3개 정도 리턴. 비공식 api라 그런가 선정기준이 있진않아보임..
         */

        HotelSearchVo.SearchBarResponse result = new HotelSearchVo.SearchBarResponse();
        HotelSearchVo.HotelSearchData HotelSearchData = new HotelSearchVo.HotelSearchData();

        List<String> region_list = new ArrayList<>();
        region_list.add("서울");

        List<String> place_list = new ArrayList<>();
        place_list.add("서울역");
        place_list.add("서울대입구");

       List<String> hotel_address_list = new ArrayList<>();
        hotel_address_list.add("노보텔 엠비시티 서울 용산");

        List<String> hotel_name_list = new ArrayList<>();
        hotel_name_list.add("서울신라호텔");
        hotel_name_list.add("밀레니엄 힐튼 서울");

        HotelSearchData.setRegion_list(region_list);
        HotelSearchData.setHotel_address_list(hotel_address_list);
        HotelSearchData.setHotel_name_list(hotel_name_list);
        HotelSearchData.setPlace_list(place_list);

        result.setMessage("검색 완료");
        result.setData(HotelSearchData);

        return result;

    }

    @Override
    public HotelSearchVo.SearchListResponse SearchHotelList(HotelSearchVo.HotelSearchListRequest searchBarVoSearchBarRequest) {
        HotelSearchVo.SearchListResponse result = new HotelSearchVo.SearchListResponse();
        List<HotelSearchVo.HotelSearchList> hotelSearchList = new ArrayList<>();
        HotelSearchVo.HotelSearchList hotel_a = new HotelSearchVo.HotelSearchList();
        HotelSearchVo.HotelSearchList hotel_b = new HotelSearchVo.HotelSearchList();
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(3);
        tags.add(5);

        hotel_a.setHotel_num(12345);
        hotel_a.setImage("https://aws.bucket/123");
        hotel_a.setName("신라스테이 서초점");
        hotel_a.setEng_name("Shilla Stay Seocho");
        hotel_a.setStar(5);
        hotel_a.setMinimum_price(330000);
        hotel_a.setTags(tags);

        hotel_b.setHotel_num(45678);
        hotel_b.setImage("https://aws.bucket/456");
        hotel_b.setName("힐튼");
        hotel_b.setEng_name("Hilton");
        hotel_b.setStar(5);
        hotel_b.setMinimum_price(230000);
        hotel_b.setTags(tags);

        hotelSearchList.add(hotel_a);
        hotelSearchList.add(hotel_b);

        result.setMessage("호텔 검색 리스트 조회 완료");
        result.setData(hotelSearchList);

        return result;
    }

    @Override
    public HotelSearchVo.SearchListResponse SearchHotelListFilter(HotelSearchVo.HotelSearchListFilterRequest hotelSearchListFilterRequest) {

        // 전달받은 호텔 번호 목록을 필터 조건에 따라 필터링

        HotelSearchVo.SearchListResponse result = new HotelSearchVo.SearchListResponse();
        List<HotelSearchVo.HotelSearchList> hotelSearchFilterList = new ArrayList<>();
        HotelSearchVo.HotelSearchList hotel_a = new HotelSearchVo.HotelSearchList();
        HotelSearchVo.HotelSearchList hotel_b = new HotelSearchVo.HotelSearchList();
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(3);
        tags.add(5);

        hotel_a.setHotel_num(12345);
        hotel_a.setImage("https://aws.bucket/123");
        hotel_a.setName("신라스테이 서초점");
        hotel_a.setEng_name("Shilla Stay Seocho");
        hotel_a.setStar(5);
        hotel_a.setMinimum_price(330000);
        hotel_a.setTags(tags);

        hotel_b.setHotel_num(45678);
        hotel_b.setImage("https://aws.bucket/456");
        hotel_b.setName("밀레니엄 힐튼 서울");
        hotel_b.setEng_name("Hilton");
        hotel_b.setStar(5);
        hotel_b.setMinimum_price(230000);
        hotel_b.setTags(tags);

        hotelSearchFilterList.add(hotel_b);
        hotelSearchFilterList.add(hotel_a);

        result.setMessage("호텔 검색 필터 조회 완료");
        result.setData(hotelSearchFilterList);

        return result;
    }

    @Override
    public HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo(HotelInfoVo.HotelDetailInfoRequest hotelDetailInfoRequest) {
        HotelInfoVo.HotelDetailInfoResponse result = new HotelInfoVo.HotelDetailInfoResponse();

        try{
            HotelInfoVo.HotelDetailInfo hotelDetailInfo = new HotelInfoVo.HotelDetailInfo();
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);


            HotelInfoVo.RoomInfo room_b = new HotelInfoVo.RoomInfo();
            room_b.setRoom_num(45678);
            room_b.setName("디럭스 더블");
            room_b.setImage(roomImageList);
            room_b.setMinimum_people(2);
            room_b.setMaximum_people(3);
            room_b.setDouble_bed_count(2);
            room_b.setSingle_bed_count(1);
            room_b.setCheck_in_time("15:00");
            room_b.setCheck_out_time("11:00");
            room_b.setPrice(330000);
            room_b.setReservable_room_count(2);
            room_b.setAvailable_yn(false);
            room_b.setTags(roomTagList);

            List<HotelInfoVo.RoomInfo> room_list = new ArrayList<>();
            room_list.add(room_a);
            room_list.add(room_b);

            //성수기
            HotelInfoVo.PeakSeason season_a = new HotelInfoVo.PeakSeason();
            season_a.setPeak_season_start(Util.stringToDateMonth("07/01"));
            season_a.setPeak_season_end(Util.stringToDateMonth("08/31"));

            HotelInfoVo.PeakSeason season_b = new HotelInfoVo.PeakSeason();
            season_b.setPeak_season_start(Util.stringToDateMonth("11/01"));
            season_b.setPeak_season_end(Util.stringToDateMonth("01/31"));

            // 호텔
            List<String> hotelImageList = new ArrayList<>();
            hotelImageList.add("https://aws.bucket/45");
            hotelImageList.add("https://aws.bucket/67");

            List<HotelInfoVo.PeakSeason> hotelPeakSeasonList = new ArrayList<>();
            hotelPeakSeasonList.add(season_a);
            hotelPeakSeasonList.add(season_b);

            List<Integer> hotelTagList = new ArrayList<>();
            hotelTagList.add(4);
            hotelTagList.add(5);

            hotelDetailInfo.setHotel_num(123);
            hotelDetailInfo.setName("신라스테이");
            hotelDetailInfo.setEng_name("Shilla Stay");
            hotelDetailInfo.setAddress("서울특별시 강남구");
            hotelDetailInfo.setPhone_num("0212345678");
            hotelDetailInfo.setStar(5);
            hotelDetailInfo.setImage(hotelImageList);
            hotelDetailInfo.setInfo("서울 강남에 위치한 본 호텔은...");
            hotelDetailInfo.setRule("라멘 서비스 및 대욕장 이용안내...");
            hotelDetailInfo.setPeak_season_list(hotelPeakSeasonList);
            hotelDetailInfo.setTags(hotelTagList);
            hotelDetailInfo.setRoom_list(room_list);

            result.setMessage("호텔 상세정보 조회 완료");
            result.setData(hotelDetailInfo);
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo DeleteHotel(HotelInfoVo.DeleteHotelRequest deleteHotelRequest) {
        // 삭제 사유 DB 저장 후 소프트 삭제

        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호텔 삭제 완료");
        return result;
    }

    @Override
    public CommonResponseVo EditHotel(HotelInfoVo.RegisterHotelRequest editHotelRequest) {
        // 호텔정보 수정 정보 DB 업데이트
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호텔 정보 수정 완료");

        return result;
    }

    @Override
    public CommonResponseVo RegisterRoom(HotelInfoVo.RegisterRoomRequest registerRoomRequest) {
        // 객실정보 수정 정보 DB 업데이트
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("객실 정보 수정 완료");
        return result;
    }

    @Override
    public HotelInfoVo.RoomInfoResponse RoomInfo(HotelInfoVo.RoomInfoRequest roomInfoRequest) {
        HotelInfoVo.RoomInfoResponse result = new HotelInfoVo.RoomInfoResponse();

        try {
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);

            // 호실
            HotelInfoVo.RoomDetailInfo room_detail_a = new HotelInfoVo.RoomDetailInfo();
            HotelInfoVo.RoomDetailInfo room_detail_b = new HotelInfoVo.RoomDetailInfo();
            List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = new ArrayList<>();
            room_detail_a.setRoom_detail_num(123);
            room_detail_a.setName("101호");
            room_detail_a.setStatus(0);

            room_detail_b.setRoom_detail_num(123);
            room_detail_b.setName("102호");
            room_detail_b.setStatus(1);
            room_detail_b.setRoom_closed_start(Util.stringToDate("2022/08/01"));
            room_detail_b.setRoom_closed_end(Util.stringToDate("2022/08/05"));
            room_detail_b.setDelete_date(Util.stringToDate("2022/08/30"));

            roomDetailInfoList.add(room_detail_a);
            roomDetailInfoList.add(room_detail_b);
            room_a.setRoom_detail_info(roomDetailInfoList);

            result.setMessage("객실 정보 제공 완료");
            result.setData(room_a);

        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo EditRoom(HotelInfoVo.EditRoomRequest registerRoomRequest) {
        CommonResponseVo result = new CommonResponseVo();

        result.setMessage("객실 정보 수정 완료");
        return result;
    }

    @Override
    public HotelInfoVo.CheckDuplicateRoomNameResponse CheckDuplicateRoomName(HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest) {
        HotelInfoVo.CheckDuplicateRoomNameResponse result = new HotelInfoVo.CheckDuplicateRoomNameResponse();
        Boolean CheckDuplicateResult = true;
        result.setMessage("객실명 중복 조회 완료");
        result.setData(CheckDuplicateResult);
        return result;
    }

    @Override
    public CommonResponseVo DeleteRoom(HotelInfoVo.DeleteRoomRequest deleteRoomRequest) {
        CommonResponseVo result = new CommonResponseVo();

        result.setMessage("객실 삭제처리 완료");
        return result;
    }

    @Override
    public HotelInfoVo.DeleteRoomInfoResponse DeleteRoomInfo(HotelInfoVo.DeleteRoomRequest deleteRoomRequest) {
        HotelInfoVo.DeleteRoomInfoResponse result = new HotelInfoVo.DeleteRoomInfoResponse();

        try{
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.DeleteRoomInfo room_a = new HotelInfoVo.DeleteRoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);
            room_a.setLast_reservation_date(Util.stringToDate("2022/08/01"));

            result.setMessage("객실 삭제추가정보 조회 완료");
            result.setData(room_a);
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public HotelInfoVo.RoomInfoListResponse RoomInfoList(HotelInfoVo.RoomInfoRequest roomInfoRequest) {
        HotelInfoVo.RoomInfoListResponse result = new HotelInfoVo.RoomInfoListResponse();

        try{
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);

            HotelInfoVo.RoomInfo room_b = new HotelInfoVo.RoomInfo();
            room_b.setRoom_num(45678);
            room_b.setName("디럭스 더블");
            room_b.setImage(roomImageList);
            room_b.setMinimum_people(2);
            room_b.setMaximum_people(3);
            room_b.setDouble_bed_count(2);
            room_b.setSingle_bed_count(1);
            room_b.setCheck_in_time("15:00");
            room_b.setCheck_out_time("11:00");
            room_b.setPrice(330000);
            room_b.setReservable_room_count(2);
            room_b.setAvailable_yn(false);
            room_b.setTags(roomTagList);

            // 호실
            HotelInfoVo.RoomDetailInfo room_detail_a = new HotelInfoVo.RoomDetailInfo();
            HotelInfoVo.RoomDetailInfo room_detail_b = new HotelInfoVo.RoomDetailInfo();
            List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = new ArrayList<>();
            room_detail_a.setRoom_detail_num(123);
            room_detail_a.setName("101호");
            room_detail_a.setStatus(0);

            room_detail_b.setRoom_detail_num(123);
            room_detail_b.setName("102호");
            room_detail_b.setStatus(1);
            room_detail_b.setRoom_closed_start(Util.stringToDate("2022/08/01"));
            room_detail_b.setRoom_closed_end(Util.stringToDate("2022/08/05"));
            room_detail_b.setDelete_date(Util.stringToDate("2022/08/30"));

            roomDetailInfoList.add(room_detail_a);
            roomDetailInfoList.add(room_detail_b);
            room_a.setRoom_detail_info(roomDetailInfoList);

            List<HotelInfoVo.RoomInfo> room_list = new ArrayList<>();
            room_list.add(room_a);
            room_list.add(room_b);

            result.setMessage("객실 리스트 조회 완료");
            result.setData(room_list);
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo AddRoomDetail(HotelInfoVo.AddRoomDetailRequest addRoomDetailRequest) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호실 추가 완료");

        return result;
    }

    @Override
    public CommonResponseVo EditRoomDetail(HotelInfoVo.EditRoomDetailRequest editRoomDetailRequest) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호실 정보 수정 완료");

        return result;
    }

    @Override
    public CommonResponseVo DeleteRoomDetail(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호실 삭제 완료");

        return result;
    }

    @Override
    public HotelInfoVo.DeleteRoomDetailInfoResponse DeleteRoomDetailInfo(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest) {
        HotelInfoVo.DeleteRoomDetailInfoResponse result = new HotelInfoVo.DeleteRoomDetailInfoResponse();

        try{
            HotelInfoVo.DeleteRoomDetailInfo deleteRoomDetailInfo = new HotelInfoVo.DeleteRoomDetailInfo();

            deleteRoomDetailInfo.setRoom_detail_num(123);
            deleteRoomDetailInfo.setName("101호");
            deleteRoomDetailInfo.setStatus(0);
            deleteRoomDetailInfo.setLast_reservation_date(Util.stringToDate("2022/08/01"));

            result.setData(deleteRoomDetailInfo);
            result.setMessage("호실 삭제 추가정보 조회 완료");
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public HotelInfoVo.HotelReservationListResponse HotelReservationList(HotelInfoVo.HotelReservationListRequest deleteRoomDetailRequest) {
        HotelInfoVo.HotelReservationListResponse result = new HotelInfoVo.HotelReservationListResponse();

        try{
            HotelInfoVo.HotelReservation reservation_a = new HotelInfoVo.HotelReservation();
            HotelInfoVo.HotelReservation reservation_b = new HotelInfoVo.HotelReservation();
            List<HotelInfoVo.HotelReservation> reservationsList = new ArrayList<>();

            reservation_a.setReservation_num(12345);
            reservation_a.setHotel_name("신라스테이");
            reservation_a.setRoom_name("스탠다드 트윈");
            reservation_a.setCustomer_name("홍길동");
            reservation_a.setCustomer_phone_num("01012345678");
            reservation_a.setReservation_date(Util.stringToDate("2022/08/01"));
            reservation_a.setReservation_status(0);

            reservation_b.setReservation_num(45678);
            reservation_b.setHotel_name("신라스테이");
            reservation_b.setRoom_name("디럭스 더블");
            reservation_b.setCustomer_name("홍길동");
            reservation_b.setCustomer_phone_num("01012345678");
            reservation_b.setReservation_date(Util.stringToDate("2022/08/20"));
            reservation_b.setReservation_status(1);

            reservationsList.add(reservation_a);
            reservationsList.add(reservation_b);

            result.setMessage("호텔 예약정보 조회 완료");
        }catch (Exception e){

        }

        return result;
    }

}
