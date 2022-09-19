package com.hotel.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDetailPaymentsRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationPaymentsRequest;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationRequest;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberWithdrawRequest;

@Mapper
@Repository
public interface ReservationMapper {

	int reservationInfo(MemberReservationRequest memberReservationRequest);

	int payInfo(ReservationDetailPaymentsRequest payment);

	int selectReservationNum(int member_num);

	void reservationDelete(int member_num);

	int selectPaymentNum(MemberReservationRequest memberReservationRequest);

	String selectUserInfo(int member_num);

	int insertPaymentInfo(ReservationPaymentsRequest req);

	void paymentDelete(String insert_user);

	int reservationCancelUpdate(MemberWithdrawRequest memberWithdrawVo);

	int selectInsertUser(MemberWithdrawRequest memberWithdrawVo);

	int reservationDeleteUpdate(MemberWithdrawRequest memberWithdrawVo);

	int paymentNum(int reservation_num);

	List<MemberReservationListInfo> reservationList(MemberReservationListRequest memberInfo);

	String selectReservationCancelContent(MemberReservationDeleteRequest memberInfoRequest);

	String selectMemberPhoneInfo(UnMemberReservationRequest unMemberReservationInfo);
	
	/////////////////////비회원 추가///////////////////////////////
	List<UnMemberReservationRequest> unMemberReservationInfo(UnMemberReservationRequest unMemberReservationInfo);
	
	int selectInsertUnUser(UnMemberWithdrawRequest unMemberWithdrawVo);
	
	int unReservationCancelUpdate(UnMemberWithdrawRequest unMemberWithdrawVo);
	
	int unReservationDeleteUpdate(UnMemberWithdrawRequest unMemberWithdrawVo);
}
