package com.hotel.reservation.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.svc.HotelService;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import com.hotel.owner.vo.OwnerVo;
import com.hotel.reservation.svc.ReservationService;
import com.hotel.reservation.vo.MemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfo;

import io.swagger.annotations.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")

public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @ApiOperation(value="비회원 조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/unMemberInfo")
    public UnMemberInfoVo.UnMemberResponse UnMemberInfo(@RequestBody UnMemberInfoVo.UnMemberReservationRequest unMemberReservationRequest){
        return reservationService.UnMemberReservationInfo(unMemberReservationRequest);
    }

    @ApiOperation(value="비회원 예약 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping(value = "/unMemberwithdraw", produces = "application/json")
    public CommonResponseVo UnMemberReservationWithdraw(@RequestBody UnMemberInfoVo.UnMemberWithdrawRequest unMemberWithdrawVo){
        return reservationService.UnMemberReservationWithdraw(unMemberWithdrawVo);
    }
    
    @ApiOperation(value="결제하기(필요없을수도)")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/payments")
    public CommonResponseVo Payments(@RequestBody MemberInfoVo.MemberReservationInfo memberReservationInfo){
        return reservationService.Payments(memberReservationInfo);
    }
    
    @ApiOperation(value="고객 객실 예약하기")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/memberReservation")
    public CommonResponseVo MemberReservation(@RequestBody MemberInfoVo.MemberReservationRequest memberReservationRequest){
        return reservationService.memberReservation(memberReservationRequest);
    }
    
    @ApiOperation(value="고객 예약 삭제")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping(value = "/memberwithdraw", produces = "application/json")
    public CommonResponseVo MemberReservationWithdraw(@RequestBody MemberInfoVo.MemberWithdrawRequest	memberWithdrawVo){
        return reservationService.MemberReservationWithdraw(memberWithdrawVo);
    }
    
    @ApiOperation(value="고객 예약내역 조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/memberReservationList", produces = "application/json")
    public Map<String, Object> MemberReservationList(@RequestBody MemberInfoVo.MemberInfoRequest memberInfo){
        return reservationService.MemberReservationList(memberInfo);
    }
    
    @ApiOperation(value="고객 예약취소 사유 조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping(value = "/memberReservationDeleteContent", produces = "application/json")
    public Map<String, Object> MemberReservationDeleteContent(@RequestBody MemberInfoVo.MemberReservationDeleteRequest memberInfoRequest){
        return reservationService.MemberReservationDeleteContent(memberInfoRequest);
    }

}
