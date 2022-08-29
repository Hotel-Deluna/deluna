package com.hotel.common.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.CommonVo;

public interface CommonService {
    CommonResponseVo RequestPhoneAuth(CommonVo.PhoneAuthRequest phoneAuthRequest);

    CommonVo.VerifyPhoneAuthResponse VerifyPhoneAuth(CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest);

    CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest);
}
