package com.hotel.common.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.CommonVo;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public CommonResponseVo RequestPhoneAuth(CommonVo.PhoneAuthRequest phoneAuthRequest) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("휴대폰 인증번호 전송 완료");

        // AuthUtil CreateAuthNum으로 인증번호 생성. DB에 해당 인증번호 이미 존재하는지 체크 후 진행.
        // 휴대폰은 휴대폰번호 + 인증번호로 확인가능하므로 인증번호 DB존재 체크가 불필요할수 있음. 하지만 이메일은 필요함
        // 인증번호 DB 저장하고 해당 휴대폰번호로 문자 전송. 문자전송기능은 유틸 패키지에서 관리.

        return result;
    }

    @Override
    public CommonVo.VerifyPhoneAuthResponse VerifyPhoneAuth(CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest) {
        CommonVo.VerifyPhoneAuthResponse result = new CommonVo.VerifyPhoneAuthResponse();

        // DB에서 휴대폰번호 + 인증번호로 해당 사용자 휴대폰 번호 맞는지 체크
        // 맞으면 true , 틀리면 false 리턴

        result.setMessage("휴대폰 인증 완료");
        result.setData(true);

        return result;
    }

    @Override
    public CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest) {
        CommonVo.EmailDuplicateCheckResponse result = new CommonVo.EmailDuplicateCheckResponse();

        // 이메일 DB에서 조회 후 이미 있으면 true, 없으면 false 리턴

        result.setMessage("이메일 중복 확인 완료");
        result.setData(false);

        return result;
    }
}
