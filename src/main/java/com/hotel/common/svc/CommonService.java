package com.hotel.common.svc;

import java.util.Map;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.CommonVo;

public interface CommonService {
    CommonResponseVo RequestPhoneAuth(CommonVo.PhoneAuthRequest phoneAuthRequest);

    CommonVo.VerifyPhoneAuthResponse VerifyPhoneAuth(CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest);

    CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest);

    CommonVo.CommonCodeResponse HotelTagCode();

    CommonVo.CommonCodeResponse UserRoleCode();

    CommonVo.CommonCodeResponse RoomTagCode();

    CommonVo.CommonCodeResponse MemberDeleteReasonCode();

    CommonVo.CommonCodeResponse OwnerDeleteReasonCode();

    CommonVo.CommonCodeResponse RegionCode();

    CommonVo.CommonCodeResponse TouristSpotCode();

    String EncryptConfig(String text);

    String HolidayCrawling();

    String CreateToken(int user_num, int user_role);

    String SaveTouristSpotHotelCount();

    String InsertTouristSpotImage(CommonVo.InsertTouristSpotImageRequest insertTouristSpotImageRequest);

    String InsertTouristSpot(CommonVo.InsertTouristSpotRequest insertTouristSpotRequest);

	  void updateReservationEndDate();
   
	  String MailTest(String text, String to);

	  Map<String, Object> TokenReCreate(String email);

    String deleteRoom();

}
