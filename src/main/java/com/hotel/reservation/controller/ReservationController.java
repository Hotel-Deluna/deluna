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

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")

public class ReservationController {

	@Autowired
	ReservationService reservationService;

	@ApiOperation(value = "비회원 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/unMemberInfo")
	public Map<String, Object> UnMemberInfo(
			@RequestBody UnMemberInfoVo.UnMemberReservationRequest unMemberReservationRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		Map<String, Object> result = new HashMap<>();

		if (unMemberReservationRequest.getReservation_name().equals("")) {
			result.put("result", "ERR");
			result.put("reason", "name Not Found");
			result.put("data", "");
			return result;
		} else if (unMemberReservationRequest.getReservation_phone().equals("")) {
			result.put("result", "ERR");
			result.put("reason", "phone Not Found");
			result.put("data", "");
			return result;
		} else if (unMemberReservationRequest.getReservation_num() == 0) {
			result.put("result", "ERR");
			result.put("reason", "reservation_num Not Found");
			result.put("data", "");
			return result;
		}

		return reservationService.UnMemberReservationInfo(unMemberReservationRequest);
	}

	@ApiOperation(value = "비회원 예약 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@DeleteMapping(value = "/unMemberwithdraw", produces = "application/json")
	public CommonResponseVo UnMemberReservationWithdraw(
			@RequestBody UnMemberInfoVo.UnMemberWithdrawRequest memberWithdrawVo) {

		CommonResponseVo result = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();
		// 예약취소 인터페이스 호출 시 사유를 번저 저장하고 최종 취소를 해야하는거 ?
		// db를 두 번 호출하는건데 괜찮은건가?
		if (memberWithdrawVo.getReservation_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "reservation_num Not Found");
			result.setMap(map);
			return result;
		} else if (memberWithdrawVo.getMember_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "member_num Not Found");
			result.setMap(map);
			return result;
		} else if (memberWithdrawVo.getPayment_detail_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "payment_detail_num Not Found");
			result.setMap(map);
			return result;
		} else if (memberWithdrawVo.getContent().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "content Not Found");
			result.setMap(map);
			return result;
		}

		return reservationService.UnMemberReservationWithdraw(memberWithdrawVo);
	}

//	@ApiOperation(value = "결제하기(필요없을수도)")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PostMapping("/payments")
//	public CommonResponseVo Payments(@RequestBody MemberInfoVo.MemberReservationInfo memberReservationInfo) {
//		return reservationService.Payments(memberReservationInfo);
//	}

	@ApiOperation(value = "고객 객실 예약하기")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/memberReservation")
	public CommonResponseVo MemberReservation(
			@RequestBody MemberInfoVo.MemberReservationRequest memberReservationRequest) {

		CommonResponseVo vo = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();

		// 파라미터 밸리데이션 체크
		if (memberReservationRequest.getEd_date().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "end Date Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getMember_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "member_num Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getName().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "name Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getReservation_name().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "reservation Name Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getReservation_people().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "reservation People Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getReservation_phone().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "reservation Phone Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getReservation_price() == 0) {
			map.put("result", "ERR");
			map.put("reason", "reservation Price Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getRoom_detail_name().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "room Detail Name Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getRoom_detail_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "room Detail Num Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberReservationRequest.getSt_date().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "start Date Not Found");
			vo.setMap(map);
			return vo;
		}
		return reservationService.memberReservation(memberReservationRequest);
	}

	@ApiOperation(value = "고객 예약 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PutMapping(value = "/memberReservationwithdraw", produces = "application/json")
	public CommonResponseVo MemberReservationWithdraw(
			@RequestBody MemberInfoVo.MemberWithdrawRequest memberWithdrawVo) {

		CommonResponseVo vo = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();
		if (memberWithdrawVo.getReservation_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "reservation_num Not Found");
			vo.setMap(map);
			return vo;
//		} else if (memberWithdrawVo.getRoom_detail_num() == 0) {
//			map.put("result", "ERR");
//			map.put("reason", "room_detail_num Not Found");
//			vo.setMap(map);
//			return vo;
		} else if (memberWithdrawVo.getContent().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "getContent Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberWithdrawVo.getMember_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "member_num Not Found");
			vo.setMap(map);
			return vo;
		} else if (memberWithdrawVo.getPayment_detail_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "payment_detail_num Not Found");
			vo.setMap(map);
			return vo;
		}

		return reservationService.MemberReservationWithdraw(memberWithdrawVo);
	}

	@ApiOperation(value = "고객 예약내역 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping(value = "/memberReservationList", produces = "application/json")
	public Map<String, Object> MemberReservationList(@RequestBody MemberInfoVo.MemberReservationListRequest memberInfo)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		Map<String, Object> result = new HashMap<>();

		if (memberInfo.getMember_num() == 0) {
			result.put("result", "ERR");
			result.put("reason", "member_num Not Found");
			return result;
		} else if (memberInfo.getReservation_status() == 0) {
			result.put("result", "ERR");
			result.put("reason", "reservation_status Not Found");
			return result;
		} else if (memberInfo.getSt_date().equals("")) {
			result.put("result", "ERR");
			result.put("reason", "st_date Not Found");
			return result;
		}

		result = reservationService.MemberReservationList(memberInfo);

		return result;
	}

	@ApiOperation(value = "고객 예약취소 사유 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping(value = "/memberDeleteContent", produces = "application/json")
	public Map<String, Object> MemberReservationDeleteContent(
			@RequestBody MemberInfoVo.MemberReservationDeleteRequest memberInfoRequest) {
		Map<String, Object> result = new HashMap<>();

		if (memberInfoRequest.getMember_num() == 0) {
			result.put("result", "ERR");
			result.put("reason", "member_num Not Found");
			return result;
		} else if (memberInfoRequest.getReservation_num() == 0) {
			result.put("result", "ERR");
			result.put("reason", "reservation_num Not Found");
			return result;
		}

		return reservationService.MemberReservationDeleteContent(memberInfoRequest);
	}

}
