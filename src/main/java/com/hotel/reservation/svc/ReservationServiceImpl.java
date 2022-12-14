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
import com.hotel.reservation.vo.MemberInfoVo.UnMemberInfo;
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

		// ???????????? ??????
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
			result.setMessage("????????????");
			return result;
		}

		result.setMessage("????????????");
		return result;
	}

	@Override
	public MemberReservationResponseDto memberReservation(List<MemberReservationRequest> memberReservationRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		MemberReservationResponseDto dto = new MemberReservationResponseDto();
		// ???????????? ??????
		// ?????????????????? ??????
		// ???????????? ??????
		// ????????? insert_user ??????
		String insert_user = null;
		UnMemberInfo unMemberInfo = new UnMemberInfo();
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

				// ????????? ?????? ??????
				// ????????? ?????? ?????? ??????
				// ?????? 1?????? ??????
				MemberVo.RegisterMemberUnMemberRequest unMemberVo = new MemberVo.RegisterMemberUnMemberRequest();
				unMemberVo.setName(memberReservationRequest.get(i).getReservation_name());
				unMemberVo.setPhone_num(aesUtil.encrypt(memberReservationRequest.get(i).getReservation_phone()));
				unMemberVo.setRole(memberReservationRequest.get(i).getRole());
				
				//?????? ???????????? ?????? ?????? ?????????
				// ?????? ????????? ??????????????? ??????????????? ??????
				String phoneData = reservationMapper.checkUnMemberInfo(unMemberVo);
				if (phoneData == null) {
					int UnMemberInfo = memberMapper.registerUnMemberInfo(unMemberVo);
					if (UnMemberInfo == 0) {
						// insert ?????? ??? ?????? ??????
						dto.setResult("ERR");
						dto.setReason("unMember info insert fail");
						return dto;
					}
				}else {
					// ?????? ????????? ??????????????? ??????
					int memberUnmemberInfo = memberMapper.registerMemberUnMemberInfo(unMemberVo);
					if(memberUnmemberInfo == 0) {
						dto.setResult("ERR");
						dto.setReason("unMember info insert fail");
						return dto;
					}
					
				}
				
				unMemberInfo = reservationMapper.selectUnUserInfo(unMemberVo);
				
				// ????????? ?????? ??????
				memberReservationRequest.get(i).setInsert_user(unMemberInfo.getInsert_user());
				memberReservationRequest.get(i).setMember_num(unMemberInfo.getMember_num());
			}
			
			System.out.println("data = " + memberReservationRequest.toString());
			// ????????? ??? ??????
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
				// insert ?????? ??? ?????? ??????
				dto.setResult("ERR");
				dto.setReason("reservation info insert fail");
				return dto;
			} else {

				// ???????????? ??? ???
				payment.setPayment_price(memberReservationRequest.get(x).getReservation_price());
				payment.setInsert_user(memberReservationRequest.get(x).getInsert_user());

			}

			// ?????? ?????????
			int pay_info = reservationMapper.payInfo(payment);

			if (pay_info == 0) {
				// ?????? ????????? ?????? ?????? ??????????????? ???????????? ???
				reservationMapper.reservationDeleteUnMember(memberReservationRequest.get(x).getInsert_user());
				reservationMapper.reservationDelete(memberReservationRequest.get(x).getMember_num());
				dto.setResult("ERR");
				dto.setReason("payment Info insert fail");
				return dto;
			} else {

				// ???????????? + ?????????????????? ????????? ????????? ???

				// ?????? ?????? ?????????
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
						// ?????? ??????
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
				//member_num ??????
				String memberNum = reservationMapper.checkMemberNum(memberWithdrawVo.getEmail());
				if(memberNum == null) {
					dto.setResult("ERR");
					dto.setReason("member_num Not Found");
					return dto;
				}
				
				Integer member_num = Integer.valueOf(memberNum);				
				memberWithdrawVo.setMember_num(member_num);
				insert_user = reservationMapper.selectInsertUser(memberWithdrawVo);
				if(insert_user == null) {
					dto.setResult("ERR");
					dto.setReason("member Reservation Not Found");
					return dto;
				}
				memberWithdrawVo.setUpdate_user(insert_user);
			}else {
				// ????????? ????????????...
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
		// status ??? ??? ?????? ?????????
		checkDate = reservationMapper.reservationCheckTime(memberWithdrawVo.getReservation_num());
		
		if(checkDate.getSt_date() == null) {
			dto.setResult("ERR");
			dto.setReason("reservation data Not found");
			return dto;
		}
		
		
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
		Integer member_num;
		Integer totalCnt = 0;
		String memberNum = reservationMapper.checkMemberNum(memberInfo.getEmail());
		if (memberNum == null) {
			map.put("result", "ERR");
			map.put("reason", "tokenNotFound");
			map.put("list", totalCnt);
			map.put("list", list);
			return map;
		} else {
			member_num = Integer.valueOf(memberNum) ;
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
		// ????????? ??????
		int page = memberInfo.getPage();
		// ??? ?????? 30??? ??? ???

		if (1 == memberInfo.getPage() || 0 == memberInfo.getPage()) {
			page = 0;
			memberInfo.setPage(page);
		} else if (1 < memberInfo.getPage()) {
			page = memberInfo.getPage_cnt() * memberInfo.getPage();
			memberInfo.setPage(page);
		}
		System.out.println("data = "+ memberInfo.toString());
		
		if(memberInfo.getReservation_status() == 0) {
			list = reservationMapper.reservationAllList(memberInfo);
		}else {
			list = reservationMapper.reservationList(memberInfo);
		}
		if (list.size() == 0) {
			map.put("result", "OK");
			map.put("reason", "");
			map.put("total_cnt", list.size());
			map.put("list", list);
			return map;
		} else {
			// ???????????? ?????????
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setReservation_phone(aesUtil.decrypt(list.get(i).getReservation_phone()));
			}
			map.put("result", "OK");
			map.put("reason", "");
			map.put("page", page);
			map.put("total_cnt", list.size());
			map.put("list", list);
		}
		return map;
	}

	@Override
	public ReservationDeleteContentResponseDto MemberReservationDeleteContent(
			MemberReservationDeleteRequest memberInfoRequest) {
		ReservationDeleteContentResponseDto dto = new ReservationDeleteContentResponseDto();
		String data = null;
		//????????? ??????
		if(memberInfoRequest.getRole() == 3) {
			data = reservationMapper.selectUnReservationCancelContent(memberInfoRequest);
		}else {
			String insert_user = reservationMapper.selectInsertUser(memberInfoRequest.getEmail());
			memberInfoRequest.setInsert_user(insert_user);
			data = reservationMapper.selectReservationCancelContent(memberInfoRequest);
		}

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
		if (id == null) {
			return null;
		}
		return id;
	}

	@Override
	public int selectMemberNum(String email) {
			
		String data = reservationMapper.checkMemberNum(email);
		
		if(data == null) {
			return 0;
		}
		Integer num = Integer.valueOf(data);
		
		return  num;
	}

//    @Override
//    public CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelRequest) {
//        CommonResponseVo result = new CommonResponseVo();
//        // registerHotelParamVo ???????????? + ????????? + ???????????? + ????????? ?????? DB ??????
//
//        result.setMessage("?????? ?????? ??????");
//        return result;
//    }

//    @Override
//    public HotelSearchVo.Response SearchBar(HotelSearchVo.SearchBarRequest searchBarVoRequest) {
//        //???????????? ???????????? ?????????, ?????????, ???????????? ????????? DB ??????
//        HotelSearchVo.Response result = new HotelSearchVo.Response();
//        HotelSearchVo.resultData resultData = new HotelSearchVo.resultData();
//
//        List<String> region_list = new ArrayList<>();
//        region_list.add("??????");
//
//        List<String> hotel_address_list = new ArrayList<>();
//        hotel_address_list.add("????????? ???????????? ?????? ??????");
//
//        List<String> hotel_name_list = new ArrayList<>();
//        hotel_name_list.add("??????????????????");
//        hotel_name_list.add("???????????? ?????? ??????");
//
//        resultData.setRegion_list(region_list);
//        resultData.setHotel_address_list(hotel_address_list);
//        resultData.setHotel_name_list(hotel_name_list);
//
//        result.setMessage("?????? ??????");
//        result.setData(resultData);
//
//        return result;
//
//    }

}
