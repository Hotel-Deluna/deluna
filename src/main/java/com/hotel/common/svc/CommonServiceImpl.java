package com.hotel.common.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.dto.CommonMapper;
import com.hotel.common.vo.CommonEnum;
import com.hotel.common.vo.CommonVo;
import com.hotel.common.vo.JwtTokenDto;
import com.hotel.common.vo.JwtTokenDto.TokenDto;
import com.hotel.company.dto.HotelMapper;
import com.hotel.company.vo.HotelInfoVo;
import com.hotel.company.vo.HotelSearchVo;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.member.dto.MemberRequestDto;
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
import org.springframework.util.CollectionUtils;
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
            String SmsMessage = "[???????????????] ???????????? ???????????? ["+auth_num+"] ??? ????????? ?????????";
            phoneAuthRequest.setPhone_num(aes256Util.encrypt(phone_num));
            phoneAuthRequest.setAuth_num(auth_num);
            int insertResult = commonMapper.insertPhoneAuthInfo(phoneAuthRequest);

            // AuthUtil CreateAuthNum?????? ???????????? ??????. DB??? ?????? ???????????? ?????? ??????????????? ?????? ??? ??????. ????????? ???????????? ?????? ?????????
            // ???????????? ??????????????? + ??????????????? ????????????????????? ???????????? DB?????? ????????? ??????????????? ??????. ????????? ???????????? ?????????
            // ???????????? DB ???????????? ?????? ?????????????????? ?????? ??????. ????????????????????? ?????? ??????????????? ??????.

            if (insertResult > 0){
                smsResponse = smsUtil.sendSMS(phone_num, SmsMessage);
                if("202".equals(smsResponse.getStatusCode())){
                    result.setMessage("????????? ???????????? ?????? ??????");
                }
                else {
                    result.setResult("ERROR");
                    result.setMessage("????????????");
                }
            }
            else {
                result.setResult("ERROR");
                result.setMessage("DB ??????");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public CommonVo.VerifyPhoneAuthResponse VerifyPhoneAuth(CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest) {
        // DB?????? ??????????????? + ??????????????? ?????? ????????? ????????? ?????? ????????? ??????
        // ????????? true , ????????? false ??????
        CommonVo.VerifyPhoneAuthResponse result = new CommonVo.VerifyPhoneAuthResponse();
        boolean is_verify = false;

        try{
            // ???????????? ?????????????????? DB??????
            verifyPhoneAuthRequest.setPhone_num(aes256Util.encrypt(verifyPhoneAuthRequest.getPhone_num()));
            Date authNumInsertTime = commonMapper.verifyPhoneAuth(verifyPhoneAuthRequest);

            // DB ????????? ????????? ???????????? ?????? ?????? 3??? ????????? ????????????
            long diffSec = DateUtil.timeDiffSec(authNumInsertTime);

            if(authNumInsertTime != null && diffSec < 180){
                is_verify = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        result.setMessage("????????? ?????? ??????");
        result.setData(is_verify);

        return result;
    }

    @Override
    public CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest) {
        CommonVo.EmailDuplicateCheckResponse result = new CommonVo.EmailDuplicateCheckResponse();
        boolean isduplication = true;
        Integer check = 0;

        try{
            // ?????? ????????? ????????????
            if(emailDuplicateCheckRequest.getRole() == 1){
                check = commonMapper.checkDuplicateMemberEmail(emailDuplicateCheckRequest.getEmail());
            }
            // ????????? ????????? ????????????
            else if(emailDuplicateCheckRequest.getRole() == 2){
                check = commonMapper.checkDuplicateOwnerEmail(emailDuplicateCheckRequest.getEmail());
            }else {
                log.info("Role ?????? ?????????????????????.");
                result.setResult("ERROR");
                result.setMessage("VAL-0001");
                return result;
            }

            // ????????? ????????? ????????? false
            if(check == null){
                isduplication = false;
            }

        }catch (Exception e){
            e.printStackTrace();
            result.setResult("ERROR");
            result.setMessage("BACK-0001");
            return result;
        }

        result.setMessage("????????? ?????? ?????? ??????");
        result.setData(isduplication);

        return result;
    }

    @Override
    public CommonVo.CommonCodeResponse HotelTagCode() {
        CommonVo.CommonCodeResponse result = new CommonVo.CommonCodeResponse();
        List<CommonVo.CommonCode> codeList = new ArrayList<>();

        for (CommonEnum.HotelTags enumList : CommonEnum.HotelTags.values() ) {
            codeList.add(new CommonVo.CommonCode(enumList.getCode(), enumList.getName()));
        }

        result.setMessage("?????? ?????? ?????? ?????? ??????");
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

        result.setMessage("?????? ?????? ?????? ??????");
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

        result.setMessage("?????? ?????? ?????? ??????");
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

        result.setMessage("?????? ?????? ?????? ?????? ??????");
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

        result.setMessage("????????? ?????? ?????? ?????? ??????");
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

        result.setMessage("?????? ?????? ?????? ??????");
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

        result.setMessage("????????? ?????? ?????? ??????");
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

        String encryptedText = encryptor.encrypt(text); // ?????????
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
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*??? ????????? ?????? ???*/
            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(thisYear.substring(0,4), "UTF-8")); /*??????*/
            urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*??????*/
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

            // ????????? response ????????? json ???????????? ?????? -> ????????? ????????? ??????
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(sb.toString());
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject response = (JSONObject) jsonObj.get("response");

            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            // ????????? ?????? ??????????????? ??????
            commonMapper.deleteHoliday();

            // ????????? ?????? ??????
            for (Object o : item) {
                CommonVo.Holiday holiday = new CommonVo.Holiday();
                holiday.setHoliday_name((String) ((JSONObject) o).get("dateName"));
                holiday.setHoliday_date(dateUtil.stringToDateForHolidayCrawling(String.valueOf((Long) ((JSONObject) o).get("locdate"))));
                commonMapper.insertHoliday(holiday);
            }

            log.info("????????? ?????? ?????? ??????");

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
            // ????????? ?????? ??????
            List<HotelSearchVo.TouristSpotInfo> getTouristSpotRegion = hotelMapper.selectTouristSpotList();

            // ??? ???????????? ?????? ?????? ?????????
            for(HotelSearchVo.TouristSpotInfo touristSpotInfo : getTouristSpotRegion){
                int count = 0;
                count = commonMapper.countHotelTouristSpot(touristSpotInfo);
                touristSpotInfo.setHotel_count(count);
                // ????????????
                commonMapper.updatecountHotelTouristSpot(touristSpotInfo);
            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return "????????? ???????????? ????????????";
    }

    @Override
    public String InsertTouristSpotImage(CommonVo.InsertTouristSpotImageRequest insertTouristSpotImageRequest) {

        try{
            int tourist_spot_num = insertTouristSpotImageRequest.getTourist_spot_num();
            int select_type = CommonEnum.ImageType.TOURIST_SPOT.getCode();
            String userPk = "0"; // ?????????

            List<MultipartFile> multipartFileList = new ArrayList<>();
            multipartFileList.add(insertTouristSpotImageRequest.getImage());

            // DB??? ????????? ?????? ????????? ????????? ?????? ??????
            imageUtil.deleteImage(select_type, tourist_spot_num);

            // ????????? ?????? ?????? & DB??? ?????? ??????
            imageUtil.insertImage(multipartFileList, select_type, tourist_spot_num, userPk);

        }catch (Exception e){
            e.printStackTrace();
        }

        return "????????? ????????? ?????? ??????";
    }

    @Override
    public String InsertTouristSpot(CommonVo.InsertTouristSpotRequest insertTouristSpotRequest) {
        log.info("????????? ?????? ?????? ??????");

        try{
            int tourist_spot_num = dbUtil.getAutoIncrementNext(CommonEnum.TableName.D_TOURIST_SPOT.getName());
            int select_type = CommonEnum.ImageType.TOURIST_SPOT.getCode();
            String userPk = "0"; // ?????????

            List<MultipartFile> multipartFileList = new ArrayList<>();

            // ????????? ?????? ????????? ????????? ??????, ????????? ?????? ????????? ????????? ??????
            if(insertTouristSpotRequest.getImage() != null){
                multipartFileList.add(insertTouristSpotRequest.getImage());

                // DB??? ????????? ?????? ????????? ????????? ?????? ??????
                imageUtil.deleteImage(select_type, tourist_spot_num);

                // ????????? ?????? ?????? & DB??? ?????? ??????
                imageUtil.insertImage(multipartFileList, select_type, tourist_spot_num, userPk);
            }

            if(insertTouristSpotRequest.getName() != null){
                // ?????????????????? ????????????
                Integer checkDuplicationTouristSpot = commonMapper.checkDuplicationTouristSpot(insertTouristSpotRequest.getName());

                if(checkDuplicationTouristSpot != null){
                    return "????????? ????????????";
                }

                // ????????? ?????? ??????
                commonMapper.insertTouristSpot(insertTouristSpotRequest.getName());
            }
            else {
                return "??????????????? ????????????????????????.";
            }

        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }

        return "????????? ?????? ?????? ??????";
    }

	@Override
	public void updateReservationEndDate() {
		log.info("?????? ????????? ???????????? ??????");
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

        return "?????? ?????? ??????";
    }

	@Override
	public Map<String, Object> TokenReCreate(String email) {
		MemberRequestDto memberRequestDto = new MemberRequestDto();
		memberRequestDto.setEmail(email);
		Map<String, Object> map = new HashMap<>();
		TokenDto dto = new TokenDto();
		String data = commonMapper.selectMemberInfo(email);

		if(data == null) {
			Integer num = commonMapper.selectBusinessMemberInfo(email);
			if(num.toString() == null) {
				map.put("result", "ERR");
				map.put("reason", "memberInfo Not Found");
				return map;
			}
			UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
			dto = jwtTokenProvider.generateMemberTokenDto(authenticationToken, "ROLE_OWNER", num);
		}else {
			UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
			dto = jwtTokenProvider.generateMemberTokenDto(authenticationToken, "ROLE_MEMBER");
		}
		String id = memberRequestDto.getEmail();
		Integer role = memberRequestDto.getRole();

		map.put("Authorization", dto.getAccessToken());
		map.put("RefreshToken", dto.getRefreshToken());

		return map;
	}

    @Override
    public String deleteRoom() {

        log.info("??????, ?????? ?????? ?????? ??????");

        try{
            // ?????? ?????? ???????????? ???????????? ??????, ?????? ??????
            List<CommonVo.deleteRoomInfo> deleteRoomInfoList = commonMapper.deleteRoomInfo();
            List<CommonVo.deleteRoomDetailInfo> deleteRoomDetailInfoList = commonMapper.deleteRoomDetailInfo();

            if(!CollectionUtils.isEmpty(deleteRoomInfoList)){
                log.info("????????? ?????? ??? : " + deleteRoomInfoList.size());
                List<Integer> room_num_list = deleteRoomInfoList
                        .stream()
                        .map(CommonVo.deleteRoomInfo::getRoom_num)
                        .toList();

                // ?????? ????????????
                hotelMapper.deleteRoom(room_num_list);

                // ?????? ?????????????????? ?????? ??????
                for(CommonVo.deleteRoomInfo deleteRoomInfo : deleteRoomInfoList){
                    HotelInfoVo.DeleteTable insertRoomDelete = new HotelInfoVo.DeleteTable();
                    insertRoomDelete.setPk(deleteRoomInfo.getRoom_num());
                    insertRoomDelete.setInsert_user(deleteRoomInfo.getInsert_user());
                    hotelMapper.insertRoomDelete(insertRoomDelete);
                }
            }

            if(!CollectionUtils.isEmpty(deleteRoomDetailInfoList)){
                log.info("????????? ?????? ??? : " + deleteRoomDetailInfoList.size());
                List<Integer> room_detail_num_list = deleteRoomDetailInfoList
                        .stream()
                        .map(CommonVo.deleteRoomDetailInfo::getRoom_detail_num)
                        .toList();
                // ?????? ????????????
                hotelMapper.deleteRoomDetail(room_detail_num_list);

                // ?????? ?????????????????? ?????? ??????
                for(CommonVo.deleteRoomDetailInfo deleteRoomDetailInfo : deleteRoomDetailInfoList){
                    HotelInfoVo.DeleteTable insertRoomDetailDelete = new HotelInfoVo.DeleteTable();
                    insertRoomDetailDelete.setPk(deleteRoomDetailInfo.getRoom_detail_num());
                    insertRoomDetailDelete.setInsert_user(deleteRoomDetailInfo.getInsert_user());
                    hotelMapper.insertRoomDetailDelete(insertRoomDetailDelete);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("??????, ?????? ?????? ??????");
            return "?????? ?????? ?????? ??????";
        }

        return "??????, ?????? ?????? ?????? ??????";
    }

}
