package com.hotel.reservation.svc;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotel.common.CommonResponseVo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDeleteContentResponseDto;
import com.hotel.reservation.vo.UnMemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberWithdrawRequest;

@Service
public interface ReservationService {

	Map<String, Object> UnMemberReservationInfo(UnMemberInfoVo.UnMemberReservationRequest unMemberReservationInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException;
	
	MemberReservationResponseDto UnMemberReservationWithdraw(UnMemberInfoVo.UnMemberWithdrawRequest unMemberWithdrawVo);

	CommonResponseVo Payments(MemberReservationInfo memberReservationInfo);

	MemberReservationResponseDto memberReservation(MemberReservationRequest memberReservationRequest);

	MemberReservationResponseDto MemberReservationWithdraw(MemberWithdrawRequest memberWithdrawVo);

	Map<String, Object> MemberReservationList(MemberReservationListRequest memberInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException;

	ReservationDeleteContentResponseDto MemberReservationDeleteContent(MemberReservationDeleteRequest memberInfoRequest);

//    HotelSearchVo.Response SearchBar(HotelSearchVo.SearchBarRequest searchBarVoSearchBarRequest);
}
