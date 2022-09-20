package com.hotel.company.dto;

import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
@Mapper
public interface HotelMapper {
    // 호텔
    void insertHotelInfo(HotelInfoVo.RegisterHotelRequest registerHotelRequest) throws Exception;

    void insertHotelTags(HotelInfoVo.Tags tags) throws Exception;

    void updateHotelInfo(HotelInfoVo.EditInfoHotelRequest editInfoHotelRequest) throws Exception;

    HotelInfoVo.HotelDetailInfo selectHotelInfo(int hotel_num) throws Exception;

    void deleteHotelTags(int hotel_num) throws Exception;

    List<Integer> selectHotelTags(int hotel_num) throws Exception;

    void deleteHotel(int hotel_num) throws Exception;

    void insertHotelDeleteReason(HotelInfoVo.DeleteHotelRequest deleteHotelRequest) throws Exception;

    void deleteHotelCancelReservation(List<Integer> room_detail_num_list);

    void insertRoomDelete(HotelInfoVo.DeleteTable deleteTable) throws Exception;

    void insertRoomDetailDelete(HotelInfoVo.DeleteTable deleteTable) throws Exception;

    List<Integer> selectOwnerHotelList(int business_user_num) throws Exception;

    List<HotelInfoVo.HotelReservationDetailInfo> selectHotelReservationList(HotelInfoVo.HotelReservationListRequest hotelReservationListRequest) throws Exception;

    int selectHotelReservationListCount(HotelInfoVo.HotelReservationListRequest hotelReservationListRequest) throws Exception;

    // 성수기
    void insertPeakSeason(HotelDto.PeekSeasonTable peekSeasonTable) throws Exception;

    void softDeletePeakSeason(int hotel_num) throws Exception;

    List<HotelInfoVo.PeakSeason> selectPeakSeasonList(int hotel_num) throws Exception;

    // 이미지
    void insertImage(HotelDto.ImageTable imageTable) throws Exception;

    List<String> selectImageList(HotelInfoVo.ImageInfo imageParams) throws Exception;

    void deleteImage(HotelDto.ImageTable imageTable) throws Exception;

    // 객실
    void insertRoomInfo(HotelInfoVo.RegisterRoomRequest registerRoomRequest) throws Exception;

    String selectDuplicateRoomName(HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest) throws Exception;

    void updateHolidayPriceStatus(HotelInfoVo.RegisterRoomRequest registerRoomRequest) throws Exception;

    void insertRoomTags(HotelInfoVo.Tags tags) throws Exception;

    List<HotelInfoVo.RoomInfo> selectRoomInfoList(int hotel_num) throws Exception;

    HotelInfoVo.RoomInfo selectRoomInfo(int room_num) throws Exception;

    List<Integer> selectRoomTags(int room_num) throws Exception;

    void updateRoomDeleteDate(HotelInfoVo.UpdateDeleteDateRequest updateDeleteDateRequest) throws Exception;

    void deleteRoom(List<Integer> room_num_list) throws Exception;

    void updateRoomInfo(HotelInfoVo.EditRoomRequest editRoomRequest) throws Exception;

    void deleteRoomTags(int room_num) throws Exception;

    // 호실
    void insertRoomDetailInfo(HotelInfoVo.RegisterRoomDetailRequest registerRoomDetailRequest) throws Exception;

    List<HotelInfoVo.RoomDetailInfo> selectRoomDetailInfo(int room_num) throws Exception;

    Date selectLastReservationDate(List<Integer> room_detail_num_list);

    void updateRoomDetailDeleteDate(HotelInfoVo.UpdateDeleteDateRequest updateDeleteDateRequest) throws Exception;

    void deleteRoomDetail(List<Integer> room_num_list) throws Exception;

    HotelInfoVo.RoomDetailInfo selectRoomDetailByDetailNum(int room_detail_num) throws Exception;

    List<HotelDto.RoomDetailReservationDate> selectRoomDetailReservationDate(int room_detail_num) throws Exception;

    void updateDisableRoomDetail(HotelInfoVo.DisableSettingRoomDetailRequest disableSettingRoomDetailRequest) throws Exception;

    // 공휴일
    String selectHoliday(Date today);

    List<String> selectTodayPeakSeason();

    // 검색
    Set<String> selectHotelSearchBarName(String text) throws Exception;

    Set<String> selectHotelSearchBarAddress(String text) throws Exception;

    Set<String> selectHotelSearchBarRegionCode(String text) throws Exception;

    List<Integer> selectSearchList(HotelSearchVo.HotelSearchListRequest hotelSearchListRequest) throws Exception;

    List<Integer> selectHotelReservationListForSearch(HotelDto.SelectHotelReservationListForSearch hotelReservationListForSearch) throws Exception;

    List<HotelSearchVo.TouristSpotInfo> selectTouristSpotList() throws Exception;

    List<HotelInfoVo.HotelDetailInfo> selectHotelByRegionCode(String kakaoRegion);
}
