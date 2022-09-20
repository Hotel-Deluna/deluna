package com.hotel.reservation.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.*;
import com.hotel.mapper.ReservationMapper;
import com.hotel.reservation.vo.MemberInfoVo;
import com.hotel.reservation.vo.MemberInfoVo.MemberInfoRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDeleteContentResponseDto;
import com.hotel.reservation.vo.MemberInfoVo.ReservationDetailPaymentsRequest;
import com.hotel.reservation.vo.MemberInfoVo.ReservationPaymentsRequest;
import com.hotel.reservation.vo.UnMemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationRequest;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberWithdrawRequest;
import com.hotel.util.AES256Util;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationMapper reservationMapper;

	private final AES256Util aesUtil;

	@Override
	public Map<String, Object> UnMemberReservationInfo(
			UnMemberReservationRequest unMemberReservationInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		Map<String, Object> result = new HashMap<>();

		String phone = reservationMapper.selectMemberPhoneInfo(unMemberReservationInfo);
		
		if(phone.equals("")) {
			result.put("result", "ERR");
			result.put("reason", "reservation_num Not Found");
			return result;
		}
		
		//암호화 된 핸드폰 번호 복호화
		phone = aesUtil.decrypt(phone);
		String reqPhone = unMemberReservationInfo.getReservation_phone();
		List<UnMemberReservationRequest> list;
		
		//예약된 고객 번호와 입력된 고객번호가 맞는지 확인 후
		if(reqPhone.equals(phone)) {
			list = reservationMapper.unMemberReservationInfo(unMemberReservationInfo);	
		}else {
			result.put("result", "ERR");
			result.put("reason", "phone Not Found");
			return result;
		}
		
		//예약정보 확인
		if(list.size() == 0) {
			result.put("result", "ERR");
			result.put("reason", "reservation Info Not Found");
			return result;
		}else {
			result.put("result", "OK");
			result.put("reason", "");
			result.put("data", list);
		}
		return result;
	}

	@Override
	public MemberReservationResponseDto UnMemberReservationWithdraw(UnMemberWithdrawRequest unMemberWithdrawVo) {
		MemberReservationResponseDto result = new MemberReservationResponseDto();
		Map<String, Object> map = new HashMap<>();
		int member_num;

		if (unMemberWithdrawVo.getMember_num() == 0) {
			member_num = reservationMapper.selectInsertUnUser(unMemberWithdrawVo);
		}

		int reservation_cancel = reservationMapper.unReservationCancelUpdate(unMemberWithdrawVo);
		if (reservation_cancel == 0) {
			result.setResult("ERR");
			result.setReason("reservation_cancel Fail");
			return result;
		} else {

			int reservation_num = unMemberWithdrawVo.getUpdate_user();

			int payment_detail_num = reservationMapper.paymentNum(reservation_num);

			if (payment_detail_num == 0) {
				result.setResult("ERR");
				result.setReason("payment_num Not Found");
				return result;
			}
			unMemberWithdrawVo.setPayment_detail_num(payment_detail_num);

			int reservation_delete = reservationMapper.unReservationDeleteUpdate(unMemberWithdrawVo);

			if (reservation_delete == 0) {
				result.setResult("ERR");
				result.setReason("reservation_delete Update Fail");
				return result;
			}else {
				result.setResult("OK");
				result.setReason("delete success");
			}
		}
		return result;
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
	public MemberReservationResponseDto memberReservation(MemberReservationRequest memberReservationRequest) {
		MemberReservationResponseDto dto = new MemberReservationResponseDto();
		Map<String, Object> map = new HashMap<>();
		// 예약정보 입력
		// 결재상세정보 입력
		// 결재정보 입력

		// 예약자 insert_user 조회
		String insert_user = reservationMapper.selectUserInfo(memberReservationRequest.getMember_num());

		if (insert_user == null) {
			dto.setResult("ERR");
			dto.setReason("insert_user select fail");
			return dto;
		}

		// 예약자 정보 입력
		memberReservationRequest.setInsert_user(insert_user);
		// 예약 인서트
		int reservation_info = reservationMapper.reservationInfo(memberReservationRequest);

		if (reservation_info == 0) {
			// insert 실패 시 에러 처리
			dto.setResult("ERR");
			dto.setReason("reservation info insert fail");
			return dto;
		} else {

			// 예약완료 일 떄
			ReservationDetailPaymentsRequest payment = new ReservationDetailPaymentsRequest();

			payment.setPayment_price(memberReservationRequest.getReservation_price());
			payment.setInsert_user(insert_user);

			// 가격 인서트
			int pay_info = reservationMapper.payInfo(payment);

			if (pay_info == 0) {
				// 가격 인서트 안될 경우 예약정보도 취소해야 함
				reservationMapper.reservationDelete(memberReservationRequest.getMember_num());
				dto.setResult("ERR");
				dto.setReason("payment Info insert fail");
				return dto;
			} else {

				// 예약정보 + 결제상세정보 입력이 끝났을 때

				// 결제 정보 인서트
				int reservation_num = reservationMapper.selectReservationNum(memberReservationRequest.getMember_num());
				int payment_num = reservationMapper.selectPaymentNum(memberReservationRequest);

				if (reservation_num != 0 && payment_num != 0) {

					ReservationPaymentsRequest req = new ReservationPaymentsRequest();

					req.setReservation_num(reservation_num);
					req.setPayment_detail_num(payment_num);
					req.setInsert_user(insert_user);

					int d_payment = reservationMapper.insertPaymentInfo(req);

					if (d_payment == 0) {
						// 전체 취소
						reservationMapper.reservationDelete(memberReservationRequest.getMember_num());
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
	public MemberReservationResponseDto MemberReservationWithdraw(MemberWithdrawRequest memberWithdrawVo) {
		MemberReservationResponseDto dto = new MemberReservationResponseDto();
		int member_num;

		if (memberWithdrawVo.getMember_num() == 0) {
			member_num = reservationMapper.selectInsertUser(memberWithdrawVo);
		}

		int reservation_cancel = reservationMapper.reservationCancelUpdate(memberWithdrawVo);
		if (reservation_cancel == 0) {
			dto.setResult("ERR");
			dto.setReason("reservation_cancel update fail");
			return dto;
		} else {

			int reservation_num = memberWithdrawVo.getUpdate_user();

			int payment_detail_num = reservationMapper.paymentNum(reservation_num);

			if (payment_detail_num == 0) {
				dto.setResult("ERR");
				dto.setReason("payment_num Not Found");
				return dto;
			}
			memberWithdrawVo.setPayment_detail_num(payment_detail_num);

			int reservation_delete = reservationMapper.reservationDeleteUpdate(memberWithdrawVo);

			if (reservation_delete == 0) {
				dto.setResult("ERR");
				dto.setReason("reservation_delete update fail");
				return dto;
			}else {
				dto.setResult("OK");
				dto.setReason("reservation_delete success");
			}
		}
		return dto;
	}

	@Override
	public Map<String, Object> MemberReservationList(MemberReservationListRequest memberInfo)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		List<MemberReservationListInfo> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();

		list = reservationMapper.reservationList(memberInfo);
		
		System.out.println("list = " + list.toString());
		
		if (list.size() == 0) {
			map.put("result", "ERR");
			map.put("message", "data Not Found");
			map.put("data", list);
		} else {
			//전화번호 복호화
			for (int i = 0; i < list.size(); i++) {
				
				list.get(i).setReservation_phone(aesUtil.decrypt(list.get(i).getReservation_phone()));
			}
			map.put("result", "OK");
			map.put("message", "");
			map.put("data", list);
		}
		return map;
	}

	@Override
	public ReservationDeleteContentResponseDto MemberReservationDeleteContent(MemberReservationDeleteRequest memberInfoRequest) {
		ReservationDeleteContentResponseDto dto = new ReservationDeleteContentResponseDto();
		
		String data = reservationMapper.selectReservationCancelContent(memberInfoRequest);
		
		if(data == null) {
			dto.setResult("ERR");
			dto.setReason("content Not Found");
			dto.setContent("");
			return dto;
			
		}
		dto.setResult("ERR");
		dto.setReason("content Not Found");
		dto.setContent(data);
		return dto;
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
