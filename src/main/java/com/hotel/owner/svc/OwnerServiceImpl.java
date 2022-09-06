package com.hotel.owner.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.CommonEnum;
import com.hotel.owner.dto.OwnerMapper;
import com.hotel.util.AES256Util;
import com.hotel.util.DateUtil;
import com.hotel.owner.vo.*;
import com.hotel.util.SHA512Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    OwnerMapper ownerMapper;

    @Autowired
    SHA512Util sha512Util;

    @Autowired
    AES256Util aes256Util;

    @Override
    public CommonResponseVo OwnerSignUp(OwnerVo.OwnerSignUpRequest ownerVo) {
        CommonResponseVo result = new CommonResponseVo();

        try{
            // 중복가입 조회 - 사업자번호기준
            String checkDuplicationOwner = ownerMapper.checkDuplicationOwner(ownerVo.getBusiness_num());
            if(checkDuplicationOwner != null){
                result.setResult("ERROR");
                result.setMessage("중복 회원 입니다");
                return result;
            }

            String nextNum = ownerMapper.getOwnerTableAutoIncrementNext(); // 다음 생성될 Auto Increment 숫자 조회
            String ownerCode = String.valueOf(CommonEnum.UserRole.OWNER.getCode()); // 사업자 코드

            ownerVo.setPassword(sha512Util.encryptSHA512(ownerVo.getPassword())); //비밀번호 암호화
            ownerVo.setPhone_num(aes256Util.encrypt(ownerVo.getPhone_num())); //핸드폰번호 암호화
            ownerVo.setInsert_user(ownerCode+nextNum);
            ownerVo.setUpdate_user(ownerCode+nextNum);

            ownerMapper.ownerSignUp(ownerVo);
        }catch (Exception e){
            e.printStackTrace();
        }

        result.setMessage("사업자 회원 가입 완료");

        return result;
    }

    @Override
    public CommonResponseVo OwnerEditInfo(OwnerVo.OwnerInfo ownerInfoVo) {
        CommonResponseVo result = new CommonResponseVo();

        // 토큰 파싱해서 사업자 정보 조회

        result.setMessage("사업자 정보 수정 완료");
        return result;
    }

    @Override
    public OwnerVo.OwnerInfoResponse ViewOwnerInfo() {
//        ApiResponseVo result = new ApiResponseVo();
        OwnerVo.OwnerInfoResponse result = new OwnerVo.OwnerInfoResponse();

        try{
            // 토큰 파싱해서 사업자 정보 조회

            OwnerVo.OwnerInfo ownerInfoVo = new OwnerVo.OwnerInfo();

            ownerInfoVo.setEmail("abc@hotel.com");
            ownerInfoVo.setName("홍길동");
            ownerInfoVo.setPhone_num("01098765432");
            ownerInfoVo.setBusiness_num("0123456789");
            ownerInfoVo.setOpening_day(DateUtil.stringToDate("2022/07/05"));

            result.setMessage("사업자 정보 조회 완료");
            result.setData(ownerInfoVo);
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo OwnerWithdraw(OwnerVo.OwnerWithdrawRequest ownerWithdrawRequest) {
        CommonResponseVo result = new CommonResponseVo();
        // 사업자 회원 탈퇴처리
        // ownerWithdrawReasonVo 사유 DB 저장
        result.setMessage("사업자 회원 탈퇴 완료");

        return result;
    }
}
