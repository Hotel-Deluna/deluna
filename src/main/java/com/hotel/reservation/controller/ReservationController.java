package com.hotel.reservation.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.svc.HotelService;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import com.hotel.jwt.CheckTokenInfo;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.member.svc.MemberServiceImpl;
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
	public List<UnMemberReservationInfoResponseDto> UnMemberInfo(
			@RequestBody UnMemberInfoVo.UnMemberReservationRequest unMemberReservationRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		UnMemberReservationInfoResponseDto dto = new UnMemberReservationInfoResponseDto();
		
		List<UnMemberReservationInfoResponseDto> list = new ArrayList<>();
		
		if (unMemberReservationRequest.getReservation_name().equals("")) {
			dto.setResult("ERR");
			dto.setReason("name Not Found");
			list.add(dto);
			return list;
		} else if (unMemberReservationRequest.getReservation_phone().equals("")) {
			dto.setResult("ERR");
			dto.setReason("phone Not Found");
			list.add(dto);
			return list;
		} else if (unMemberReservationRequest.getReservation_num() == 0) {
			dto.setResult("ERR");
			dto.setReason("reservation_num Not Found");
			list.add(dto);
			return list;
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
			@RequestBody MemberInfoVo.MemberReservationRequest memberReservationRequest, HttpServletRequest req) {

		MemberReservationResponseDto dto = new MemberReservationResponseDto();

		// 파라미터 밸리데이션 체크
		if (memberReservationRequest.getEd_date().equals("")) {
			dto.setResult("ERR");
			dto.setReason("end_date Not Found");
			return dto;
		} else if (memberReservationRequest.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("member_num Not Found");
			return dto;
		} else if (memberReservationRequest.getName().equals("")) {
			dto.setResult("ERR");
			dto.setReason("name Not Found");
			return dto;
		} else if (memberReservationRequest.getReservation_name().equals("")) {
			dto.setResult("ERR");
			dto.setReason("reservation Name Not Found");
			return dto;
		} else if (memberReservationRequest.getReservation_people().toString().equals("")){
			dto.setResult("ERR");
			dto.setReason("reservation_people Not Found");
			return dto;
		} else if (memberReservationRequest.getReservation_phone().equals("")) {
			dto.setResult("ERR");
			dto.setReason("reservation_phone Not Found");
			// 없을 경우 에러코드 채워서 보내기 -> 
			dto.setReservation_phone_yn("N");
			return dto;
		} else if (memberReservationRequest.getReservation_price().toString().equals("")) {
			dto.setResult("ERR");
			dto.setReason("reservation_price Not Found");
			return dto;
		} else if (memberReservationRequest.getRoom_detail_name().equals("")) {
			dto.setResult("ERR");
			dto.setReason("room_detail_name Not Found");
			return dto;
		} else if (memberReservationRequest.getRoom_detail_num().toString().equals("")) {
			dto.setResult("ERR");
			dto.setReason("room_detail_num Not Found");
			return dto;
		} else if (memberReservationRequest.getSt_date().equals("")) {
			dto.setResult("ERR");
			dto.setReason("start_date Not Found");
			return dto;
		}else if (memberReservationRequest.getRole().toString().equals("")){
			dto.setResult("ERR");
			dto.setReason("start_date Not Found");
			return dto;
		}else if (memberReservationRequest.getRole() == 1 || memberReservationRequest.getRole() == 2) {
			String token = req.getHeader("accessToken");
			String email ;
			if(token == null) {
				// 삭제 예정
				email = "sms44556688@gmail.com";
			}else {
				email = info.tokenInfo(token);
			}
			email = reservationService.checkMemberInfo(email);
			if(email == null) {
				dto.setResult("ERR");
				dto.setReason("memberInfo fail");
				return dto;
			}else {
				int member_num = reservationService.selectMemberNum(email);
				if(member_num == 0) {
					dto.setResult("ERR");
					dto.setReason("member_num fail");
					return dto;
				}
				memberReservationRequest.setMember_num(member_num);		
			}
			if(email.equals(memberReservationRequest.getEmail())) {
			dto = reservationService.memberReservation(memberReservationRequest);
			}else {
				dto.setResult("ERR");
				dto.setReason("param email and db email not match");
			}
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
	public List<MemberReservationListInfoResponseDto> MemberReservationList(@RequestBody MemberInfoVo.MemberReservationListRequest memberInfo,
			HttpServletRequest req) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		List<MemberReservationListInfoResponseDto> list = new ArrayList<>();
		MemberReservationListInfoResponseDto dto = new MemberReservationListInfoResponseDto();
		
		System.out.println("data = " + memberInfo.toString()) ;
		 
	    if (memberInfo.getReservation_status() == 0) {
	    	memberInfo.setReservation_status(1);		
	    	
	    } else if (memberInfo.getSt_date().equals("")) {
			dto.setResult("ERR");
			dto.setReason("st_date Not Found");			
			list.add(dto);
			return list;
		} else if (memberInfo.getEd_date().equals("")) {
			dto.setResult("ERR");
			dto.setReason("ed_date Not Found");			
			list.add(dto);
			return list;
		} else if (memberInfo.getPage().toString().equals("")){
			// 수정해야 할 수 있음
			memberInfo.setPage(0);		
		}else if (memberInfo.getPage_cnt().toString().equals("")){
			memberInfo.setPage_cnt(10);		
		}
	    
		String token = req.getHeader("accessToken");
		String email ;
		if(token == null) {
			// 삭제 예정
		//	email = "sms44556688@gmail.com";
			dto.setResult("ERR");
			dto.setReason("token decryption fail");
			list.add(dto);
			return list;
		}else {
			email = info.tokenInfo(token);
		}
		memberInfo.setEmail(email);
		list = reservationService.MemberReservationList(memberInfo);

		return list;
	}

	@ApiOperation(value = "고객 예약취소 사유 조회")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, paramType = "header", example = "0")})
	@ResponseBody
	@PostMapping(value = "/memberDeleteContent", produces = "application/json")
	public ReservationDeleteContentResponseDto MemberReservationDeleteContent(
			@RequestBody MemberInfoVo.MemberReservationDeleteRequest memberInfoRequest, HttpServletRequest req) {
		ReservationDeleteContentResponseDto dto = new ReservationDeleteContentResponseDto();
		
		String token = req.getHeader("accessToken");
		String email;
		if(token == null) {
			
			dto.setResult("ERR");
			dto.setReason("token member info fail");
			return dto;
		}else {
			email = info.tokenInfo(token);
		}
		if (memberInfoRequest.getReservation_num().toString().equals("")){
			dto.setResult("ERR");
			dto.setReason("reservation_num Not Found");
			dto.setContent("");
			return dto;
		}else if(email.equals("")) {
			dto.setResult("ERR");
			dto.setReason("token find email fail");
			dto.setContent("");
			return dto;
		}
		
		return reservationService.MemberReservationDeleteContent(memberInfoRequest);
	}

}
