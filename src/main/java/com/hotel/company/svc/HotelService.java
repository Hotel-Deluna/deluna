package com.hotel.company.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import io.micrometer.core.lang.Nullable;

public interface HotelService {

    HotelInfoVo.OwnerHotelListResponse OwnerHotelList(@Nullable HotelInfoVo.OwnerHotelListRequest ownerHotelListRequest);

    CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelParamVo);

    HotelSearchVo.SearchBarResponse TouristSpotInfo();

    HotelSearchVo.SearchBarResponse SearchBar(HotelSearchVo.SearchBarRequest searchBarVoSearchBarRequest);

    HotelSearchVo.SearchListResponse SearchHotelList(HotelSearchVo.HotelSearchListRequest searchBarVoSearchBarRequest);

    HotelSearchVo.SearchListResponse SearchHotelListFilter(HotelSearchVo.HotelSearchListFilterRequest hotelSearchListFilterRequest);

    HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo(HotelInfoVo.HotelDetailInfoRequest hotelDetailInfoRequest);

    CommonResponseVo DeleteHotel(HotelInfoVo.DeleteHotelRequest deleteHotelRequest);

    CommonResponseVo EditHotel(HotelInfoVo.RegisterHotelRequest editHotelRequest);

    CommonResponseVo RegisterRoom(HotelInfoVo.RegisterRoomRequest registerRoomRequest);

    HotelInfoVo.RoomInfoResponse RoomInfo(HotelInfoVo.RoomInfoRequest roomInfoRequest);

    CommonResponseVo EditRoom(HotelInfoVo.EditRoomRequest registerRoomRequest);

    HotelInfoVo.CheckDuplicateRoomNameResponse CheckDuplicateRoomName(HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest);

    CommonResponseVo DeleteRoom(HotelInfoVo.DeleteRoomRequest deleteRoomRequest);

    HotelInfoVo.DeleteRoomInfoResponse DeleteRoomInfo(HotelInfoVo.DeleteRoomRequest deleteRoomRequest);

    HotelInfoVo.RoomInfoListResponse RoomInfoList(HotelInfoVo.RoomInfoRequest roomInfoRequest);

    CommonResponseVo AddRoomDetail(HotelInfoVo.AddRoomDetailRequest addRoomDetailRequest);

    CommonResponseVo EditRoomDetail(HotelInfoVo.EditRoomDetailRequest editRoomDetailRequest);

    CommonResponseVo DeleteRoomDetail(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest);

    HotelInfoVo.DeleteRoomDetailInfoResponse DeleteRoomDetailInfo(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest);

    HotelInfoVo.HotelReservationListResponse HotelReservationList(HotelInfoVo.HotelReservationListRequest deleteRoomDetailRequest);
}
