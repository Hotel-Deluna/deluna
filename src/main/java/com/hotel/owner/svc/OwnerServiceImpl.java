package com.hotel.owner.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.CommonEnum;
import com.hotel.common.vo.JwtTokenDto;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.owner.dto.OwnerMapper;
import com.hotel.util.AES256Util;
import com.hotel.util.DBUtil;
import com.hotel.owner.vo.*;
import com.hotel.util.DateUtil;
import com.hotel.util.SHA512Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

@Service
@Slf4j
@Transactional
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    OwnerMapper ownerMapper;

    @Autowired
    DBUtil dbUtil;

    @Autowired
    SHA512Util sha512Util;

    @Autowired
    AES256Util aes256Util;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Value("${odcloud.serviceKey}")
    String odcloudServiceKey;

    @Value("{aes.256.key}")
    String aesKey;

    @Override
    @Transactional
    public CommonResponseVo OwnerSignUp(OwnerVo.OwnerSignUpRequest ownerVo) {
        CommonResponseVo result = new CommonResponseVo();

        try{
            // 중복가입 조회 - 사업자번호
            if(checkDuplicationOwner(ownerVo.getBusiness_num())){
                result.setResult("ERROR");
                result.setMessage("DUP-0002");
                return result;
            }

            // 중복가입 조회 - 이메일
            if(checkDuplicationEmail(ownerVo.getEmail())){
                result.setResult("ERROR");
                result.setMessage("DUP-0001");
                return result;
            }

            int nextNum = dbUtil.getAutoIncrementNext(CommonEnum.TableName.D_BUSINESS_MEMBER.getName()); // 다음 생성될 Auto Increment 숫자 조회
            String ownerCode = String.valueOf(CommonEnum.UserRole.OWNER.getCode()); // 사업자 코드

            ownerVo.setPassword(sha512Util.encryptSHA512(ownerVo.getPassword())); //비밀번호 암호화
            ownerVo.setPhone_num(aes256Util.encrypt(ownerVo.getPhone_num())); //핸드폰번호 암호화
            ownerVo.setInsert_user(ownerCode+nextNum);
            ownerVo.setUpdate_user(ownerCode+nextNum);


            ownerMapper.ownerSignUp(ownerVo);
            result.setMessage("사업자 회원 가입 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    @Transactional
    public CommonResponseVo OwnerEditInfo(OwnerVo.OwnerInfo ownerInfoVo, String jwt) {
        CommonResponseVo result = new CommonResponseVo();
        try{
            // 토큰 파싱해서 사업자 정보 확인
            int business_user_num = getPk(jwt);
            OwnerVo.OwnerInfo currentOwnerInfo = ownerMapper.selectOwnerInfo(business_user_num);
            String editBusinessNum = ownerInfoVo.getBusiness_num();

            if(currentOwnerInfo == null){
                result.setResult("ERROR");
                result.setMessage("해당 사업자에 대한 정보가 없습니다");
                return result;
            }

            String currentBusinessNum = currentOwnerInfo.getBusiness_num();

            // 중복가입 방지
            // 현재 내 사업자 번호로 사업자번호가 들어오면 사업자 번호 변경안한것. 중복체크 안함 : 그대로 업데이트
            // 만약 내 사업자 번호가 아닌 다른번호로 변경하려는데 중복이면 체크해서 걸러냄

            if(!editBusinessNum.equals(currentBusinessNum)){
                // 중복가입 조회 - 사업자번호기준
                if(checkDuplicationOwner(editBusinessNum)){
                    result.setResult("ERROR");
                    result.setMessage("동일한 사업자 번호가 이미 존재합니다");
                    return result;
                }
            }

            ownerInfoVo.setBusiness_user_num(business_user_num);
            ownerInfoVo.setPhone_num(aes256Util.encrypt(ownerInfoVo.getPhone_num()));
            ownerInfoVo.setUpdate_user(CommonEnum.UserRole.OWNER.getCode()+Integer.toString(business_user_num));
            ownerMapper.ownerEditInfo(ownerInfoVo);

            result.setMessage("사업자 정보 수정 완료");

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public OwnerVo.OwnerInfoResponse ViewOwnerInfo(String jwt) {
        OwnerVo.OwnerInfoResponse result = new OwnerVo.OwnerInfoResponse();

        try{
            // 토큰 파싱해서 사업자 정보 확인
            int business_user_num = getPk(jwt);
            OwnerVo.OwnerInfo ownerInfo = ownerMapper.selectOwnerInfo(business_user_num);

            if(ownerInfo == null){
                result.setData(null);
                result.setMessage("사업자 정보 조회 완료");
                return result;
            }
            ownerInfo.setPhone_num(aes256Util.decrypt(ownerInfo.getPhone_num())); // 휴대폰번호 복호화

            result.setData(ownerInfo);
            result.setMessage("사업자 정보 조회 완료");

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    @Transactional
    public CommonResponseVo OwnerWithdraw(OwnerVo.OwnerWithdrawRequest ownerWithdrawRequest, String jwt) {
        CommonResponseVo result = new CommonResponseVo();
        try{

            int business_user_num = getPk(jwt);
            int isDeleteCnt = 0;
            int insertReasonCnt = 0;

            // 소프트 삭제 처리
            isDeleteCnt = ownerMapper.ownerWithdraw(business_user_num);

            if(isDeleteCnt <=0 ){
                result.setResult("ERROR");
                result.setMessage("해당 사업자가 존재하지 않습니다");
                return result;
            }

            // 사유 코드 저장
            ownerWithdrawRequest.setBusiness_user_num(business_user_num);
            ownerWithdrawRequest.setInsert_user(CommonEnum.UserRole.OWNER.getCode()+Integer.toString(business_user_num));
            insertReasonCnt = ownerMapper.insertOwnerWithdrawReason(ownerWithdrawRequest);

            result.setMessage("사업자 회원 탈퇴 완료");

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public OwnerVo.OwnerVerifyResponse OwnerVerify(OwnerVo.OwnerVerifyRequest ownerVerifyRequest) {
        log.info("사업자 번호 진위확인 START");
        OwnerVo.OwnerVerifyResponse result = new OwnerVo.OwnerVerifyResponse();
        boolean OwnerVerify = false;

        try{
            URL url = new URL("https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey="+odcloudServiceKey);

            JSONObject postParams = new JSONObject();
            List<Map<String, String>> businesses = new ArrayList<>();

            // Date 형식인 개업일 사업자 진위확인 API 형식에 맞춰 형변환
            String start_dt = DateUtil.dateToStringForAPI(ownerVerifyRequest.getOpening_day());

            // 사업자 진위확인 API 파라미터 형식에 맞춰 데이터 가공
            Map<String, String> ownerInfo = new HashMap<>();
            ownerInfo.put("b_no", ownerVerifyRequest.getBusiness_num());
            ownerInfo.put("start_dt", start_dt);
            ownerInfo.put("p_nm", ownerVerifyRequest.getName());
            businesses.add(ownerInfo);

            postParams.put("businesses", businesses);

            // POST 전송
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(postParams.toJSONString()); //json 형식의 message 전달
            wr.flush();

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                log.info(sb.toString());

                // 받아온 response 데이터 json 형식으로 파싱 -> 필요한 결과값 추출
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(sb.toString());
                JSONObject jsonObj = (JSONObject) obj;
                String status_code = (String) ((JSONObject) obj).get("status_code");

                if(!"OK".equals(status_code)){
                    result.setResult("ERROR");
                    result.setMessage("외부 API 호출 에러");
                    return result;
                }

                JSONArray jsonArrayData = (JSONArray) jsonObj.get("data");
                String valid = (String) ((JSONObject) jsonArrayData.get(0)).get("valid");
                // valid 01 : valid 02: Invalid
                if("01".equals(valid)){
                    OwnerVerify = true;
                }
                log.info("사업자 번호 진위확인 결과 : " + valid);
            } else {
               log.info(con.getResponseMessage());
            }

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }
        result.setMessage("사업자 번호 진위여부 확인 완료");
        result.setData(OwnerVerify);
        return result;
    }

    /**
     * 중복가입 조회 - 사업자번호, 이메일 중복조회. DB에 이미 있으면 True 리턴
     * @param business_num : 사업자번호
     * @return
     */
    private boolean checkDuplicationOwner(String business_num){
        boolean result = false;

        String checkDuplicationOwner = ownerMapper.checkDuplicationOwner(business_num);
        if(checkDuplicationOwner != null){
            result = true;
        }

        return result;
    }

    private boolean checkDuplicationEmail(String email){
        boolean result = false;

        String checkDuplicationOwner = ownerMapper.checkDuplicationEmail(email);
        if(checkDuplicationOwner != null){
            result = true;
        }

        return result;
    }

    /**
     * JWT Token에서 PK조회
     * @param jwtToken
     * @return
     * @throws Exception
     */
    private int getPk(String jwtToken) throws Exception{
        JwtTokenDto.PayLoadDto payloadData = jwtTokenProvider.getPayload(jwtToken);
        return payloadData.getId();
    }

    private CommonResponseVo ErrorResult (CommonResponseVo result){
        result.setResult("ERROR");
        result.setMessage("BACK-0001");
        return result;
    }
}
