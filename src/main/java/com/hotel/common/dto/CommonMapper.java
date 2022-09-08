package com.hotel.common.dto;

import com.hotel.common.vo.CommonVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

@Repository
@Mapper
public interface CommonMapper {

    int insertPhoneAuthInfo(CommonVo.PhoneAuthRequest phoneAuthRequest) throws Exception;

    Date verifyPhoneAuth(CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest) throws Exception;

    String getAutoIncrementNext(Map<String, String> data) throws Exception;
}
