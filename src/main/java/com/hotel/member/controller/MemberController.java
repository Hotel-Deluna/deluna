package com.hotel.member.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.JwtTokenDto;
import com.hotel.common.vo.JwtTokenDto.TokenDto;
import com.hotel.member.dto.MemberRequestDto;
import com.hotel.member.dto.MemberResponseDto;
import com.hotel.member.svc.MemberService;
import com.hotel.member.svc.MemberServiceImpl;
import com.hotel.member.vo.MemberVo;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberServiceImpl memberServiceImpl;
	
    @ApiOperation(value="고객 회원가입")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header")})
    @ResponseBody
    @PostMapping("/sign-up")
    public CommonResponseVo MemberSignUp(@RequestBody MemberVo.RegisterMemberRequest registerMemberRequest){
    	
    	CommonResponseVo vo = new CommonResponseVo();
    	
    	if(registerMemberRequest.getEmail().equals("")) {
    		vo.setMessage("이메일이 없습니다.");
    		return vo;
    	}else if(registerMemberRequest.getName().equals("")) {
    		vo.setMessage("이름이 없습니다.");
    		return vo;
    	}else if(registerMemberRequest.getPassword().equals("")) {
    		vo.setMessage("비밀번호가 없습니다.");
    		return vo;
    	}else if(registerMemberRequest.getPhone_num().equals("")) {
    		vo.setMessage("핸드폰번호가 없습니다.");
    		return vo;
    	}
    	
        return memberServiceImpl.MemberSignUp(registerMemberRequest);
    }

	@ApiOperation(value = "공통 로그인")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/sign-in")
	public ResponseEntity<TokenDto> MemberSignIn(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse res) {
		
		System.out.println("test  = " + memberRequestDto.toString());
		
		String email = memberRequestDto.getEmail();
		
		if (email.equals("") || email.equals(null)) {
			return ResponseEntity.of(null);
		}
		
		TokenDto dto = memberServiceImpl.getMemberInfo(memberRequestDto);
		res.setHeader("AccessToken", dto.getAccessToken());
		res.setHeader("RefreshToken", dto.getRefreshToken());

		// role 체크 없으면 메세지 리턴
		return ResponseEntity.ok(dto);
	}

//	@ApiOperation(value = "카카오 로그인")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PostMapping("/sign-kakao")
//	public CommonResponseVo MemberSignInKakao(@RequestBody MemberVo.LoginMemberRequestKokao loginMemberRequestKokao) {
//		return memberService.memberSignInKakao(loginMemberRequestKokao);
//	}
//
//	@ApiOperation(value = "네이버 로그인")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PostMapping("/sign-naver")
//	public CommonResponseVo MemberSignInNaver(@RequestBody MemberVo.LoginMemberRequestNaver loginMemberRequestNaver) {
//		return memberService.memberSignInNaver(loginMemberRequestNaver);
//	}
//
//	@ApiOperation(value = "구글 로그인")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PostMapping("/sign-google")
//	public CommonResponseVo MemberSignInGoogle(
//			@RequestBody MemberVo.LoginMemberRequestGoogle loginMemberRequestGoogle) {
//		return memberService.memberSignInGoogle(loginMemberRequestGoogle);
//	}
//
//	@ApiOperation(value = "고객 정보조회")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PostMapping("/view-info")
//	public MemberVo.MemberInfoResponse ViewMemberInfo() {
//		return memberService.ViewMemberInfo();
//	}
//
//	@ApiOperation(value = "고객 정보수정")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PatchMapping("/edit-info")
//	public CommonResponseVo MemberEditInfo(@RequestBody MemberVo.MemberUpdateInfo memberInfo) {
//		return memberService.MemberEditInfo(memberInfo);
//	}
//
//	@ApiOperation(value = "공통 비밀번호 찾기")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PostMapping("/findPwd")
//	public CommonResponseVo FindPasswdEmail(@RequestBody MemberVo.MemberFindPwdRequest findPwdRequest) {
//		return memberService.FindPasswdEmail(findPwdRequest);
//	}
//
//	@ApiOperation(value = "공통 비밀번호 재설정")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PatchMapping("/updatePwd")
//	public CommonResponseVo UpdatePasswd(@RequestBody MemberVo.MemberUpdatePwdRequest updatePwdRequest) {
//		return memberService.UpdatePasswd(updatePwdRequest);
//	}
//
//	@ApiOperation(value = "공통 아이디 찾기")
//	@ResponseBody
//	@PostMapping("/find/id")
//	public MemberVo.FindIdResponse FindId(@RequestBody MemberVo.FindIdRequest findIdRequest) {
//		return memberService.FindId(findIdRequest);
//	}
//
//	@ApiOperation(value = "공통 비밀번호 변경")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@PostMapping("/edit/password")
//	public CommonResponseVo EditPassword(@RequestBody MemberVo.EditPasswordRequest editPasswordRequest) {
//		return memberService.EditPassword(editPasswordRequest);
//	}
//
//	@ApiOperation(value = "고객 회원탈퇴")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
//	@ResponseBody
//	@DeleteMapping(value = "/withdraw", produces = "application/json")
//	public CommonResponseVo MemberWithdraw(@RequestBody MemberVo.MemberDeleteRequest memberWithdrawReasonVo) {
//		return memberService.MemberWithdraw(memberWithdrawReasonVo);
//	}

}
