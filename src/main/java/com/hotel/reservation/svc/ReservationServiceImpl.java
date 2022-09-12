package com.hotel.reservation.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.*;
import com.hotel.reservation.vo.MemberInfoVo;
import com.hotel.reservation.vo.MemberInfoVo.MemberInfoRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationDeleteRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListInfo;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationListRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberReservationRequest;
import com.hotel.reservation.vo.MemberInfoVo.MemberWithdrawRequest;
import com.hotel.reservation.vo.UnMemberInfoVo;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberReservationRequest;
import com.hotel.reservation.vo.UnMemberInfoVo.UnMemberWithdrawRequest;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {

//    @Override
//    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList() {
//        HotelInfoVo.OwnerHotelListResponse result = new HotelInfoVo.OwnerHotelListResponse();
//        List<HotelInfoVo.OwnerHotel> hotel_list = new ArrayList<>(); // 해당 사업자 정보로 소유호텔 리스트 조회
//
//        HotelInfoVo.OwnerHotel hotel_a = new HotelInfoVo.OwnerHotel();
//        HotelInfoVo.OwnerHotel hotel_b = new HotelInfoVo.OwnerHotel();
//
//        hotel_a.setHotel_num(123);
//        hotel_a.setImage("https://aws.bucket/a");
//        hotel_a.setName("신라스테이 강남");
//        hotel_a.setPhone_num("0212345678");
//
//        hotel_b.setHotel_num(456);
//        hotel_b.setImage("https://aws.bucket/b");
//        hotel_b.setName("파라다이스 부산점");
//        hotel_b.setPhone_num("05312345678");
//
//        hotel_list.add(hotel_a);
//        hotel_list.add(hotel_b);
//
//        result.setMessage("호텔 리스트 조회 완료");
//        result.setData(hotel_list);
//
//        return result;
//    }

	@Override
	public com.hotel.reservation.vo.UnMemberInfoVo.UnMemberResponse UnMemberReservationInfo(
			UnMemberReservationRequest unMemberReservationInfo) {

		UnMemberInfoVo.UnMemberResponse result = new UnMemberInfoVo.UnMemberResponse();
		UnMemberInfoVo.UnMemberReservationInfo info = new UnMemberInfoVo.UnMemberReservationInfo();

		info.setReservation_num(unMemberReservationInfo.getReservation_num());
		info.setReservation_name(unMemberReservationInfo.getName());
		info.setReservation_phone(unMemberReservationInfo.getPhone_num());
		info.setName("호텔 델루나");
		info.setRoom_detail_name("스탠다드룸");
		info.setReservation_people(2);
		info.setSt_date("2022-08-28 14:20:00");
		info.setEd_date("2022-08-29 11:00:00");
		info.setReservation_price(new BigDecimal("3000000"));
		info.setReservation_status("0");

		result.setData(info);
		result.setMessage("비회원 예약정보 조회 완료");

		return result;
	}

	@Override
	public CommonResponseVo UnMemberReservationWithdraw(UnMemberWithdrawRequest unMemberWithdrawVo) {
		CommonResponseVo result = new CommonResponseVo();
		// 예약취소 인터페이스 호출 시 사유를 번저 저장하고 최종 취소를 해야하는거 ?
		// db를 두 번 호출하는건데 괜찮은건가?
		if (unMemberWithdrawVo.getReservation_num().equals("")) {
			result.setMessage("예약번호를 입력해주세요.");
			return result;
		}

		result.setMessage("비회원 예약취소 완료");
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
	public CommonResponseVo memberReservation(MemberReservationRequest memberReservationRequest) {
		CommonResponseVo result = new CommonResponseVo();
		if (memberReservationRequest.getReservation_price().equals("")) {
			result.setMessage("예약불가");
			return result;
		}
		result.setMessage("예약완료");
		return result;
	}

	@Override
	public CommonResponseVo MemberReservationWithdraw(MemberWithdrawRequest memberWithdrawVo) {
		CommonResponseVo result = new CommonResponseVo();
		if (memberWithdrawVo.getReservation_num().equals("")) {
			result.setMessage("예약번호 없음");
			return result;
		} else if (memberWithdrawVo.getRoom_detail_num().equals("")) {
			result.setMessage("객실 상세정보 번호 없음");
			return result;
		}

		result.setMessage("삭제완료");
		return result;
	}

	@Override
	public Map<String, Object> MemberReservationList(MemberReservationListRequest memberInfo) {
		MemberInfoVo.MemberReservationListInfo data1 = new MemberInfoVo.MemberReservationListInfo();
		MemberInfoVo.MemberReservationListInfo data2 = new MemberInfoVo.MemberReservationListInfo();
		MemberInfoVo.MemberReservationListInfo data3 = new MemberInfoVo.MemberReservationListInfo();
		
		List<MemberReservationListInfo> result = new ArrayList<>(); 
		
		data1.setReservation_num("000001");
		data1.setRoom_detail_num("000002");
		data1.setReservation_phone("01012345678");
		data1.setReservation_name("홍길동");
		data1.setName("호텔 델루나");
		data1.setRoom_detail_name("디럭스룸");
		data1.setReservation_people(2);
		data1.setReservation_price("300000");
		data1.setSt_date("2022-08-28 14:00:00");
		data1.setEd_date("2022-08-29 11:00:00");
		data1.setReservation_status("0");
		
		data2.setReservation_num("000002");
		data2.setRoom_detail_num("000003");
		data2.setReservation_phone("01012345555");
		data2.setReservation_name("임꺽정");
		data2.setName("호텔 신라 명동");
		data2.setReservation_people(3);
		data2.setReservation_price("200000");
		data2.setRoom_detail_name("스탠다드룸");
		data2.setSt_date("2022-08-29 14:00:00");
		data2.setEd_date("2022-08-30 11:00:00");
		data2.setReservation_status("1");
		
		data3.setReservation_num("000003");
		data3.setRoom_detail_num("000004");
		data3.setReservation_phone("01012345678");
		data3.setReservation_name("이순신");
		data3.setName("호텔 인터네셔널 삼성");
		data3.setReservation_people(2);
		data3.setReservation_price("400000");
		data3.setRoom_detail_name("프리미엄룸");
		data3.setSt_date("2022-08-30 14:00:00");
		data3.setEd_date("2022-08-31 11:00:00");
		data3.setReservation_status("2");
		
		result.add(data1);
		result.add(data2);
		result.add(data3);
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", "OK");
		map.put("message", "");
		map.put("data", result);
		
		return map;
	}

	@Override
	public Map<String, Object> MemberReservationDeleteContent(
			MemberReservationDeleteRequest memberInfoRequest) {
		CommonResponseVo result = new CommonResponseVo();
		MemberInfoVo.MemberReservationDeleteInfo content = new MemberInfoVo.MemberReservationDeleteInfo(); 
		
		content.setContent("왜 그럴 때 있잖아요? 그냥 가기 싫을 때가요.. 그때인거같아요 살려주세요.");
		
		result.setMessage("사유 조회 성공!!");
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("result", "OK");
		map.put("massage", result.getMessage());
		map.put("data", content);
		
		return map;
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
