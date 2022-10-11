package com.hotel.reservation.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.svc.HotelService;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import com.hotel.jwt.CheckTokenInfo;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.member.svc.MemberServiceImpl;
import com.hotel.member.vo.MemberVo;
import com.hotel.owner.vo.OwnerVo;
import com.hotel.reservation.svc.ReservationService;
import com.hotel.reservation.vo.MemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationInfoResponseDto;
import com.hotel.util.SHA512Util;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfoResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDeleteContentResponseDto;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

	private final CheckTokenInfo info;

	@Autowired
	ReservationService reservationService;

	@ApiOperation(value = "비회원 조회")
	@ResponseBody
	@PostMapping("/unMemberInfo")
	public Map<String, Object> UnMemberInfo(
			@RequestBody UnMemberInfoVo.UnMemberReservationRequest unMemberReservationRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		UnMemberReservationInfoResponseDto dto = new UnMemberReservationInfoResponseDto();

		Map<String, Object> map = new HashMap<>();
		List<UnMemberReservationInfoResponseDto> list = new ArrayList<>();
		System.out.println("parameter = " + unMemberReservationRequest.toString());
		if (unMemberReservationRequest.getReservation_name().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "name Not Found");
			return map;

		} else if (unMemberReservationRequest.getReservation_phone().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "phone Not Found");
			return map;

		} else if (unMemberReservationRequest.getReservation_num() == 0) {
			map.put("result", "ERR");
			map.put("reason", "reservation_num Not Found");
			return map;
		}

		return reservationService.UnMemberReservationInfo(unMemberReservationRequest);
	}

	@ApiOperation(value = "비회원 예약 삭제")
	@ResponseBody
	@DeleteMapping(value = "/unMemberwithdraw", produces = "application/json")
	public MemberReservationResponseDto UnMemberReservationWithdraw(
			@RequestBody UnMemberInfoVo.UnMemberWithdrawRequest memberWithdrawVo) {

		MemberReservationResponseDto dto = new MemberReservationResponseDto();
		if (memberWithdrawVo.getReservation_num() == 0) {
			dto.setResult("ERR");
			dto.setReason("reservation_num Not Found");
			return dto;
		} else if (memberWithdrawVo.getMember_num() == 0) {
			dto.setResult("ERR");
			dto.setReason("member_num Not Found");
			return dto;
		} else if (memberWithdrawVo.getPayment_detail_num() == 0) {
			dto.setResult("ERR");
			dto.setReason("payment_detail_num Not Found");
			return dto;
		} else if (memberWithdrawVo.getContent().equals("")) {
			dto.setResult("ERR");
			dto.setReason("content Not Found");
			return dto;
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
	public MemberReservationResponseDto MemberReservation(
			@RequestBody List<MemberInfoVo.MemberReservationRequest> memberReservationRequest, HttpServletRequest req)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		MemberReservationResponseDto dto = new MemberReservationResponseDto();

		System.out.println("data = " + memberReservationRequest.toString());

		int size = memberReservationRequest.size();

		if (size >= 0) {
			for (int i = 0; i < size; i++) {
				// 파라미터 밸리데이션 체크
				if (memberReservationRequest.get(i).getEd_date().equals("")) {
					dto.setResult("ERR");
					dto.setReason("end_date Not Found");
					return dto;
//				} else if (memberReservationRequest.getHotel_num().toString().equals("")){
//					dto.setResult("ERR");
//					dto.setReason("hotel_num Not Found");
//					return dto;
				} else if (memberReservationRequest.get(i).getReservation_name().equals("")) {
					dto.setResult("ERR");
					dto.setReason("reservation Name Not Found");
					return dto;
				} else if (memberReservationRequest.get(i).getReservation_people().toString().equals("")) {
					dto.setResult("ERR");
					dto.setReason("reservation_people Not Found");
					return dto;
				} else if (memberReservationRequest.get(i).getReservation_phone().equals("")) {
					dto.setResult("ERR");
					dto.setReason("reservation_phone Not Found");
					return dto;
				} else if (memberReservationRequest.get(i).getReservation_price().toString().equals("")) {
					dto.setResult("ERR");
					dto.setReason("reservation_price Not Found");
					return dto;
				} else if (memberReservationRequest.get(i).getRoom_detail_num().toString().equals("")) {
					dto.setResult("ERR");
					dto.setReason("room_detail_num Not Found");
					return dto;
				} else if (memberReservationRequest.get(i).getSt_date().equals("")) {
					dto.setResult("ERR");
					dto.setReason("start_date Not Found");
					return dto;
				} else if (memberReservationRequest.get(i).getRole().toString().equals("")) {
					dto.setResult("ERR");
					dto.setReason("start_date Not Found");
					return dto;
				}
			}
		}

		if (memberReservationRequest.get(0).getRole() == 1 || memberReservationRequest.get(0).getRole() == 2) {
			String token = req.getHeader("Authorization");
			String email = null;
			if (token.equals("")) {
				// 삭제 예정
				dto.setResult("ERR");
				dto.setReason("tokenNotFound");
			} else {
				email = info.tokenInfo(token);
			}
			email = reservationService.checkMemberInfo(email);
			if (email == null) {
				dto.setResult("ERR");
				dto.setReason("memberInfo fail");
				return dto;
			} else {
				int member_num = reservationService.selectMemberNum(email);
				if (member_num == 0) {
					dto.setResult("ERR");
					dto.setReason("member_num fail");
					return dto;
				}
				
				for(int x = 0; x < memberReservationRequest.size(); x++) {
					memberReservationRequest.get(x).setMember_num(member_num);
				}
			}
			dto = reservationService.memberReservation(memberReservationRequest);
		} else {
			dto = reservationService.memberReservation(memberReservationRequest);
		}

		return dto;
	}

	@ApiOperation(value = "고객 예약 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PutMapping(value = "/memberReservationwithdraw", produces = "application/json")
	public MemberReservationResponseDto MemberReservationWithdraw(
			@RequestBody MemberInfoVo.MemberWithdrawRequest memberWithdrawVo) {

		MemberReservationResponseDto dto = new MemberReservationResponseDto();
		Map<String, Object> map = new HashMap<>();
		if (memberWithdrawVo.getReservation_num() == 0) {
			dto.setResult("ERR");
			dto.setReason("reservation_num Not Found");
			return dto;
//		} else if (memberWithdrawVo.getRoom_detail_num() == 0) {
//			map.put("result", "ERR");
//			map.put("reason", "room_detail_num Not Found");
//			vo.setMap(map);
//			return vo;
		} else if (memberWithdrawVo.getContent().equals("")) {
			dto.setResult("ERR");
			dto.setReason("getContent Not Found");
			return dto;
		} else if (memberWithdrawVo.getMember_num() == 0) {
			dto.setResult("ERR");
			dto.setReason("member_num Not Found");
			return dto;
		} else if (memberWithdrawVo.getPayment_detail_num() == 0) {
			dto.setResult("ERR");
			dto.setReason("payment_detail_num Not Found");
			return dto;
		}

		return reservationService.MemberReservationWithdraw(memberWithdrawVo);
	}

	@ApiOperation(value = "고객 예약내역 조회")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "String", paramType = "header") })
	@ResponseBody
	@PostMapping(value = "/memberReservationList", produces = "application/json")
	public Map<String, Object> MemberReservationList(@RequestBody MemberInfoVo.MemberReservationListRequest memberInfo,
			HttpServletRequest req)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		Map<String, Object> map = new HashMap<>();

		List<MemberReservationListInfoResponseDto> list = new ArrayList<>();
		MemberReservationListInfoResponseDto dto = new MemberReservationListInfoResponseDto();

		System.out.println("data = " + memberInfo.toString());

		if (memberInfo.getReservation_status() == 0) {
			memberInfo.setReservation_status(1);

		} else if (memberInfo.getSt_date().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "st_dateNotFound");
			map.put("list", list);
			return map;
		} else if (memberInfo.getEd_date().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "ed_dateNotFound");
			map.put("list", list);
			return map;
		} else if (memberInfo.getPage().toString().equals("")) {
			// 수정해야 할 수 있음
			memberInfo.setPage(0);
		} else if (memberInfo.getPage_cnt().toString().equals("")) {
			memberInfo.setPage_cnt(10);
		}
		String token = req.getHeader("Authorization");
		String email;
		if (token == null) {
			map.put("result", "ERR");
			map.put("reason", "tokenNotFound");
			map.put("list", list);
			return map;
		} else {
			email = info.tokenInfo(token);
		}
		memberInfo.setEmail(email);
		
		System.out.println("token email = " + email);
		map = reservationService.MemberReservationList(memberInfo);

		return map;
	}

	@ApiOperation(value = "고객 예약취소 사유 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, paramType = "header", example = "0") })
	@ResponseBody
	@PostMapping(value = "/memberDeleteContent", produces = "application/json")
	public ReservationDeleteContentResponseDto MemberReservationDeleteContent(
			@RequestBody MemberInfoVo.MemberReservationDeleteRequest memberInfoRequest, HttpServletRequest req) {
		ReservationDeleteContentResponseDto dto = new ReservationDeleteContentResponseDto();

		String token = req.getHeader("Authorization");
		String email;
		if (token == null) {

			dto.setResult("ERR");
			dto.setReason("token member info fail");
			return dto;
		} else {
			email = info.tokenInfo(token);
		}
		if (memberInfoRequest.getReservation_num().toString().equals("")) {
			dto.setResult("ERR");
			dto.setReason("reservation_num Not Found");
			dto.setContent("");
			return dto;
		} else if (email.equals("")) {
			dto.setResult("ERR");
			dto.setReason("token find email fail");
			dto.setContent("");
			return dto;
		}

		return reservationService.MemberReservationDeleteContent(memberInfoRequest);
	}

}
