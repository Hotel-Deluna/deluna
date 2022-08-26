package com.hotel.company.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.svc.HotelService;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
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
    @PostMapping("/register")
    public CommonResponseVo RegisterHotel(@RequestBody HotelInfoVo.RegisterHotelRequest registerHotelRequest){
        return hotelService.RegisterHotel(registerHotelRequest);
    }

    @ApiOperation(value="사업자 호텔 리스트")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/owner-hotel-list")
    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList(){
        return hotelService.OwnerHotelList();
    }

    @ApiOperation(value="메인페이지 검색바 - 검색어에 일치하는 지역명, 호텔명, 호텔주소 있는지 조회")
    @ResponseBody
    @GetMapping(value = "/search-bar")
    public HotelSearchVo.Response SearchBar(HotelSearchVo.SearchBarRequest searchBarVoSearchBarRequest){
        return hotelService.SearchBar(searchBarVoSearchBarRequest);
    }

}
