package com.hotel.member.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.member.svc.MemberService;
import com.hotel.member.vo.MemberVo;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")

public class MemberController {

    @Autowired
    MemberService memberService;

    @ApiOperation(value="고객 회원가입")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/sign-up")
    public CommonResponseVo MemberSignUp(@RequestBody MemberVo.RegisterMemberRequest registerMemberRequest){
        return memberService.MemberSignUp(registerMemberRequest);
    }
    
    @ApiOperation(value="공통 로그인")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/sign-in")
    public CommonResponseVo MemberSignIn(@RequestBody MemberVo.LoginMemberRequest loginMemberRequest){
        return memberService.MemberSignIn(loginMemberRequest);
    }
    
    @ApiOperation(value="카카오 로그인")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/sign-kakao")
    public CommonResponseVo MemberSignInKakao(@RequestBody MemberVo.LoginMemberRequestKokao loginMemberRequestKokao){
        return memberService.memberSignInKakao(loginMemberRequestKokao);
    }
    
    @ApiOperation(value="네이버 로그인")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/sign-naver")
    public CommonResponseVo MemberSignInNaver(@RequestBody MemberVo.LoginMemberRequestNaver loginMemberRequestNaver){
        return memberService.memberSignInNaver(loginMemberRequestNaver);
    }
    
    @ApiOperation(value="구글 로그인")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/sign-google")
    public CommonResponseVo MemberSignInGoogle(@RequestBody MemberVo.LoginMemberRequestGoogle loginMemberRequestGoogle){
        return memberService.memberSignInGoogle(loginMemberRequestGoogle);
    }
    
    @ApiOperation(value="고객 정보조회")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/view-info")
    public MemberVo.MemberInfoResponse ViewMemberInfo(){
        return memberService.ViewMemberInfo();
    }

    @ApiOperation(value="고객 정보수정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/edit-info")
    public CommonResponseVo MemberEditInfo(@RequestBody MemberVo.MemberUpdateInfo memberInfo){
        return memberService.MemberEditInfo(memberInfo);
    }
    
    @ApiOperation(value="공통 비밀번호 찾기")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/findPwd")
    public CommonResponseVo FindPasswdEmail(@RequestBody MemberVo.MemberFindPwdRequest findPwdRequest){
        return memberService.FindPasswdEmail(findPwdRequest);
    }
    
    @ApiOperation(value="공통 비밀번호 재설정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/updatePwd")
    public CommonResponseVo UpdatePasswd(@RequestBody MemberVo.MemberUpdatePwdRequest updatePwdRequest){
        return memberService.UpdatePasswd(updatePwdRequest);
    }

    @ApiOperation(value="공통 아이디 찾기")
    @ResponseBody
    @PostMapping("/find/id")
    public MemberVo.FindIdResponse FindId(@RequestBody MemberVo.FindIdRequest findIdRequest){
        return memberService.FindId(findIdRequest);
    }

    @ApiOperation(value="공통 비밀번호 변경")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/edit/password")
    public CommonResponseVo EditPassword(@RequestBody MemberVo.EditPasswordRequest editPasswordRequest){
        return memberService.EditPassword(editPasswordRequest);
    }

    @ApiOperation(value="공통 비밀번호 찾기")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/findPwd")
    public CommonResponseVo FindPasswdEmail(@RequestBody MemberVo.MemberFindPwdRequest findPwdRequest){
        return memberService.FindPasswdEmail(findPwdRequest);
    }

    @ApiOperation(value="공통 비밀번호 재설정")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PatchMapping("/updatePwd")
    public CommonResponseVo UpdatePasswd(@RequestBody MemberVo.MemberUpdatePwdRequest updatePwdRequest){
        return memberService.UpdatePasswd(updatePwdRequest);
    }

    @ApiOperation(value="고객 회원탈퇴")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @DeleteMapping(value = "/withdraw", produces = "application/json")
    public CommonResponseVo MemberWithdraw(@RequestBody MemberVo.MemberDeleteRequest memberWithdrawReasonVo){
        return memberService.MemberWithdraw(memberWithdrawReasonVo);
    }
    
}
