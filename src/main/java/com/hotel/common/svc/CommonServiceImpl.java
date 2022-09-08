package com.hotel.common.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.dto.CommonMapper;
import com.hotel.common.vo.CommonEnum;
import com.hotel.common.vo.CommonVo;
import com.hotel.util.*;
import com.hotel.util.vo.UtilVo;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class CommonServiceImpl implements CommonService {

    @Value("${jasypt.encryptor.password}")
    private String encryptKey;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    SMSUtil smsUtil;

    @Autowired
    AES256Util aes256Util;

    @Autowired
    MailUtil mailUtil;

    @Override
    public CommonResponseVo RequestPhoneAuth(CommonVo.PhoneAuthRequest phoneAuthRequest) {
        CommonResponseVo result = new CommonResponseVo();
        UtilVo.SmsResponse smsResponse = new UtilVo.SmsResponse();
        String phone_num = phoneAuthRequest.getPhone_num();
        try{
            String auth_num = authUtil.CreateAuthNum();
            String SmsMessage = "[호텔델루나] 본인확인 인증번호 ["+auth_num+"] 를 입력해 주세요";
            phoneAuthRequest.setPhone_num(aes256Util.encrypt(phone_num));
            phoneAuthRequest.setAuth_num(auth_num);
            int insertResult = commonMapper.insertPhoneAuthInfo(phoneAuthRequest);

            // AuthUtil CreateAuthNum으로 인증번호 생성. DB에 해당 인증번호 이미 존재하는지 체크 후 진행. 있다면 인증번호 생성 재시도
            // 휴대폰은 휴대폰번호 + 인증번호로 확인가능하므로 인증번호 DB존재 체크가 불필요할수 있음. 하지만 이메일은 필요함
            // 인증번호 DB 저장하고 해당 휴대폰번호로 문자 전송. 문자전송기능은 유틸 패키지에서 관리.

            if (insertResult > 0){
                smsResponse = smsUtil.sendSMS(phone_num, SmsMessage);
                if("202".equals(smsResponse.getStatusCode())){
                    result.setMessage("휴대폰 인증번호 전송 완료");
                }
                else {
                    result.setResult("ERROR");
                    result.setMessage("전송오류");
                }
            }
            else {
                result.setResult("ERROR");
                result.setMessage("DB 에러");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public CommonVo.VerifyPhoneAuthResponse VerifyPhoneAuth(CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest) {
        // DB에서 휴대폰번호 + 인증번호로 해당 사용자 휴대폰 번호 맞는지 체크
        // 맞으면 true , 틀리면 false 리턴
        CommonVo.VerifyPhoneAuthResponse result = new CommonVo.VerifyPhoneAuthResponse();
        boolean is_verify = false;

        try{
            // 암호화된 휴대폰번호로 DB조회
            verifyPhoneAuthRequest.setPhone_num(aes256Util.encrypt(verifyPhoneAuthRequest.getPhone_num()));
            Date authNumInsertTime = commonMapper.verifyPhoneAuth(verifyPhoneAuthRequest);

            // DB 저장된 시간과 현재시간 차이 계산 3분 넘으면 실패처리
            long diffSec = DateUtil.timeDiffSec(authNumInsertTime);

            if(authNumInsertTime != null && diffSec < 180){
                is_verify = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        result.setMessage("휴대폰 인증 완료");
        result.setData(is_verify);

        return result;
    }

    @Override
    public CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest) {
        CommonVo.EmailDuplicateCheckResponse result = new CommonVo.EmailDuplicateCheckResponse();

        result.setMessage("이메일 중복 확인 완료");
        result.setData(false);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse HotelTagCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.HotelTags enumList : CommonEnum.HotelTags.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("호텔 태그 코드 조회 완료");
        result.setData(codeList);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse UserRoleCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.UserRole enumList : CommonEnum.UserRole.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("회원 코드 조회 완료");
        result.setData(codeList);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse RoomTagCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.RoomTags enumList : CommonEnum.RoomTags.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("객실 코드 조회 완료");
        result.setData(codeList);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse MemberDeleteReasonCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.MemberDeleteReason enumList : CommonEnum.MemberDeleteReason.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("회원 삭제 코드 조회 완료");
        result.setData(codeList);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse OwnerDeleteReasonCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.OwnerDeleteReason enumList : CommonEnum.OwnerDeleteReason.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("사업자 삭제 코드 조회 완료");
        result.setData(codeList);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse RegionCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.Region enumList : CommonEnum.Region.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("지역 코드 조회 완료");
        result.setData(codeList);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse TouristSpotCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.TouristSpot enumList : CommonEnum.TouristSpot.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("여행지 코드 조회 완료");
        result.setData(codeList);

        return result;
    }

    @Override
    public String EncryptConfig(String text) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(1);
        encryptor.setPassword(encryptKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES");

        String encryptedText = encryptor.encrypt(text); // 암호화
        return "ENC("+encryptedText+")";
    }

    @Override
    public String DecryptConfig(String text) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(1);
        encryptor.setPassword(encryptKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES");

        String decrypt = encryptor.decrypt(text); // 복호화
        return decrypt;
    }
}
