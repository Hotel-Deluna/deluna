package com.hotel.common.dto;

import com.hotel.common.vo.CommonVo;
import com.hotel.company.vo.HotelSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CommonMapper {

    int insertPhoneAuthInfo(CommonVo.PhoneAuthRequest phoneAuthRequest) throws Exception;

    Date verifyPhoneAuth(CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest) throws Exception;

    int getAutoIncrementNext(Map<String, String> data) throws Exception;

    void insertHoliday(CommonVo.Holiday holiday);

    void deleteHoliday();

    int countHotelTouristSpot(HotelSearchVo.TouristSpotInfo touristSpotInfo) throws Exception;

    void updatecountHotelTouristSpot(HotelSearchVo.TouristSpotInfo touristSpotInfo);
}
