package com.hotel.common.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.dto.CommonMapper;
import com.hotel.common.vo.CommonEnum;
import com.hotel.common.vo.CommonVo;
import com.hotel.common.vo.JwtTokenDto;
import com.hotel.company.dto.HotelMapper;
import com.hotel.company.vo.HotelSearchVo;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.util.*;
import com.hotel.util.vo.UtilVo;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

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

    @Autowired
    DateUtil dateUtil;

    @Value("${odcloud.serviceKey}")
    String odcloudServiceKey;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    HotelMapper hotelMapper;

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    DBUtil dbUtil;

    @Value("${spring.mail.username}")
    String sender;

    @Autowired
    JavaMailSender javaMailSender;

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
    @Transactional
    public String HolidayCrawling() {
        String result = "";
        try {
            Date now = new  Date();
            String thisYear = dateUtil.dateToString(now);

            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+odcloudServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(thisYear.substring(0,4), "UTF-8")); /*연도*/
            urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*연도*/
            URL url = new URL(urlBuilder.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            result = sb.toString();

            // 받아온 response 데이터 json 형식으로 파싱 -> 필요한 결과값 추출
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(sb.toString());
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject response = (JSONObject) jsonObj.get("response");

            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            // 저장전 기존 공휴일정보 삭제
            commonMapper.deleteHoliday();

            // 공휴일 정보 저장
            for (Object o : item) {
                CommonVo.Holiday holiday = new CommonVo.Holiday();
                holiday.setHoliday_name((String) ((JSONObject) o).get("dateName"));
                holiday.setHoliday_date(dateUtil.stringToDateForHolidayCrawling(String.valueOf((Long) ((JSONObject) o).get("locdate"))));
                commonMapper.insertHoliday(holiday);
            }

            log.info("공휴일 정보 저장 완료");

        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String CreateToken(int user_num, int user_role) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("test","test1");
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("id", user_num);
        claimMap.put("role", user_role);

        authenticationToken.setDetails(claimMap);

        JwtTokenDto.TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authenticationToken);

        return tokenDto.getAccessToken();
    }

    @Override
    public String SaveTouristSpotHotelCount() {

        try{
            // 여행지 정보 조회
            List<HotelSearchVo.TouristSpotInfo> getTouristSpotRegion = hotelMapper.selectTouristSpotList();

            // 각 여행지별 호텔 갯수 카운트
            for(HotelSearchVo.TouristSpotInfo touristSpotInfo : getTouristSpotRegion){
                int count = 0;
                count = commonMapper.countHotelTouristSpot(touristSpotInfo);
                touristSpotInfo.setHotel_count(count);
                // 정보저장
                commonMapper.updatecountHotelTouristSpot(touristSpotInfo);
            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return "여행지 호텔갯수 저장완료";
    }

    @Override
    public String InsertTouristSpotImage(CommonVo.InsertTouristSpotImageRequest insertTouristSpotImageRequest) {

        try{
            int tourist_spot_num = insertTouristSpotImageRequest.getTourist_spot_num();
            int select_type = CommonEnum.ImageType.TOURIST_SPOT.getCode();
            String userPk = "0"; // 관리자

            List<MultipartFile> multipartFileList = new ArrayList<>();
            multipartFileList.add(insertTouristSpotImageRequest.getImage());

            // DB에 저장된 해당 여행지 이미지 모두 삭제
            imageUtil.deleteImage(select_type, tourist_spot_num);

            // 이미지 파일 저장 & DB에 정보 저장
            imageUtil.insertImage(multipartFileList, select_type, tourist_spot_num, userPk);

        }catch (Exception e){
            e.printStackTrace();
        }

        return "여행지 이미지 등록 완료";
    }

    @Override
    public String InsertTouristSpot(CommonVo.InsertTouristSpotRequest insertTouristSpotRequest) {
        log.info("여행지 정보 저장 시작");

        try{
            int tourist_spot_num = dbUtil.getAutoIncrementNext(CommonEnum.TableName.D_TOURIST_SPOT.getName());
            int select_type = CommonEnum.ImageType.TOURIST_SPOT.getCode();
            String userPk = "0"; // 관리자

            List<MultipartFile> multipartFileList = new ArrayList<>();

            // 이미지 정보 있으면 이미지 등록, 아니면 그냥 여행지 정보만 등록
            if(insertTouristSpotRequest.getImage() != null){
                multipartFileList.add(insertTouristSpotRequest.getImage());

                // DB에 저장된 해당 여행지 이미지 모두 삭제
                imageUtil.deleteImage(select_type, tourist_spot_num);

                // 이미지 파일 저장 & DB에 정보 저장
                imageUtil.insertImage(multipartFileList, select_type, tourist_spot_num, userPk);
            }

            if(insertTouristSpotRequest.getName() != null){
                // 여행지 정보 등록
                commonMapper.insertTouristSpot(insertTouristSpotRequest.getName());
            }
            else {
                return "여행지명이 존재하지않습니다.";
            }

        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }

        return "여행지 정보 등록 완료";
    }

	@Override
	public void updateReservationEndDate() {
		log.info("예약 만요일 업데이트 시작");
		commonMapper.updateReservationEndDate();
	}
    @Override
    public String MailTest(String text, String to) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(sender);
        message.setSubject("Mail Test");
        message.setText(text);
        javaMailSender.send(message);

        return "메일 전송 완료";
    }
}
