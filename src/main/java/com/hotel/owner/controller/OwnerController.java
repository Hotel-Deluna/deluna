package com.hotel.owner.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.owner.svc.OwnerService;
import com.hotel.owner.vo.OwnerVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public OwnerVo.OwnerInfoResponse ViewOwnerInfo(){
        return ownerService.ViewOwnerInfo();
    }

    @ApiOperation(value="사업자 정보수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/edit-info")
    public CommonResponseVo OwnerEditInfo(@RequestBody OwnerVo.OwnerInfo ownerInfo){
        return ownerService.OwnerEditInfo(ownerInfo);
    }

    @ApiOperation(value="사업자 회원탈퇴")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping(value = "/withdraw", produces = "application/json")
    public CommonResponseVo OwnerWithdraw(@RequestBody OwnerVo.OwnerWithdrawRequest ownerWithdrawReasonVo){
        return ownerService.OwnerWithdraw(ownerWithdrawReasonVo);
    }



}
