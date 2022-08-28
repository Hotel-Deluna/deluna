package com.hotel.company.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.svc.HotelService;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import io.micrometer.core.lang.Nullable;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")

public class HotelController {

    @Autowired
    HotelService hotelService;

    @ApiOperation(value="호텔 등록")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/register")
    public CommonResponseVo RegisterHotel(@RequestBody HotelInfoVo.RegisterHotelRequest registerHotelRequest){
        return hotelService.RegisterHotel(registerHotelRequest);
    }

    @ApiOperation(value="호텔 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping("/delete")
    public CommonResponseVo DeleteHotel(@RequestBody HotelInfoVo.DeleteHotelRequest deleteHotelRequest){
        return hotelService.DeleteHotel(deleteHotelRequest);
    }

    @ApiOperation(value="호텔 정보 수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/edit")
    public CommonResponseVo EditHotel(@RequestBody HotelInfoVo.RegisterHotelRequest editHotelRequest){
        return hotelService.EditHotel(editHotelRequest);
    }

    @ApiOperation(value="사업자가 소유한 호텔 리스트")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/owner-hotel-list")
    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList(@Nullable HotelInfoVo.OwnerHotelListRequest ownerHotelListRequest){
        return hotelService.OwnerHotelList(ownerHotelListRequest);
    }

    @ApiOperation(value="여행지 정보 - 여행지 목록과 해당 여행지의 호텔 갯수")
    @ResponseBody
    @GetMapping(value = "/tourist")
    public HotelSearchVo.SearchBarResponse TouristSpotInfo(){
        return hotelService.TouristSpotInfo();
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
    public HotelSearchVo.SearchListResponse SearchHotelListFilter(HotelSearchVo.HotelSearchListFilterRequest hotelSearchListFilterRequest){
        return hotelService.SearchHotelListFilter(hotelSearchListFilterRequest);
    }

    @ApiOperation(value="호텔상세정보")
    @ResponseBody
    @PostMapping("/info")
    public HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo(@RequestBody HotelInfoVo.HotelDetailInfoRequest hotelDetailInfoRequest){
        return hotelService.HotelDetailInfo(hotelDetailInfoRequest);
    }

    @ApiOperation(value="객실상세정보")
    @ResponseBody
    @PostMapping("/room/info")
    public HotelInfoVo.RoomInfoResponse RoomInfo(@RequestBody HotelInfoVo.RoomInfoRequest roomInfoRequest){
        return hotelService.RoomInfo(roomInfoRequest);
    }

    @ApiOperation(value="중복 객실명 조회")
    @ResponseBody
    @PostMapping("/room/check-duplicate-name")
    public HotelInfoVo.CheckDuplicateRoomNameResponse CheckDuplicateRoomName(@RequestBody HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest){
        return hotelService.CheckDuplicateRoomName(checkDuplicateRoomNameRequest);
    }

    @ApiOperation(value="객실등록")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/room/register")
    public CommonResponseVo RegisterRoom(@RequestBody HotelInfoVo.RegisterRoomRequest registerRoomRequest){
        return hotelService.RegisterRoom(registerRoomRequest);
    }

    @ApiOperation(value="객실수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/room/edit")
    public CommonResponseVo EditRoom(@RequestBody HotelInfoVo.EditRoomRequest registerRoomRequest){
        return hotelService.EditRoom(registerRoomRequest);
    }

    @ApiOperation(value="객실삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping("/room/delete")
    public CommonResponseVo DeleteRoom(@RequestBody HotelInfoVo.DeleteRoomRequest deleteRoomRequest){
        return hotelService.DeleteRoom(deleteRoomRequest);
    }

    @ApiOperation(value="객실삭제시 모달창 정보 - 해당 객실정보 + 최종예약날짜 정보 제공")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/room/delete/info")
    public HotelInfoVo.DeleteRoomInfoResponse DeleteRoomInfo(@RequestBody HotelInfoVo.DeleteRoomRequest deleteRoomRequest){
        return hotelService.DeleteRoomInfo(deleteRoomRequest);
    }

    @ApiOperation(value="객실상세정보 리스트")
    @ResponseBody
    @PostMapping("/room/info/list")
    public HotelInfoVo.RoomInfoListResponse RoomInfoList(@RequestBody HotelInfoVo.RoomInfoRequest roomInfoRequest){
        return hotelService.RoomInfoList(roomInfoRequest);
    }

    @ApiOperation(value="호실 추가")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/room/detail/add")
    public CommonResponseVo AddRoomDetail(@RequestBody HotelInfoVo.AddRoomDetailRequest addRoomDetailRequest){
        return hotelService.AddRoomDetail(addRoomDetailRequest);
    }

    @ApiOperation(value="호실 정보 수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/room/detail/edit")
    public CommonResponseVo EditRoomDetail(@RequestBody HotelInfoVo.EditRoomDetailRequest editRoomDetailRequest){
        return hotelService.EditRoomDetail(editRoomDetailRequest);
    }

    @ApiOperation(value="호실 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping("/room/detail/delete")
    public CommonResponseVo DeleteRoomDetail(@RequestBody HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest){
        return hotelService.DeleteRoomDetail(deleteRoomDetailRequest);
    }

    @ApiOperation(value="호실삭제시 모달창 정보 - 해당 호실정보 + 최종예약날짜 정보 제공")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/room/detail/delete/info")
    public HotelInfoVo.DeleteRoomDetailInfoResponse DeleteRoomDetailInfo(@RequestBody HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest){
        return hotelService.DeleteRoomDetailInfo(deleteRoomDetailRequest);
    }

    @ApiOperation(value="호텔 예약정보 조회 - 해당호텔의 예약내역 리스트 조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/reservation-list")
    public HotelInfoVo.HotelReservationListResponse HotelReservationList(@RequestBody HotelInfoVo.HotelReservationListRequest deleteRoomDetailRequest){
        return hotelService.HotelReservationList(deleteRoomDetailRequest);
    }

}
