package com.hotel.company.dto;

import com.hotel.common.vo.CommonVo;
import com.hotel.company.vo.HotelInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Repository
@Mapper
public interface HotelMapper {

    void insertHotelInfo(HotelInfoVo.RegisterHotelRequest registerHotelRequest) throws Exception;

    void insertPeekSeason(HotelDto.PeekSeasonTable peekSeasonTable);

    void insertHotelImage(HotelDto.ImageTable imageTable);
}
