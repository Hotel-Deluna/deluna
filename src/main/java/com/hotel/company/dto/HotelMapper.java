package com.hotel.company.dto;

import com.hotel.company.vo.HotelInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HotelMapper {

    void insertHotelInfo(HotelInfoVo.RegisterHotelRequest registerHotelRequest) throws Exception;

    void insertHotelTags(HotelInfoVo.Tags tags) throws Exception;

    void updateHotelInfo(HotelInfoVo.EditInfoHotelRequest editInfoHotelRequest) throws Exception;

    void insertPeekSeason(HotelDto.PeekSeasonTable peekSeasonTable) throws Exception;

    void insertImage(HotelDto.ImageTable imageTable) throws Exception;

    void deleteHotelImage(HotelDto.ImageTable imageTable) throws Exception;

    void softDeletePeekSeason(int hotel_num) throws Exception;

    void deleteHotelTags(int hotel_num) throws Exception;

    void insertRoomInfo(HotelInfoVo.RegisterRoomRequest registerRoomRequest) throws Exception;

    void insertRoomTags(HotelInfoVo.Tags tags) throws Exception;

    void updateHolidayPriceStatus(HotelInfoVo.RegisterRoomRequest registerRoomRequest) throws Exception;

    void insertRoomDetailInfo(HotelInfoVo.RegisterRoomDetailRequest registerRoomDetailRequest) throws Exception;
}
