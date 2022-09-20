package com.hotel.member.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.JwtTokenDto.TokenDto;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.mapper.MemberMapper;
import com.hotel.member.dto.MemberRequestDto;
import com.hotel.member.dto.MemberResponseDto;
import com.hotel.member.repo.MemberRepository;
import com.hotel.member.vo.Member;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.FindIdRequest;
import com.hotel.member.vo.MemberVo.LoginMemberRequestGoogle;
import com.hotel.member.vo.MemberVo.LoginMemberRequestKokao;
import com.hotel.member.vo.MemberVo.LoginMemberRequestNaver;
import com.hotel.member.vo.MemberVo.MemberDeleteVo;
import com.hotel.member.vo.MemberVo.MemberEmailAuthInfo;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;
import com.hotel.util.AES256Util;
import com.hotel.util.AuthUtil;
import com.hotel.util.MailUtil;
import com.hotel.util.SHA512Util;
import com.hotel.util.SecurityUtil;
import com.hotel.util.vo.UtilVo;
import com.hotel.util.vo.UtilVo.MailRequest;

import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements UserDetailsService {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final JwtTokenProvider jwtTokenProvider;

	private final MemberRepository memberRepository;

	private final MemberMapper memberMapper;

	private final AES256Util aesUtil;

	private final SHA512Util shaUtil;

	private final MailUtil mailUtil;

	private final AuthUtil authUtil;

	@Value("{aes.256.key}")
	String aesKey;

	public CommonResponseVo MemberSignUp(MemberVo.RegisterMemberRequest memberVo) {

		CommonResponseVo result = new CommonResponseVo();

		String checkByEmail = memberMapper.checkByEmail(memberVo.getEmail());

		// 이메일 중복여부 체크
		// 비밀번호 암호화 후 db 인서트
		try {
			if (checkByEmail == null) {
				// 비밀번호 암호화
				// 추가 예정
				System.out.println("memberVo = " + memberVo.toString());

				// 핸드폰 암호화
				memberVo.setPhone_num(aesUtil.encrypt(memberVo.getPhone_num()));

				// 비밀번호 암호화
				memberVo.setPassword(shaUtil.encryptSHA512(memberVo.getPassword()));

				System.out.println("pwd = " + memberVo.getPassword());
				int registerMember = memberMapper.registerMemberInfo(memberVo);

				System.out.println("registerMember = " + registerMember);

				if (registerMember > 0) {
					result.setMessage("회원가입이 완료되었습니다.");
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	// 이메일 조회
	@SuppressWarnings("static-access")
	@Transactional(readOnly = true)
	public CommonResponseVo getMemberInfo(MemberRequestDto memberRequestDto) {
		CommonResponseVo result = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();
		TokenDto dto = new TokenDto();

		try {
			Optional<Member> member = memberRepository.findByEmail(memberRequestDto.getEmail());
			
			System.out.println("test = " + member.isEmpty()); 
			
			if (member.isEmpty() == true) {
				result.setResult("ERR");
				result.setMessage("member Not Found");
				result.setMap(map);
				return result;
			}
			member = memberRepository.findByPassword(memberRequestDto.getPassword());
			if (member.isEmpty() == true) {
				result.setResult("ERR");
				result.setMessage("password auth fail");
				result.setMap(map);
				return result;
			}
			String id = member.get().getEmail();
			String role = member.get().getRole();
			if (role.equals("0")) {
				member.get().setRole("ROLE_USER");
			} else if (role.equals("1")) {
				member.get().setRole("ROLE_OWNER");
			} else if (role.equals("2")) {
				member.get().setRole("ROLE_UN_USER");
			}
			member.get().builder().email(id).role(role);
			UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
			dto = jwtTokenProvider.generateTokenDto(authenticationToken, member.get().getRole());
			
			map.put("AccessToken", dto.getAccessToken());
			map.put("RefreshToken", dto.getRefreshToken());
			map.put("email", id);
			map.put("role", role);
		} catch (Exception e) {
			// TODO: handle exception
		}
			result.setMap(map);
		return result;
	}

	// 현재 SecurityContext 에 있는 유저 정보 가져오기
	@Transactional(readOnly = true)
	public MemberResponseDto getMyInfo() {
		return memberRepository.findById(SecurityUtil.getCurrentMemberId()).map(MemberResponseDto::of)
				.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
	}

	public CommonResponseVo memberSignInKakao(LoginMemberRequestKokao memberVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("code정보");

		return result;
	}

	public CommonResponseVo memberSignInNaver(LoginMemberRequestNaver memberVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("code정보");

		return result;
	}

	public CommonResponseVo memberSignInGoogle(LoginMemberRequestGoogle memberVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("code정보, 수정 예정");

		return result;
	}

	public CommonResponseVo MemberEditInfo(MemberVo.MemberUpdateInfo memberInfoVo)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		CommonResponseVo result = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();

		String phone = memberInfoVo.getPhone_num();

		memberInfoVo.setPhone_num(aesUtil.encrypt(phone));

		System.out.println("phone = " + memberInfoVo.getPhone_num());

		int chk = memberMapper.updateMemberInfo(memberInfoVo);

		if (chk > 0) {
			map.put("result", "OK");
			map.put("reason", "");
			result.setMap(map);
		} else {
			map.put("result", "ERR");
			map.put("reason", "member Not Found");
			result.setMap(map);
		}

		return result;
	}

	public MemberVo.MemberInfoResponse ViewMemberInfo(String email)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
//        ApiResponseVo result = new ApiResponseVo();
		MemberVo.MemberInfoResponse result = new MemberVo.MemberInfoResponse();
		MemberVo.MemberInfo memberInfoVo = new MemberVo.MemberInfo();

		memberInfoVo = memberMapper.viewMemberInfo(email);

		// 핸드폰 복호화
		memberInfoVo.setPhone_num(aesUtil.decrypt(String.valueOf(memberInfoVo.getPhone_num())));

		result.setMessage("고객정보 조회 완료");
		result.setData(memberInfoVo);
		return result;
	}

	public CommonResponseVo MemberWithdraw(MemberDeleteVo deleteVo) {
		CommonResponseVo result = new CommonResponseVo();
		// 회원 탈퇴처리
		// ownerWithdrawReasonVo 사유 DB 저장

		Map<String, Object> memberInfo = memberMapper.deleteMemberInfo(deleteVo.getEmail());
		System.out.println("memberInfo = " + memberInfo.toString());
		int member_num = (int) memberInfo.get("member_num");
		if (member_num > -1) {
			int num = (int) memberInfo.get("role");

			String role = Integer.toString(num);

			deleteVo.setMember_num(member_num);
			deleteVo.setUpdate_user(role + member_num);

			// 삭제 여부 확인
			int check = memberMapper.deleteMember(deleteVo);

			if (check == 1) {
				int deleteOk = memberMapper.insertDeleteMember(deleteVo);
				Map<String, Object> map = new HashMap<>();
				if (deleteOk == 1) {
					map.put("result", "OK");
					map.put("reason", "");
					result.setMap(map);
				} else {
					map.put("result", "ERR");
					map.put("reason", "ERR");
					result.setMap(map);
				}
			}
		}

		return result;
	}

	public CommonResponseVo FindByPasswd(MemberFindPwdRequest findPwdRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		CommonResponseVo result = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();
		findPwdRequest.setPhone_num(aesUtil.encrypt(findPwdRequest.getPhone_num()));

		String email = memberMapper.checkEmailByPwd(findPwdRequest);

		if (email == null) {
			map.put("result", "ERR");
			map.put("reason", "email Not Found");
			result.setMap(map);
			return result;
		}

		String user_num = memberMapper.selectMemberNum(email);

		if (user_num == null) {
			map.put("result", "ERR");
			map.put("reason", "member_num Not Found");
			result.setMap(map);
			return result;
		}

		String key = authUtil.CreateAuthNum();

		System.out.println("key = " + key);

		MemberEmailAuthInfo req = new MemberEmailAuthInfo();

		req.setEmail_auth_num(key);
		req.setUser_num(user_num);

		int data = memberMapper.updateEmailAuthInfo(req);

		if (data == 0) {
			result.setResult("ERR");
			result.setMessage("");
			map.put("errContent", "auth Not Insert");
			result.setMap(map);
			return result;
		}

		UtilVo.MailRequest mailSender = new MailRequest();

		mailSender.setReceiver(email);
		mailSender.setKey(key);
		// 수신자 메일정보 저장
		mailUtil.sendMail(mailSender);

		map.put("data", "");
		result.setResult("OK");
		result.setMessage("");
		result.setMap(map);

		return result;
	}

	public CommonResponseVo UpdatePasswd(MemberUpdatePwdRequest updatePwdRequest) throws Exception {
		CommonResponseVo result = new CommonResponseVo();
		Map<String, Object> map = new HashMap<>();
		
		String user_num = memberMapper.selectMemberNum(updatePwdRequest.getEmail());
		
		updatePwdRequest.setUser_num(user_num);
		String auth_num = memberMapper.checkEmailAuthInfo(updatePwdRequest);
		
		if(auth_num == null) {
			result.setResult("ERR");
			result.setMessage("");
			map.put("auth_num", "auth Not Found");
			result.setMap(map);
			return result;
		}
		
		updatePwdRequest.setPassword(shaUtil.encryptSHA512(updatePwdRequest.getPassword()));
		
		int updatePwd = memberMapper.updatePwdInfo(updatePwdRequest);
		
		if(updatePwd == 0) {
			result.setResult("ERR");
			result.setMessage("");
			map.put("pwd", "update fail");
			result.setMap(map);
			return result;
		}
		
		
		return result;
	}

	public Map<String, Object> FindId(MemberVo.FindIdRequest findIdRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		Map<String, Object> result = new HashMap<>();

		findIdRequest.setPhone_num(aesUtil.encrypt(findIdRequest.getPhone_num()));

		String email = memberMapper.findByEmail(findIdRequest);

		if (email == null) {
			result.put("result", "ERR");
			result.put("reason", "email Not found");
			return result;
		}
		result.put("result", "OK");
		result.put("reason", "");
		result.put("data", email);

		return result;
	}

	public CommonResponseVo EditPassword(MemberVo.EditPasswordRequest editPasswordRequest) {
		CommonResponseVo result = new CommonResponseVo();
		// 기존비밀번호 암호화 이후 DB 비밀번호와 비교
		// 만약 일치하지 않으면 일치하지 않는다는 에러값 리턴

		result.setMessage("비밀번호 변경 완료");

		return result;
	}

}
