package com.hotel.owner.svc;

import com.hotel.common.ApiResponseVo;
import com.hotel.owner.vo.OwnerSignUpParamVo;

public interface OwnerService {
    ApiResponseVo OwnerSignUp(OwnerSignUpParamVo ownerSignUpParamVo);
}
