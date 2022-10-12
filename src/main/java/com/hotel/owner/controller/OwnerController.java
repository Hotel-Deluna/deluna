package com.hotel.owner.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.owner.svc.OwnerService;
import com.hotel.owner.vo.OwnerVo;
import io.micrometer.core.lang.Nullable;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpRequest;

@RestController
@RequestMapping("/owner")

public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @ApiOperation(value="사업자 회원가입")
    @ResponseBody
    @PostMapping("/sign-up")
    public CommonResponseVo OwnerSignUp(@RequestBody OwnerVo.OwnerSignUpRequest ownerVo){
        return ownerService.OwnerSignUp(ownerVo);
    }

    @ApiOperation(value="사업자 정보조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/view-info")
    public OwnerVo.OwnerInfoResponse ViewOwnerInfo(@RequestHeader(value="Authorization") String jwt){
        return ownerService.ViewOwnerInfo(jwt);
    }

    @ApiOperation(value="사업자 정보수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/edit-info")
    public CommonResponseVo OwnerEditInfo(@RequestBody OwnerVo.OwnerInfo ownerInfo, @RequestHeader(value="Authorization") String jwt){
        return ownerService.OwnerEditInfo(ownerInfo, jwt);
    }

    @ApiOperation(value="사업자 회원탈퇴")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping(value = "/withdraw", produces = "application/json")
    public CommonResponseVo OwnerWithdraw(@RequestBody OwnerVo.OwnerWithdrawRequest ownerWithdrawReasonVo, @RequestHeader(value="Authorization") String jwt){
        return ownerService.OwnerWithdraw(ownerWithdrawReasonVo, jwt);
    }

    @ApiOperation(value="사업자 번호 진위여부 조회 - 사업자 등록번호 유효 : true, 사업자 등록번호 유효하지않음 : false")
    @ResponseBody
    @PostMapping(value = "/verify", produces = "application/json")
    public OwnerVo.OwnerVerifyResponse OwnerVerify(@RequestBody OwnerVo.OwnerVerifyRequest ownerVerifyRequest){
        return ownerService.OwnerVerify(ownerVerifyRequest);
    }

}
