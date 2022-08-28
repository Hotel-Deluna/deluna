package com.hotel.reservation.svc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import com.hotel.reservation.vo.MemberInfoVo.MemberInfoRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawRequest;
import com.hotel.reservation.vo.MemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo;

@Service
public interface ReservationService {

	UnMemberInfoVo.UnMemberResponse UnMemberReservationInfo(UnMemberInfoVo.UnMemberReservationRequest unMemberReservationInfo);
	
	CommonResponseVo UnMemberReservationWithdraw(UnMemberInfoVo.UnMemberWithdrawRequest unMemberWithdrawVo);

	CommonResponseVo Payments(MemberReservationInfo memberReservationInfo);

	CommonResponseVo memberReservation(MemberReservationRequest memberReservationRequest);

	CommonResponseVo MemberReservationWithdraw(MemberWithdrawRequest memberWithdrawVo);

	Map<String, Object> MemberReservationList(MemberInfoRequest memberInfo);

	Map<String, Object> MemberReservationDeleteContent(MemberReservationDeleteRequest memberInfoRequest);

//    HotelSearchVo.Response SearchBar(HotelSearchVo.SearchBarRequest searchBarVoSearchBarRequest);
}
