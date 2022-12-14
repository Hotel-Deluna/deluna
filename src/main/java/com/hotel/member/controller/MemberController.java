package com.hotel.member.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.JwtTokenDto;
import com.hotel.common.vo.JwtTokenDto.TokenDto;
import com.hotel.jwt.CheckTokenInfo;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.member.dto.MemberRequestDto;
import com.hotel.member.svc.MemberServiceImpl;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.LoginMemberResponseDto;
import com.hotel.member.vo.MemberVo.MemberDeleteVo;
import com.hotel.member.vo.MemberVo.MemberInfoResponse;
import com.hotel.member.vo.MemberVo.MemberResponseDto;
import com.hotel.member.vo.MemberVo.ViewMemberInfoResponseDto;
import com.hotel.util.SHA512Util;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberServiceImpl memberServiceImpl;

	private final SHA512Util shaUtil;

	private final JwtTokenProvider jwtTokenProvider;

	private final CheckTokenInfo info;

	@ApiOperation(value = "고객 회원가입")
	@ResponseBody
	@PostMapping("/sign-up")
	public MemberResponseDto MemberSignUp(@RequestBody MemberVo.RegisterMemberRequest registerMemberRequest) {

		MemberResponseDto dto = new MemberResponseDto();

		if (registerMemberRequest.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("email not Found");
			return dto;
		} else if (registerMemberRequest.getName().equals("")) {
			dto.setResult("ERR");
			dto.setReason("name not Found");
			return dto;
		} else if (registerMemberRequest.getPassword().equals("")) {
			dto.setResult("ERR");
			dto.setReason("password not Found");
			return dto;
		} else if (registerMemberRequest.getPhone_num().equals("")) {
			dto.setResult("ERR");
			dto.setReason("phone_num not Found");
			return dto;
		} 
		return memberServiceImpl.MemberSignUp(registerMemberRequest);
	}

	@ApiOperation(value = "공통 로그인")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/sign-in")
	public LoginMemberResponseDto MemberSignIn(@RequestBody MemberRequestDto memberRequestDto,
			HttpServletResponse res) throws Exception {
		LoginMemberResponseDto dto = new LoginMemberResponseDto();
		System.out.println("test  = " + memberRequestDto.toString());

		String email = memberRequestDto.getEmail();
		String pwd = memberRequestDto.getPassword();
		
		if (email.equals("") || email.equals(null) || pwd.equals("") || pwd.equals(null)) {
			dto.setResult("ERR");
			dto.setReason("PrameterNotFound");
			dto.setEmail("");
			dto.setRole(0);
			return dto; 
		} 
		memberRequestDto.setPassword(shaUtil.encryptSHA512(pwd));
		Map<String, Object> data = memberServiceImpl.getMemberInfo(memberRequestDto);
		
		Date date = new Date();
		SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		res.setHeader("Authorization", (String) data.get("Authorization"));
		res.setHeader("RefreshToken", (String) data.get("RefreshToken"));
		res.setHeader("TokenCreateDate", sDate.format(date));
		dto.setEmail((String) data.get("email"));
		dto.setRole((Integer) data.get("role"));
		
		if(!(dto.getEmail() == null)) {
			dto.setResult("OK");
			dto.setReason("");
		}else {
			dto.setResult("ERR");
			dto.setReason("memberNotFound");
			dto.setEmail("");
			dto.setRole(0);
		}
		
		return dto;
	}


	@ApiOperation(value = "소셜 로그인")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/sign-in-social")
	public LoginMemberResponseDto MemberSignInKakao(@RequestBody MemberVo.LoginMemberRequestSocial loginMemberRequestSocial) throws Exception {
		
		LoginMemberResponseDto dto = new LoginMemberResponseDto();
		
		if(loginMemberRequestSocial.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("email Not Found");
			return dto;
		}else if(loginMemberRequestSocial.getName().equals("")) {
			dto.setResult("ERR");
			dto.setReason("name Not Found");
			return dto;
		}
		return memberServiceImpl.memberSignInSocial(loginMemberRequestSocial);
	}

	@ApiOperation(value = "고객 정보조회")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/view-info")
	public MemberInfoResponse ViewMemberInfo(HttpServletRequest req)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		MemberInfoResponse dto = new MemberInfoResponse();
		String token = req.getHeader("Authorization");
		String email = info.tokenInfo(token);

		if (email.equals("") || email.equals(null)) {
			dto.setResult("ERR");
			dto.setMessage("email not Found");
			return dto;
		}

		return memberServiceImpl.ViewMemberInfo(email);
	}

	@ApiOperation(value = "고객 정보수정")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PatchMapping("/edit-info")
	public MemberVo.MemberResponseDto MemberEditInfo(@RequestBody MemberVo.MemberUpdateInfo memberInfo, HttpServletRequest req)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		
		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto();
		
		String token = req.getHeader("Authorization");
		String email = info.tokenInfo(token);

		if (memberInfo.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("token Not Found");
			
			if(!memberInfo.getEmail().equals(email)) {
				dto.setReason("email deference fail");
			}
			return dto;
		} else if (memberInfo.getName().equals("")) {
			dto.setResult("ERR");
			dto.setReason("name Not Found");
			return dto;
		} else if (memberInfo.getPhone_num().equals("")) {
			dto.setResult("ERR");
			dto.setReason("phone_num Not Found");
			return dto;
		}

		return memberServiceImpl.MemberEditInfo(memberInfo);
	}

	@ApiOperation(value = "공통 비밀번호 찾기")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/findPwd")
	public MemberVo.MemberResponseDto FindByPasswd(@RequestBody MemberVo.MemberFindPwdRequest findPwdRequest,
			HttpServletResponse res)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto();
		
		if (findPwdRequest.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("name Not Found");
			return dto;
		} else if (findPwdRequest.getName().equals("")) {
			dto.setResult("ERR");
			dto.setReason("name Not Found");
			return dto;
		} else if (findPwdRequest.getPhone_num().equals("")) {
			dto.setResult("ERR");
			dto.setReason("phone_num Not Found");
			return dto;
		}
		
		dto = memberServiceImpl.FindByPasswd(findPwdRequest);
		
		//토큰 전달
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(findPwdRequest.getEmail(), findPwdRequest.getPhone_num());
		TokenDto token = new TokenDto();
		token = jwtTokenProvider.generateMemberTokenDto(authenticationToken, "ROLE_UPDATE_MEMBER");
		res.setHeader("Authorization", token.getAccessToken());
		res.setHeader("RefreshToken", token.getRefreshToken());
		
		return dto; 
	}

	@ApiOperation(value = "공통 비밀번호 재설정")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PatchMapping("/updatePwd")
	public  MemberVo.MemberResponseDto UpdatePasswd(@RequestBody MemberVo.MemberUpdatePwdRequest updatePwdRequest,
			HttpServletRequest req) throws Exception {

		MemberVo.MemberResponseDto dto = new  MemberVo.MemberResponseDto();
		 
		if (updatePwdRequest.getEmail_auth_num().equals("")) {
			dto.setResult("ERR");
			dto.setReason("auth_num Not Found");
			return dto;
		} else if (updatePwdRequest.getPassword().equals("")) {
			dto.setResult("ERR");
			dto.setReason("password Not Found");
			return dto;
		} 
		
		String token = req.getHeader("Authorization");
		String email = info.tokenInfo(token);
		
		if (email.equals("")) {
			dto.setResult("ERR");
			dto.setReason("token Not Found");
			return dto;
		}
		
		updatePwdRequest.setEmail(email);

		return memberServiceImpl.UpdatePasswd(updatePwdRequest);
	}
	
	@ApiOperation(value = "공통 비밀번호 변경")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PatchMapping("/memberUpdatePwd")
	public  MemberVo.MemberResponseDto MemberUpdatePasswd(@RequestBody MemberVo.MemberChangePwdRequest updatePwdRequest,
			HttpServletRequest req) throws Exception {

		MemberVo.MemberResponseDto dto = new  MemberVo.MemberResponseDto();
		 
		if (updatePwdRequest.getUpdate_password().equals("")) {
			dto.setResult("ERR");
			dto.setReason("update_pwd Not Found");
			return dto;
		} else if (updatePwdRequest.getPassword().equals("")) {
			dto.setResult("ERR");
			dto.setReason("password Not Found");
			return dto;
		} 

		String token = req.getHeader("Authorization");
		String email = info.tokenInfo(token);
		
		if (email == null) {
			dto.setResult("ERR");
			dto.setReason("token Not Found");
			return dto;
		}
		
		updatePwdRequest.setEmail(email);

		return memberServiceImpl.MemberUpdatePasswd(updatePwdRequest);
	}

	@ApiOperation(value = "공통 아이디 찾기")
	@ResponseBody
	@PostMapping("/find/id")
	public Map<String, Object> FindId(@RequestBody MemberVo.FindIdRequest findIdRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		Map<String, Object> map = new HashMap<>();

		if (findIdRequest.getName().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "name Not found");
			return map;
		} else if (findIdRequest.getPhone_num().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "phone_num Not found");
			return map;
		}

		return memberServiceImpl.FindId(findIdRequest);
	}

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
	@ApiOperation(value = "공통 고객 회원탈퇴")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@DeleteMapping(value = "/withdraw", produces = "application/json")
	public MemberVo.MemberResponseDto MemberWithdraw(@RequestBody MemberVo.MemberDeleteRequest memberWithdrawReasonVo,
			HttpServletRequest req) {

		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto();
		if (Integer.toString(memberWithdrawReasonVo.getReason()).equals("")) {
			dto.setResult("ERR");
			dto.setReason("reason Not Found");
			return dto;
		}

		String token = req.getHeader("Authorization");
		String email = info.tokenInfo(token);
		if (email.equals("") || email.equals(null)) {
			dto.setResult("ERR");
			dto.setReason("token Fail");
			return dto;
		}

		MemberVo.MemberDeleteVo deleteVo = new MemberVo.MemberDeleteVo();

		deleteVo.setEmail(email);
		deleteVo.setContent(memberWithdrawReasonVo.getReason());

		return memberServiceImpl.MemberWithdraw(deleteVo);
	}

}
