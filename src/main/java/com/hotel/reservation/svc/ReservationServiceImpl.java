package com.hotel.reservation.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.*;
import com.hotel.mapper.MemberMapper;
import com.hotel.mapper.ReservationMapper;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.RegisterMemberRequest;
import com.hotel.reservation.vo.MemberInfoVo;
import com.hotel.reservation.vo.MemberInfoVo.MemberInfoRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfoResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawCheckDate;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDeleteContentResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDetailPaymentsRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationPaymentsRequest;
import com.hotel.reservation.vo.UnMemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationInfoResponseDto;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationRequest;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberWithdrawRequest;
import com.hotel.util.AES256Util;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLDataException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationMapper reservationMapper;

	private final MemberMapper memberMapper;

	private final AES256Util aesUtil;

	@Override
	public Map<String, Object> UnMemberReservationInfo(UnMemberReservationRequest unMemberReservationInfo) {

		Map<String, Object> map = new HashMap<>();
		List<UnMemberReservationInfoResponseDto> list = new ArrayList<>();
		UnMemberReservationInfoResponseDto dto = new UnMemberReservationInfoResponseDto();

		try {
			unMemberReservationInfo
					.setReservation_phone(aesUtil.encrypt(unMemberReservationInfo.getReservation_phone()));
		} catch (Exception e) {
			map.put("result", "ERR");
			map.put("reason", e);
			return map;
		}

		list = reservationMapper.unMemberReservationInfo(unMemberReservationInfo);

		// 예약정보 확인
		if (list.size() == 0) {
			map.put("result", "ERR");
			map.put("reason", "unReservation Info select fail");
			return map;
		} else {
			for (int i = 0; i < list.size(); i++) {
				String phone = list.get(i).getReservation_phone();
				try {
					list.get(i).setReservation_phone(aesUtil.decrypt(phone));
				} catch (Exception e) {
					map.put("result", "ERR");
					map.put("reason", e);
					return map;
				}
			}
			map.put("result", "OK");
			map.put("reason", "");
			map.put("list", list);

		}
		return map;
	}

	@Override
	public CommonResponseVo Payments(MemberReservationInfo memberReservationInfo) {
		CommonResponseVo result = new CommonResponseVo();

		if (memberReservationInfo.getReservation_price().equals("")) {
			result.setMessage("예약불가");
			return result;
		}

		result.setMessage("예약완료");
		return result;
	}

	@Override
	public MemberReservationResponseDto memberReservation(List<MemberReservationRequest> memberReservationRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		MemberReservationResponseDto dto = new MemberReservationResponseDto();
		// 예약정보 입력
		// 결재상세정보 입력
		// 결재정보 입력
		// 예약자 insert_user 조회
		String insert_user = null;
		Map<String, Object> unMemberMap = new HashMap<>();
		ReservationDetailPaymentsRequest payment = new ReservationDetailPaymentsRequest();

		for (int i = 0; i < memberReservationRequest.size(); i++) {

			if (!(memberReservationRequest.get(i).getMember_num() == null)) {
				insert_user = reservationMapper.selectUserInfo(memberReservationRequest.get(i).getMember_num());
				memberReservationRequest.get(i).setInsert_user(insert_user);
				if (insert_user == null) {
					dto.setResult("ERR");
					dto.setReason("insertNum Not Found");
				} 
			} else {

				// 비회원 예약 로직
				// 비회원 기본 정보 저장
				// 최초 1번만 가입
				MemberVo.RegisterMemberRequest memberVo = new MemberVo.RegisterMemberRequest();
				memberVo.setName(memberReservationRequest.get(i).getReservation_name());
				memberVo.setPhone_num(aesUtil.encrypt(memberReservationRequest.get(i).getReservation_phone()));
				
				//없는 회원이면 간편 가입 시킨다
				String phoneData = reservationMapper.checkUnMemberInfo(memberVo.getPhone_num());
				if (phoneData == null) {
					int UnMemberInfo = memberMapper.registerUnMemberInfo(memberVo);
					if (UnMemberInfo == 0) {
						// insert 실패 시 에러 처리
						dto.setResult("ERR");
						dto.setReason("unMember info insert fail");
						return dto;
					}
				}
				unMemberMap = reservationMapper.selectUnUserInfo(memberVo.getPhone_num());
				
				// 예약자 정보 입력
				memberReservationRequest.get(i).setInsert_user(String.valueOf(unMemberMap.get("insert_user")));
				memberReservationRequest.get(i).setMember_num((Integer) unMemberMap.get("member_num"));
			}
			
			System.out.println("data = " + memberReservationRequest.toString());
			// 암호화 후 저장
			memberReservationRequest.get(i)
					.setReservation_phone(aesUtil.encrypt(memberReservationRequest.get(i).getReservation_phone()));
		}
		
		for (int x = 0; x < memberReservationRequest.size(); x++) {
			MemberInfoVo.MemberReservationRequest vo = new MemberInfoVo.MemberReservationRequest();
			vo = memberReservationRequest.get(x);
			System.out.println("vo = " +vo.toString());
			int reservation_info = -1;
			try {
				reservation_info = reservationMapper.reservationInfo(vo);
			} catch (Exception e) {
				dto.setResult("ERR");
				dto.setReason("insert Duplicate entry phone");
			}

			if (reservation_info == 0) {
				// insert 실패 시 에러 처리
				dto.setResult("ERR");
				dto.setReason("reservation info insert fail");
				return dto;
			} else {

				// 예약완료 일 떄
				payment.setPayment_price(memberReservationRequest.get(x).getReservation_price());
				payment.setInsert_user(memberReservationRequest.get(x).getInsert_user());

			}

			// 가격 인서트
			int pay_info = reservationMapper.payInfo(payment);

			if (pay_info == 0) {
				// 가격 인서트 안될 경우 예약정보도 취소해야 함
				reservationMapper.reservationDeleteUnMember(memberReservationRequest.get(x).getInsert_user());
				reservationMapper.reservationDelete(memberReservationRequest.get(x).getMember_num());
				dto.setResult("ERR");
				dto.setReason("payment Info insert fail");
				return dto;
			} else {

				// 예약정보 + 결제상세정보 입력이 끝났을 때

				// 결제 정보 인서트
				int reservation_num = reservationMapper
						.selectReservationNum(memberReservationRequest.get(x).getMember_num());
				int payment_num = reservationMapper.selectPaymentNum(memberReservationRequest.get(x).getInsert_user());

				if (reservation_num != 0 && payment_num != 0) {

					ReservationPaymentsRequest req = new ReservationPaymentsRequest();

					req.setReservation_num(reservation_num);
					req.setPayment_detail_num(payment_num);
					req.setInsert_user(memberReservationRequest.get(x).getInsert_user());

					int d_payment = reservationMapper.insertPaymentInfo(req);

					if (d_payment == 0) {
						// 전체 취소
						reservationMapper.reservationDelete(memberReservationRequest.get(x).getMember_num());
						reservationMapper.paymentDelete(req.getInsert_user());

					} else {
						dto.setResult("OK");
						dto.setReason("reservation success");
					}
				}
			}
		}

		return dto;
	}

	@Override
	public MemberReservationResponseDto unMemberReservation(MemberReservationRequest memberReservationRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberReservationResponseDto MemberReservationWithdraw(MemberWithdrawRequest memberWithdrawVo) throws ParseException {
		MemberReservationResponseDto dto = new MemberReservationResponseDto();
		String insert_user;
		
		if(memberWithdrawVo.getReservation_status() == 2) {
			if (!(memberWithdrawVo.getEmail() == null)) {
				//member_num 필요
				int member_num = reservationMapper.checkMemberNum(memberWithdrawVo.getEmail());
				if(member_num == 0) {
					dto.setResult("ERR");
					dto.setReason("member_num Not Found");
					return dto;
				}
				memberWithdrawVo.setMember_num(member_num);
				insert_user = reservationMapper.selectInsertUser(memberWithdrawVo);
				if(insert_user == null) {
					dto.setResult("ERR");
					dto.setReason("member Reservation Not Found");
					return dto;
				}
				memberWithdrawVo.setUpdate_user(insert_user);
			}else {
				// 비회원 
				insert_user = reservationMapper.selectUnInsertUser(memberWithdrawVo);
				memberWithdrawVo.setUpdate_user(insert_user);
			}
			
		}else if(memberWithdrawVo.getReservation_status() == 3){
			insert_user = reservationMapper.selectBusinessInsertUser(memberWithdrawVo.getBusiness_user_num());
			if(insert_user == null) {
				dto.setResult("ERR");
				dto.setReason("business_user_num Reservation Not Found");
				return dto;
			}
			memberWithdrawVo.setUpdate_user(insert_user);
		}
		MemberWithdrawCheckDate checkDate = new MemberWithdrawCheckDate();
		// status 값 및 날짜 필터링
		checkDate = reservationMapper.reservationCheckTime(memberWithdrawVo.getReservation_num());
		
		if(checkDate.getSt_date() == null) {
			dto.setResult("ERR");
			dto.setReason("reservation data Not found");
			return dto;
		}
		
		System.out.println("checkDate = " + checkDate.toString());
		
		Date date = new Date();
		//String st_date = (String) checkMap.get("st_date");
		//String ed_date = (String) checkMap.get("ed_date");
		String st_date = String.valueOf(checkDate.getSt_date());
		//String ed_date = String.valueOf(checkMap.get("ed_date"));
		System.out.println("st_date = " + st_date);
		Date stDate = new Date();
		//Date edDate = new Date();
		SimpleDateFormat format = new  SimpleDateFormat("yy-MM-dd");
		
		String today = format.format(date);
		date = format.parse(today);
		stDate = format.parse(st_date);
		//edDate = format.parse(ed_date);
		
		if(date.equals(stDate)) {
			dto.setResult("ERR");
			dto.setReason("today check st_date NOW ERR");
			return dto;
		}else if(date.after(stDate)) {
			dto.setResult("ERR");
			dto.setReason("today check st_date after ERR");
			return dto;
		}
		
		int reservation_cancel = reservationMapper.reservationCancelUpdate(memberWithdrawVo);
		if (reservation_cancel == 0) {
			dto.setResult("ERR");
			dto.setReason("reservation_cancel update fail");
			return dto;
		} else {

			int reservation_delete = reservationMapper.reservationDeleteUpdate(memberWithdrawVo);

			if (reservation_delete == 0) {
				dto.setResult("ERR");
				dto.setReason("reservation_delete update fail");
				return dto;
			} else {
				dto.setResult("OK");
				dto.setReason("reservation_delete success");
			}
		}
		return dto;
	}

	@Override
	public Map<String, Object> MemberReservationList(MemberReservationListRequest memberInfo)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		Map<String, Object> map = new HashMap<>();
		List<MemberReservationListInfoResponseDto> list = new ArrayList<>();
		int memberNum = reservationMapper.checkMemberNum(memberInfo.getEmail());
		Integer member_num = memberNum;
		Integer totalCnt;
		if (member_num.toString() == null) {
			map.put("result", "ERR");
			map.put("reason", "tokenNotFound");
			map.put("list", list);
			return map;
		} else {
			memberInfo.setMember_num(member_num);
			totalCnt = reservationMapper.selectReservationCnt(memberInfo);
			if (totalCnt.toString().equals("")) {
				map.put("result", "OK");
				map.put("reason", "total_cnt");
				map.put("total_cnt", 0);
				map.put("list", list);
				return map;
			}
		}
		memberInfo.setMember_num(member_num);
		// 페이징 처리
		int page = memberInfo.getPage();
		// 총 갯수 30개 일 때

		if (1 == memberInfo.getPage()) {
			page = 0;
		} else if (1 < memberInfo.getPage()) {
			page = memberInfo.getPage_cnt() * memberInfo.getPage();
			memberInfo.setPage(page);
		}
		list = reservationMapper.reservationList(memberInfo);

		if (list.size() == 0) {
			map.put("result", "OK");
			map.put("reason", "");
			map.put("total_cnt", totalCnt);
			map.put("list", list);
			return map;
		} else {
			// 전화번호 복호화
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setReservation_phone(aesUtil.decrypt(list.get(i).getReservation_phone()));
			}
			map.put("result", "OK");
			map.put("reason", "");
			map.put("page", page);
			map.put("total_cnt", totalCnt);
			map.put("list", list);
		}
		return map;
	}

	@Override
	public ReservationDeleteContentResponseDto MemberReservationDeleteContent(
			MemberReservationDeleteRequest memberInfoRequest) {
		ReservationDeleteContentResponseDto dto = new ReservationDeleteContentResponseDto();

		System.out.println("test = " + memberInfoRequest.toString());

		String data = reservationMapper.selectReservationCancelContent(memberInfoRequest);

		System.out.println("data = " + data);

		if (data == null) {
			dto.setResult("ERR");
			dto.setReason("content Not Found");
			dto.setContent("");
			return dto;

		}
		dto.setResult("OK");
		dto.setReason("");
		dto.setContent(data);
		return dto;
	}

	@Override
	public String checkMemberInfo(String email) {

		String id = reservationMapper.checkMemberInfo(email);
		if (id.equals("")) {
			return null;
		}
		return id;
	}

	@Override
	public int selectMemberNum(String email) {
		return reservationMapper.checkMemberNum(email);
	}

//    @Override
//    public CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelRequest) {
//        CommonResponseVo result = new CommonResponseVo();
//        // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장
//
//        result.setMessage("호텔 등록 완료");
//        return result;
//    }

//    @Override
//    public HotelSearchVo.Response SearchBar(HotelSearchVo.SearchBarRequest searchBarVoRequest) {
//        //검색어에 일치하는 지역명, 호텔명, 호텔주소 있는지 DB 조회
//        HotelSearchVo.Response result = new HotelSearchVo.Response();
//        HotelSearchVo.resultData resultData = new HotelSearchVo.resultData();
//
//        List<String> region_list = new ArrayList<>();
//        region_list.add("서울");
//
//        List<String> hotel_address_list = new ArrayList<>();
//        hotel_address_list.add("노보텔 엠비시티 서울 용산");
//
//        List<String> hotel_name_list = new ArrayList<>();
//        hotel_name_list.add("서울신라호텔");
//        hotel_name_list.add("밀레니엄 힐튼 서울");
//
//        resultData.setRegion_list(region_list);
//        resultData.setHotel_address_list(hotel_address_list);
//        resultData.setHotel_name_list(hotel_name_list);
//
//        result.setMessage("검색 완료");
//        result.setData(resultData);
//
//        return result;
//
//    }

}
