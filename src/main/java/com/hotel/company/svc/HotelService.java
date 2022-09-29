package com.hotel.company.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import io.micrometer.core.lang.Nullable;

public interface HotelService {

    HotelInfoVo.OwnerHotelListResponse OwnerHotelList(@Nullable HotelInfoVo.OwnerHotelListRequest ownerHotelListRequest, String jwtToken);

    CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelParamVo, String jwtToken);

    HotelSearchVo.TouristSpotInfoResponse TouristSpotInfo(HotelSearchVo.TouristSpotInfoRequest touristSpotInfoRequest);

    HotelSearchVo.SearchBarResponse SearchBar(HotelSearchVo.SearchBarRequest searchBarVoSearchBarRequest);

    HotelSearchVo.SearchListResponse SearchHotelList(HotelSearchVo.HotelSearchListRequest searchBarVoSearchBarRequest);

    HotelSearchVo.SearchListResponse SearchHotelListFilter(HotelSearchVo.HotelSearchListFilterRequest hotelSearchListFilterRequest);

    HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo(int hotelDetailInfoRequest);

    CommonResponseVo DeleteHotel(HotelInfoVo.DeleteHotelRequest deleteHotelRequest, String jwtToken);

    CommonResponseVo EditHotel(HotelInfoVo.EditInfoHotelRequest editHotelRequest, String jwtToken);

    CommonResponseVo RegisterRoom(HotelInfoVo.RegisterRoomRequest registerRoomRequest, String jwtToken);

    HotelInfoVo.RoomInfoResponse RoomInfo(int roomInfoRequest);

    CommonResponseVo EditRoom(HotelInfoVo.EditRoomRequest registerRoomRequest, String jwtToken);

    HotelInfoVo.CheckDuplicateRoomNameResponse CheckDuplicateRoomName(HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest);

    CommonResponseVo DeleteRoom(HotelInfoVo.DeleteRoomRequest deleteRoomRequest, String jwtToken);

    HotelInfoVo.DeleteRoomInfoResponse DeleteRoomInfo(HotelInfoVo.DeleteRoomRequest deleteRoomRequest, String jwtToken);

    HotelInfoVo.RoomInfoListResponse RoomInfoList(HotelInfoVo.RoomInfoListRequest roomInfoRequest);

    CommonResponseVo AddRoomDetail(HotelInfoVo.RegisterRoomDetailRequest registerRoomDetailRequest, String jwtToken);

    CommonResponseVo DisableSettingRoomDetail(HotelInfoVo.DisableSettingRoomDetailRequest disableSettingRoomDetailRequest, String jwtToken);

    CommonResponseVo DeleteRoomDetail(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest, String jwtToken);

    HotelInfoVo.DeleteRoomDetailInfoResponse DeleteRoomDetailInfo(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest, String jwtToken);

    HotelInfoVo.HotelReservationListResponse HotelReservationList(HotelInfoVo.HotelReservationListRequest hotelReservationListRequest, String jwtToken);

    HotelInfoVo.HotelReservationListResponse OwnerReservationList(HotelInfoVo.OwnerReservationListRequest ownerReservationListRequest, String jwtToken);
}
