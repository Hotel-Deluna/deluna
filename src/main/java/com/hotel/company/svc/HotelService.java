package com.hotel.company.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;

public interface HotelService {

    HotelInfoVo.OwnerHotelListResponse OwnerHotelList();

    CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelParamVo);

    HotelSearchVo.Response SearchBar(HotelSearchVo.SearchBarRequest searchBarVoSearchBarRequest);
}
