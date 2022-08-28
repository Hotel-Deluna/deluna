package com.hotel.owner.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.util.DateUtil;
import com.hotel.owner.vo.*;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Override
    public CommonResponseVo OwnerSignUp(OwnerVo.OwnerSignUpRequest ownerVo) {

        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("사업자 회원 가입 완료");

        return result;
    }

    @Override
    public CommonResponseVo OwnerEditInfo(OwnerVo.OwnerInfo ownerInfoVo) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("사업자 정보 수정 완료");
        return result;
    }

    @Override
    public OwnerVo.OwnerInfoResponse ViewOwnerInfo() {
//        ApiResponseVo result = new ApiResponseVo();
        OwnerVo.OwnerInfoResponse result = new OwnerVo.OwnerInfoResponse();

        try{
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
