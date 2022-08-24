package com.hotel.owner.svc;

import com.hotel.common.ApiResponseVo;
import com.hotel.owner.vo.OwnerSignUpParamVo;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Override
    public ApiResponseVo OwnerSignUp(OwnerSignUpParamVo ownerSignUpParamVo) {

        ApiResponseVo result = new ApiResponseVo();
        result.setResult("OK");
        result.setMessage("가입완료");

        return result;
    }
}
