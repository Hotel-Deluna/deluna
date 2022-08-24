package com.hotel.owner.controller;

import com.hotel.owner.svc.OwnerService;
import com.hotel.common.ApiResponseVo;
import com.hotel.owner.vo.OwnerSignUpParamVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")

public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @ApiOperation(value="사업자 회원가입")
    @PostMapping("/sign-up")
    public @ResponseBody
    ApiResponseVo OwnerSignUp(OwnerSignUpParamVo ownerSignUpParamVo){
        return ownerService.OwnerSignUp(ownerSignUpParamVo);
    }

}
