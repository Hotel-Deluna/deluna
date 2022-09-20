package com.hotel.member.controller;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.JwtTokenDto.TokenDto;
import com.hotel.jwt.CheckTokenInfo;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.member.dto.MemberRequestDto;
import com.hotel.member.svc.MemberServiceImpl;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.MemberDeleteVo;
import com.hotel.util.SHA512Util;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
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
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/sign-up")
	public CommonResponseVo MemberSignUp(@RequestBody MemberVo.RegisterMemberRequest registerMemberRequest) {

		CommonResponseVo vo = new CommonResponseVo();

		if (registerMemberRequest.getEmail().equals("")) {
			vo.setMessage("이메일이 없습니다.");
			return vo;
		} else if (registerMemberRequest.getName().equals("")) {
			vo.setMessage("이름이 없습니다.");
			return vo;
		} else if (registerMemberRequest.getPassword().equals("")) {
			vo.setMessage("비밀번호가 없습니다.");
			return vo;
		} else if (registerMemberRequest.getPhone_num().equals("")) {
			vo.setMessage("핸드폰번호가 없습니다.");
			return vo;
		} else if (registerMemberRequest.getRole().toString().equals("")) {
			vo.setMessage("권한 정보가 없습니다.");
			return vo;
		}
		return memberServiceImpl.MemberSignUp(registerMemberRequest);
	}

	@ApiOperation(value = "공통 로그인")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/sign-in")
	public ResponseEntity<CommonResponseVo> MemberSignIn(@RequestBody MemberRequestDto memberRequestDto,
			HttpServletResponse res) throws Exception {

		System.out.println("test  = " + memberRequestDto.toString());

		String email = memberRequestDto.getEmail();
		String pwd = memberRequestDto.getPassword();

		if (email.equals("") || email.equals(null)) {
			return ResponseEntity.of(null);
		} else if (memberRequestDto.getPassword().equals("")) {
			return ResponseEntity.of(null);
		}
		memberRequestDto.setPassword(shaUtil.encryptSHA512(pwd));
		CommonResponseVo dto = memberServiceImpl.getMemberInfo(memberRequestDto);
		res.setHeader("AccessToken", (String) dto.getMap().get("AccessToken"));
		res.setHeader("RefreshToken", (String) dto.getMap().get("RefreshToken"));
		
		dto.getMap().remove("AccessToken");
		dto.getMap().remove("RefreshToken");
		// role 체크 없으면 메세지 리턴
		return ResponseEntity.ok(dto);
	}
//	@ResponseBody
//	@PostMapping("/token")
//	public Map<String, Object> MemberTest(@RequestBody Map<String, Object> map, HttpServletRequest req) throws Exception {
//		
//		Map<String, Object> result = new HashMap<>();
//		
//		String token = req.getHeader("accessToken");
//		
//		System.out.println("token =" + token);
//		
//		
//		String email = info.tokenInfo(token);
//		
//		
//		return result;
//	}

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
	@ApiOperation(value = "고객 정보조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/view-info")
	public CommonResponseVo ViewMemberInfo(HttpServletRequest req)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		CommonResponseVo vo = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();
		String token = req.getHeader("accessToken");
		String email = info.tokenInfo(token);

		System.out.println("email = " + email);

		if (email.equals("") || email.equals(null)) {
			map.put("result", "ERR");
			map.put("reason", "Token Not Found");
			vo.setMap(map);
			return vo;
		}

		return memberServiceImpl.ViewMemberInfo(email);
	}

	@ApiOperation(value = "고객 정보수정")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PatchMapping("/edit-info")
	public CommonResponseVo MemberEditInfo(@RequestBody MemberVo.MemberUpdateInfo memberInfo, HttpServletRequest req)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		CommonResponseVo vo = new CommonResponseVo();
		Map<String, Object> result = new HashMap<>();

		if (memberInfo.getEmail().equals("")) {
			result.put("result", "err");
			result.put("reason", "email Not Found");
			vo.setMap(result);
			return vo;
		} else if (memberInfo.getName().equals("")) {
			result.put("result", "err");
			result.put("reason", "name Not Found");
			vo.setMap(result);
			return vo;
		} else if (memberInfo.getPhone_num().equals("")) {
			result.put("result", "err");
			result.put("reason", "phone_num Not Found");
			vo.setMap(result);
			return vo;
		}

		return memberServiceImpl.MemberEditInfo(memberInfo);
	}

	@ApiOperation(value = "공통 비밀번호 찾기")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PostMapping("/findPwd")
	public CommonResponseVo FindByPasswd(@RequestBody MemberVo.MemberFindPwdRequest findPwdRequest,
			HttpServletRequest req)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {

		CommonResponseVo vo = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();

		if (findPwdRequest.getEmail().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "email Not Found");
			vo.setMap(map);
			return vo;
		} else if (findPwdRequest.getName().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "name Not Found");
			vo.setMap(map);
			return vo;
		} else if (findPwdRequest.getPhone_num().equals("")) {
			map.put("result", "ERR");
			map.put("reason", "phone_num Not Found");
			vo.setMap(map);
			return vo;
		}

		return memberServiceImpl.FindByPasswd(findPwdRequest);
	}

	@ApiOperation(value = "공통 비밀번호 재설정")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@PatchMapping("/updatePwd")
	public CommonResponseVo UpdatePasswd(@RequestBody MemberVo.MemberUpdatePwdRequest updatePwdRequest,
			HttpServletRequest req) throws Exception {

		CommonResponseVo result = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();

		if (updatePwdRequest.getEmail_auth_num().equals("")) {
			result.setResult("ERR");
			result.setMessage("auth_num Not Found");
			result.setMap(map);
			return result;
		} else if (updatePwdRequest.getPassword().equals("")) {
			result.setResult("ERR");
			result.setMessage("password Not Found");
			result.setMap(map);
			return result;
		} else if (updatePwdRequest.getPassword().equals("")) {
			result.setResult("ERR");
			result.setMessage("password Not Found");
			result.setMap(map);
			return result;
		}

		String token = req.getHeader("accessToken");
		String email = info.tokenInfo(token);
		

		if (email.equals("")) {
			result.setResult("ERR");
			result.setMessage("token Not Found");
			result.setMap(map);
			return result;
		}
		
		updatePwdRequest.setEmail(email);

		return memberServiceImpl.UpdatePasswd(updatePwdRequest);
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
	@ApiOperation(value = "고객 회원탈퇴")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "JWT access_token", required = true, dataType = "string", paramType = "header") })
	@ResponseBody
	@DeleteMapping(value = "/withdraw", produces = "application/json")
	public CommonResponseVo MemberWithdraw(@RequestBody MemberVo.MemberDeleteRequest memberWithdrawReasonVo,
			HttpServletRequest req) {

		CommonResponseVo vo = new CommonResponseVo();
		if (Integer.toString(memberWithdrawReasonVo.getReason()).equals("")) {
			vo.setMessage("reason not found");
			return vo;
		}

		String token = req.getHeader("accessToken");
		String email = info.tokenInfo(token);
		if (email.equals("") || email.equals(null)) {
			vo.setMessage("토큰정보가 잘못되었습니다.");
			return vo;
		}

		MemberVo.MemberDeleteVo deleteVo = new MemberVo.MemberDeleteVo();

		deleteVo.setEmail(email);
		deleteVo.setContent(memberWithdrawReasonVo.getReason());

		return memberServiceImpl.MemberWithdraw(deleteVo);
	}

}
