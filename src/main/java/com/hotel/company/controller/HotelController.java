package com.hotel.company.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.svc.HotelService;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")

public class HotelController {

    @Autowired
    HotelService hotelService;

    @ApiOperation(value="호텔 등록")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseVo RegisterHotel(@ModelAttribute HotelInfoVo.RegisterHotelRequest registerHotelRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.RegisterHotel(registerHotelRequest, jwtToken);
    }

    @ApiOperation(value="호텔 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping("/delete")
    public CommonResponseVo DeleteHotel(@RequestBody HotelInfoVo.DeleteHotelRequest deleteHotelRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.DeleteHotel(deleteHotelRequest, jwtToken);
    }

    @ApiOperation(value="호텔 정보 수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseVo EditHotel(@ModelAttribute HotelInfoVo.EditInfoHotelRequest editInfoHotelRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.EditHotel(editInfoHotelRequest, jwtToken);
    }

    @ApiOperation(value="사업자가 소유한 호텔 리스트")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/owner-hotel-list")
    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList(@RequestBody HotelInfoVo.OwnerHotelListRequest ownerHotelListRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.OwnerHotelList(ownerHotelListRequest, jwtToken);
    }

    @ApiOperation(value="사업자가 소유한 호텔명 리스트 - 사업자가 소유한 호텔의 이름, 번호만 있는 리스트")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/owner-hotel-name-list")
    public HotelInfoVo.OwnerHotelNameListResponse OwnerHotelNameList(@RequestHeader(value="Authorization") String jwtToken){
        return hotelService.OwnerHotelNameList(jwtToken);
    }

    @ApiOperation(value="여행지 정보 - 여행지 목록과 해당 여행지의 호텔 갯수")
    @ResponseBody
    @GetMapping(value = "/tourist")
    public HotelSearchVo.TouristSpotInfoResponse TouristSpotInfo(HotelSearchVo.TouristSpotInfoRequest touristSpotInfoRequest){
        return hotelService.TouristSpotInfo(touristSpotInfoRequest);
    }

    @ApiOperation(value="검색바 - 검색어에 일치하는 지역명, 호텔명, 호텔주소, 장소 있는지 조회")
    @ResponseBody
    @GetMapping(value = "/search/bar")
    public HotelSearchVo.SearchBarResponse SearchBar(HotelSearchVo.SearchBarRequest searchBarVoSearchBarRequest){
        return hotelService.SearchBar(searchBarVoSearchBarRequest);
    }

    @ApiOperation(value="검색하기 - 사용자가 요청한 검색어, 조건에 일치하는 호텔조회")
    @ResponseBody
    @GetMapping(value = "/search/list")
    public HotelSearchVo.SearchListResponse SearchHotelList(HotelSearchVo.HotelSearchListRequest hotelSearchListRequest){
        return hotelService.SearchHotelList(hotelSearchListRequest);
    }

    @ApiOperation(value="검색한 호텔 리스트 필터 - 검색된 호텔을 가격범위, 호텔등급등 조건으로 필터")
    @ResponseBody
    @PostMapping(value = "/search/list/filter")
    public HotelSearchVo.SearchListResponse SearchHotelListFilter(@RequestBody HotelSearchVo.HotelSearchListFilterRequest hotelSearchListFilterRequest){
        return hotelService.SearchHotelListFilter(hotelSearchListFilterRequest);
    }

    @ApiOperation(value="호텔상세정보")
    @ResponseBody
    @PostMapping("/info")
    public HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo(@RequestBody HotelInfoVo.HotelDetailInfoRequest hotelDetailInfoRequest){
        int hotel_num = hotelDetailInfoRequest.getHotel_num();
        return hotelService.HotelDetailInfo(hotel_num);
    }

    @ApiOperation(value="객실상세정보")
    @ResponseBody
    @PostMapping("/room/info")
    public HotelInfoVo.RoomInfoResponse RoomInfo(@RequestBody HotelInfoVo.RoomInfoRequest roomInfoRequest){
        int room_num = roomInfoRequest.getRoom_num();
        return hotelService.RoomInfo(room_num);
    }

    @ApiOperation(value="중복 객실명 조회 - 중복이면 true 아니면 false")
    @ResponseBody
    @PostMapping("/room/check-duplicate-name")
    public HotelInfoVo.CheckDuplicateRoomNameResponse CheckDuplicateRoomName(@RequestBody HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest){
        return hotelService.CheckDuplicateRoomName(checkDuplicateRoomNameRequest);
    }

    @ApiOperation(value="객실등록")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/room/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseVo RegisterRoom(@ModelAttribute HotelInfoVo.RegisterRoomRequest registerRoomRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.RegisterRoom(registerRoomRequest, jwtToken);
    }

    @ApiOperation(value="객실수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping(value = "/room/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseVo EditRoom(@ModelAttribute HotelInfoVo.EditRoomRequest registerRoomRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.EditRoom(registerRoomRequest, jwtToken);
    }

    @ApiOperation(value="객실삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping("/room/delete")
    public CommonResponseVo DeleteRoom(@RequestBody HotelInfoVo.DeleteRoomRequest deleteRoomRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.DeleteRoom(deleteRoomRequest, jwtToken);
    }

    @ApiOperation(value="객실삭제시 모달창 정보 - 해당 객실정보 + 최종예약날짜 정보 제공")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/room/delete/info")
    public HotelInfoVo.DeleteRoomInfoResponse DeleteRoomInfo(@RequestBody HotelInfoVo.DeleteRoomRequest deleteRoomRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.DeleteRoomInfo(deleteRoomRequest, jwtToken);
    }

    @ApiOperation(value="객실상세정보 리스트")
    @ResponseBody
    @PostMapping("/room/info/list")
    public HotelInfoVo.RoomInfoListResponse RoomInfoList(@RequestBody HotelInfoVo.RoomInfoListRequest roomInfoRequest){
        return hotelService.RoomInfoList(roomInfoRequest);
    }

    @ApiOperation(value="호실 추가")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/room/detail/add")
    public CommonResponseVo AddRoomDetail(@RequestBody HotelInfoVo.RegisterRoomDetailRequest registerRoomDetailRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.AddRoomDetail(registerRoomDetailRequest, jwtToken);
    }

    @ApiOperation(value="호실 이용불가 설정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/room/detail/disable-setting")
    public CommonResponseVo DisableSettingRoomDetail(@RequestBody HotelInfoVo.DisableSettingRoomDetailRequest disableSettingRoomDetailRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.DisableSettingRoomDetail(disableSettingRoomDetailRequest, jwtToken);
    }

    @ApiOperation(value="호실 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping("/room/detail/delete")
    public CommonResponseVo DeleteRoomDetail(@RequestBody HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.DeleteRoomDetail(deleteRoomDetailRequest, jwtToken);
    }

    @ApiOperation(value="호실삭제시 모달창 정보 - 해당 호실정보 + 최종예약날짜 정보 제공")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/room/detail/delete/info")
    public HotelInfoVo.DeleteRoomDetailInfoResponse DeleteRoomDetailInfo(@RequestBody HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.DeleteRoomDetailInfo(deleteRoomDetailRequest, jwtToken);
    }

    @ApiOperation(value="호텔 예약정보 조회 - 해당호텔의 예약내역 리스트 조회. 핸드폰, 고객명등의 검색조건 없으면 해당 호텔의 전체 예약내역 조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/reservation-list")
    public HotelInfoVo.HotelReservationListResponse HotelReservationList(@RequestBody HotelInfoVo.HotelReservationListRequest hotelReservationListRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.HotelReservationList(hotelReservationListRequest, jwtToken);
    }

    @ApiOperation(value="사업자가 소유한 모든 호텔 예약정보 조회 - 사업자 예약관리페이지 처음 진입시 필요")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/owner-reservation-list")
    public HotelInfoVo.HotelReservationListResponse OwnerReservationList(@RequestBody HotelInfoVo.OwnerReservationListRequest ownerReservationListRequest, @RequestHeader(value="Authorization") String jwtToken){
        return hotelService.OwnerReservationList(ownerReservationListRequest, jwtToken);
    }

    @ApiOperation(value="예약희망 범위에 해당 호텔에 예약가능한 객실정보만 제공 ")
    @ResponseBody
    @PostMapping("/info/available-list")
    public HotelInfoVo.HotelDetailInfoResponse ReservationAvailableList(@RequestBody HotelInfoVo.ReservationAvailableListRequest reservationAvailableListRequest){
        return hotelService.ReservationAvailableList(reservationAvailableListRequest);
    }

}
