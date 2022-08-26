package com.hotel.company.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Override
    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList() {
        HotelInfoVo.OwnerHotelListResponse result = new HotelInfoVo.OwnerHotelListResponse();
        List<HotelInfoVo.OwnerHotel> hotel_list = new ArrayList<>(); // 해당 사업자 정보로 소유호텔 리스트 조회

        HotelInfoVo.OwnerHotel hotel_a = new HotelInfoVo.OwnerHotel();
        HotelInfoVo.OwnerHotel hotel_b = new HotelInfoVo.OwnerHotel();

        hotel_a.setHotel_num(123);
        hotel_a.setImage("https://aws.bucket/a");
        hotel_a.setName("신라스테이 강남");
        hotel_a.setPhone_num("0212345678");

        hotel_b.setHotel_num(456);
        hotel_b.setImage("https://aws.bucket/b");
        hotel_b.setName("파라다이스 부산점");
        hotel_b.setPhone_num("05312345678");

        hotel_list.add(hotel_a);
        hotel_list.add(hotel_b);

        result.setMessage("호텔 리스트 조회 완료");
        result.setData(hotel_list);

        return result;
    }

    @Override
    public CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelRequest) {
        CommonResponseVo result = new CommonResponseVo();
        // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장

        result.setMessage("호텔 등록 완료");
        return result;
    }

    @Override
    public HotelSearchVo.Response SearchBar(HotelSearchVo.SearchBarRequest searchBarVoRequest) {
        //검색어에 일치하는 지역명, 호텔명, 호텔주소 있는지 DB 조회
        HotelSearchVo.Response result = new HotelSearchVo.Response();
        HotelSearchVo.resultData resultData = new HotelSearchVo.resultData();

        List<String> region_list = new ArrayList<>();
        region_list.add("서울");

        List<String> hotel_address_list = new ArrayList<>();
        hotel_address_list.add("노보텔 엠비시티 서울 용산");

        List<String> hotel_name_list = new ArrayList<>();
        hotel_name_list.add("서울신라호텔");
        hotel_name_list.add("밀레니엄 힐튼 서울");

        resultData.setRegion_list(region_list);
        resultData.setHotel_address_list(hotel_address_list);
        resultData.setHotel_name_list(hotel_name_list);

        result.setMessage("검색 완료");
        result.setData(resultData);

        return result;

    }


}
