package com.hotel.owner.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.owner.vo.OwnerVo;

public interface OwnerService {
    CommonResponseVo OwnerSignUp(OwnerVo.OwnerSignUpRequest ownerVo);

    CommonResponseVo OwnerEditInfo(OwnerVo.OwnerInfo ownerInfoVo);

    OwnerVo.OwnerInfoResponse ViewOwnerInfo();

    CommonResponseVo OwnerWithdraw(OwnerVo.OwnerWithdrawRequest ownerWithdrawRequest);

    OwnerVo.OwnerVerifyResponse OwnerVerify(OwnerVo.OwnerVerifyRequest ownerVerifyRequest);
}
