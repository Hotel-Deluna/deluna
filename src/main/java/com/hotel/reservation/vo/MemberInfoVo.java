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
	@NoArgsConstructor
	@Schema(description = "회원 예약삭제 사유 파라미터")
	public static class MemberReservationDeleteRequest {
		@Schema(description = "회원번호", required = true, example = "000001")
		String user_num;

		@Schema(description = "예약번호", required = true, example = "000001")
		String reservation_num;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "회원 예약삭제 사유")
	public static class MemberReservationDeleteInfo {
		@Schema(description = "예약삭제 사유", required = true, example = "왜 그럴때 있잖아요....")
		String content;
	}

	@Data
	@NoArgsConstructor
	@Schema(description = "고객 예약 리스트")
	public static class MemberReservationList {

		@Schema(description = "고객 예약 리스트", required = true)
		List<MemberReservationListInfo> list;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "예약내역 조회 파라미터")
	public static class MemberReservationListRequest {
		@Schema(description = "회원번호", required = true, example = "000001")
		String user_num;

		@Schema(description = "예약 상태 코드", required = true, example = "1:전체, 2:예약확정, 3:예약취소, 4:사용완료")
		String reservation_status;
	}

	@Data
	@NoArgsConstructor
	@Schema(description = "고객 예약 리스트 조회")
	public static class MemberReservationListInfo {

		@Schema(description = "예약번호", required = true, example = "000001")
		String reservation_num;

		@Schema(description = "객실상세번호", required = true, example = "000002")
		String room_detail_num;

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
		String reservation_price;

		@Schema(description = "예약상태", required = true, example = "0")
		String reservation_status;

	}

	@Data
	@NoArgsConstructor
	@Schema(description = "회원 예약하기")
	public static class MemberReservationRequest {

		@Schema(description = "회원번호", required = true, example = "000001")
		String member_num;

		@Schema(description = "객실상세번호", required = true, example = "000002")
		String room_detail_num;

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
		BigDecimal reservation_price;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "결제 정보 조회")
	public static class ReservationPaymentsInfo {
		@Schema(description = "예약번호", required = true, example = "0013313")
		String reservation_num;

		@Schema(description = "예약상세번호", required = true, example = "0000001")
		String payment_detail_num;

		@Schema(description = "결제상태", required = true, example = "0")
		String payment_status;

		@Schema(description = "결제번호", required = true, example = "pg사 결제구분 번호:111111")
		String trade_num;

		@Schema(description = "결제금액", required = true, example = "300000")
		String payment_price;

		@Schema(description = "환불금액", required = true, example = "300000")
		String refund_price;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "회원 예약 취소")
	public static class MemberWithdrawRequest {

		@Schema(description = "회원 예약번호", required = true, example = "1108668")
		String reservation_num;

		@Schema(description = "객실 상세정보 번호", required = true, example = "3332221")
		String room_detail_num;

		@Schema(description = "비회원 탈퇴 사유", required = true, example = "다른호텔 예약")
		String reason;

	}

}
