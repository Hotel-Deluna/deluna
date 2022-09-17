package com.hotel.owner.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.owner.vo.OwnerVo;

public interface OwnerService {
    CommonResponseVo OwnerSignUp(OwnerVo.OwnerSignUpRequest ownerVo);

    CommonResponseVo OwnerEditInfo(OwnerVo.OwnerInfo ownerInfoVo, String jwt);

    OwnerVo.OwnerInfoResponse ViewOwnerInfo(String jwt);

    CommonResponseVo OwnerWithdraw(OwnerVo.OwnerWithdrawRequest ownerWithdrawRequest, String jwt);

    OwnerVo.OwnerVerifyResponse OwnerVerify(OwnerVo.OwnerVerifyRequest ownerVerifyRequest);
}
