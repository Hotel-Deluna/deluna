package com.hotel.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hotel.member.vo.MemberVo.RegisterMemberRequest;
import com.hotel.reservation.vo.MemberInfoVo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfoResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDetailPaymentsRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationPaymentsRequest;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationInfoResponseDto;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationRequest;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberWithdrawRequest;

@Mapper
@Repository
public interface ReservationMapper {

	int reservationInfo(MemberInfoVo.MemberReservationRequest vo);

	int payInfo(ReservationDetailPaymentsRequest payment);

	int selectReservationNum(int member_num);

	void reservationDelete(int member_num);

	int selectPaymentNum(String string);

	String selectUserInfo(int member_num);

	int insertPaymentInfo(ReservationPaymentsRequest req);

	void paymentDelete(String insert_user);

	int reservationCancelUpdate(MemberWithdrawRequest memberWithdrawVo);

	String selectInsertUser(MemberWithdrawRequest memberWithdrawVo);

	int reservationDeleteUpdate(MemberWithdrawRequest memberWithdrawVo);

	int paymentNum(String reservation_num);

	List<MemberReservationListInfoResponseDto> reservationList(MemberReservationListRequest memberInfo);

	String selectReservationCancelContent(MemberReservationDeleteRequest memberInfoRequest);

	String selectMemberPhoneInfo(UnMemberReservationRequest unMemberReservationInfo);
	
	/////////////////////비회원 추가///////////////////////////////
	List<UnMemberReservationInfoResponseDto> unMemberReservationInfo(UnMemberReservationRequest unMemberReservationInfo);
	
	int selectInsertUnUser(UnMemberWithdrawRequest unMemberWithdrawVo);
	
	String selectInsertUserInEmail(String email);

	int selectMemberNum(String email);

	String checkMemberInfo(String email);

	int checkMemberNum(String email);

	Integer selectReservationCnt(MemberReservationListRequest memberInfo);

	Map<String, Object> selectUnUserInfo(String string);

	void reservationDeleteUnMember(String insert_user);

	String checkUnMemberInfo(String phone_num);
	
	String selectInsertUser(String email);

	String selectUnInsertUser(MemberWithdrawRequest memberWithdrawVo);

	Map<String, Object> reservationCheckTime(Integer integer);

	String selectBusinessInsertUser(Integer business_user_num);


}
