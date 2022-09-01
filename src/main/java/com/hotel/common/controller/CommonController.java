package com.hotel.common.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.svc.CommonService;
import com.hotel.common.vo.CommonVo;
import com.hotel.owner.vo.OwnerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
//@Api(tags = {"공통으로 사용하는 API를 제공하는 Controller"})
public class CommonController {

    @Autowired
    CommonService commonService;

    @ApiOperation(value="휴대폰 인증 요청 - 인증 번호 생성 및 전송")
    @ResponseBody
    @PostMapping("/phone/auth/request")
    public CommonResponseVo RequestPhoneAuth(@RequestBody CommonVo.PhoneAuthRequest PhoneAuthRequest){
        return commonService.RequestPhoneAuth(PhoneAuthRequest);
    }

    @ApiOperation(value="휴대폰 인증 확인 - 인증 번호 체크. 인증번호 맞으면 true. 틀리면 false 리턴")
    @ResponseBody
    @PostMapping("/phone/auth/verify")
    public CommonVo.VerifyPhoneAuthResponse VerifyPhoneAuth(@RequestBody CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest){
        return commonService.VerifyPhoneAuth(verifyPhoneAuthRequest);
    }

    @ApiOperation(value="이메일 중복 확인 - 중복이면 true. 아니면 false 리턴")
    @ResponseBody
    @PostMapping("/email/duplicate-check")
    public CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(@RequestBody CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest){
        return commonService.EmailDuplicateCheck(emailDuplicateCheckRequest);
    }

//    @ApiOperation(value="호텔 태그 - 중복이면 true. 아니면 false 리턴")
//    @ResponseBody
//    @PostMapping("/email/duplicate-check")
//    public CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(@RequestBody CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest){
//        return commonService.EmailDuplicateCheck(emailDuplicateCheckRequest);
//    }

}
