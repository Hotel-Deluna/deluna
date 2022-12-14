package com.hotel.reservation.vo;

import java.math.BigDecimal;
import java.util.List;

import com.hotel.common.CommonResponseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberInfoVo {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "비회원 예약정보 조회")
	public static class PayMentsResponse extends CommonResponseVo {
		@Schema(description = "데이터")
		MemberInfoVo.MemberReservationInfo data;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "금액 정보 파라미터")
	public static class MemberReservationInfo {

		@Schema(description = "결제금액", required = true, example = "300000")
		String reservation_price;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "회원정보 파라미터")
	public static class MemberInfoRequest {
		@Schema(description = "회원번호", required = true, example = "000001")
		String user_num;
	}

	@Data
	@AllArgsConstructor
	@Schema(description = "회원 예약삭제 사유 파라미터")
	public static class MemberReservationDeleteRequest {
		@Schema(description = "예약번호", required = true, example = "3")
		Integer reservation_num;
		
		@Schema(description = "email", required = true, example = "백엔드사용")
		String email;
		
		@Schema(description = "구분값", required = true, example = "백엔드사용")
		Integer role;
		
		@Schema(description = "insert_user", required = true, example = "백엔드사용")
		String insert_user;
		
	}

//	@Data
//	@AllArgsConstructor
//	@NoArgsConstructor
//	@Schema(description = "회원 예약삭제 사유")
//	public static class MemberReservationDeleteInfo {
//		@Schema(description = "예약삭제 사유", required = true, example = "왜 그럴때 있잖아요....")
//		String content;
//	}

	@Data
	@NoArgsConstructor
	@Schema(description = "고객 예약 리스트")
	public static class MemberReservationList {

		@Schema(description = "고객 예약 리스트", required = true)
		List<MemberReservationListInfoResponseDto> list;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "예약내역 조회 파라미터")
	public static class MemberReservationListRequest {
		
		@Schema(description = "보여질 페이지", required = true, example = "0")
		Integer page;
		
		@Schema(description = "페이지 당 출력할 리스트 갯수", required = true, example = "0")
		Integer page_cnt;
		
		@Schema(description = "예약 상태 코드", required = true, example = "0")
		Integer reservation_status;
		
		@Schema(description = "예약 시작일", required = true, example = "2022-08-28")
		String st_date;
		
		@Schema(description = "예약 종료일", required = true, example = "2022-08-28")
		String ed_date;
		
		@Schema(description = "member_num", required = true, example = "백엔드에서 사용")
		Integer member_num;
		
		@Schema(description = "토큰 이메일", required = true, example = "백엔드에서 사용")
		String email;

	}

	@Data
	@NoArgsConstructor
	@Schema(description = "회원 예약 리스트 조회 response")
	public static class MemberReservationListInfoResponseDto {
		
		@Schema(description = "예약번호", required = true, example = "1")
		Integer reservation_num;
		
//		@Schema(description = "호텔번호", required = true, example = "1")
//		Integer hotel_num;

		@Schema(description = "객실상세번호", required = true, example = "2")
		Integer room_detail_num;

		@Schema(description = "예약자명", required = true, example = "예약자명")
		String reservation_name;

		@Schema(description = "휴대폰번호", required = true, example = "01012345678")
		String reservation_phone;

		@Schema(description = "호텔명", required = true, example = "호텔 델루나")
		String name;

		@Schema(description = "객실명", required = true, example = "스탠다드 룸")
		String room_detail_name;

		@Schema(description = "투숙인원", required = true, example = "2")
		Integer reservation_people;

		@Schema(description = "예약 시작일", required = true, example = "2022-08-28 14:20:00")
		String st_date;

		@Schema(description = "예약 종료일", required = true, example = "2022-08-29 11:00:00")
		String ed_date;

		@Schema(description = "결제금액", required = true, example = "300000")
		Integer reservation_price;

		@Schema(description = "예약상태", required = true, example = "0")
		String reservation_status;
		
		@Schema(description = "예약일", required = true, example = "0")
		String dt_insert;

	}

	@Data
	@NoArgsConstructor
	@Schema(description = "회원 예약하기")
	public static class MemberReservationRequest {

		@Schema(description = "객실상세번호", required = true, example = "000002")
		Integer room_detail_num;
		
		@Schema(description = "호텔번호", required = true, example = "000002")
		Integer hotel_num;

		@Schema(description = "예약자명", required = true, example = "예약자명")
		String reservation_name;

		@Schema(description = "휴대폰번호", required = true, example = "01012345678")
		String reservation_phone;

		@Schema(description = "투숙인원", required = true, example = "2")
		Integer reservation_people;

		@Schema(description = "예약 시작일", required = true, example = "2022-08-28 14:20:00")
		String st_date;

		@Schema(description = "예약 종료일", required = true, example = "2022-08-29 11:00:00")
		String ed_date;

		@Schema(description = "결제금액", required = true, example = "300000")
		Integer reservation_price;
		
		@Schema(description = "role", required = true, example = "고객:1, 사업자:2, 비회원:3")
		Integer role;
		
		@Schema(description = "insert_user", required = true, example = "백엔드에서만 사용")
		String insert_user;
		
		@Schema(description = "회원번호", required = true, example = "백엔드에서만 사용")
		Integer member_num;
		
	}

	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ReservationPaymentsRequest {

		Integer reservation_num;
		
		Integer payment_detail_num;
		
		String insert_user;
		
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ReservationDetailPaymentsRequest {

		Integer payment_price;
		
		String insert_user;
		
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UnMemberInfo {

		String insert_user;
		
		Integer member_num;
		
	}
	

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "회원 예약 취소")
	public static class MemberWithdrawRequest {

		@Schema(description = "회원 예약번호", required = true, example = "1108668")
		Integer reservation_num;
		
		@Schema(description = "사업자 회원번호", required = true, example = "백엔드사용")
		Integer business_user_num;
		
		@Schema(description = "상태값", required = true, example = "11")
		Integer reservation_status;
		
		@Schema(description = "비회원 탈퇴 사유", required = true, example = "다른호텔 예약")
		String content;
		
		@Schema(description = "이메일", required = true, example = "백엔드 사용")
		String email;
		
		@Schema(description = "회원번호", required = true, example = "백엔드 사용")
		Integer member_num;
		
		@Schema(description = "update_user", required = true, example = "백엔드만 사용")
		String update_user;

	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "회원 예약 날짜 조회")
	public static class MemberWithdrawCheckDate {

		String st_date;
		
		String ed_date;
		

	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "공통 예약 API Response")
	public static class MemberReservationResponseDto {

		@Schema(description = "api 결과값", required = true, example = "OK or ERR")
		String result;
		
		@Schema(description = "상세내용", required = true, example = "param Not Found")
		String reason;
		
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "예약취소 사유 조회 Response")
	public static class ReservationDeleteContentResponseDto {

		@Schema(description = "api 결과값", required = true, example = "OK or ERR")
		String result;
		
		@Schema(description = "상세내용", required = true, example = "param Not Found")
		String reason;
		
		@Schema(description = "취소사유", required = true, example = "지구오락실")
		String content;
		
	}


}
