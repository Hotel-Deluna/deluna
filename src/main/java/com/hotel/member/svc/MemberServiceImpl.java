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
import com.hotel.member.vo.MemberVo.LoginMemberResponseDto;
import com.hotel.member.vo.MemberVo.MemberChangePwdRequest;
import com.hotel.member.vo.MemberVo.MemberDeleteVo;
import com.hotel.member.vo.MemberVo.MemberEmailAuthInfo;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;
import com.hotel.member.vo.MemberVo.ViewMemberInfoResponseDto;
import com.hotel.util.AES256Util;
import com.hotel.util.AuthUtil;
import com.hotel.util.DBUtil;
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
	
	private final DBUtil dbUtil;
	

	@Value("{aes.256.key}")
	String aesKey;

	public com.hotel.member.vo.MemberVo.MemberResponseDto MemberSignUp(MemberVo.RegisterMemberRequest memberVo) {

		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto();

		String checkByEmail = memberMapper.checkByEmail(memberVo.getEmail());

		// 이메일 중복여부 체크
		// 핸드폰 중복여부 체크
		// 비밀번호 암호화 후 db 인서트
		try {
			if (checkByEmail == null) {
				//핸드폰 중복 확인
				String phone = memberMapper.checkPhoneNum(aesUtil.encrypt(memberVo.getPhone_num()));
				
				if(phone == null) {
					// 비밀번호 암호화
					// 추가 예정
					// 핸드폰 암호화
					memberVo.setPhone_num(aesUtil.encrypt(memberVo.getPhone_num()));
					// 비밀번호 암호화
					memberVo.setPassword(shaUtil.encryptSHA512(memberVo.getPassword()));

					int registerMember = memberMapper.registerMemberInfo(memberVo);

					if (registerMember > 0) {
						dto.setResult("OK");
						dto.setReason("");
					}
				}else {
					dto.setResult("ERR");
					dto.setReason("select phone_num overlap");
					return dto;
				}
			}else {
				dto.setResult("ERR");
				dto.setReason("select email overlap");
				return dto;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}

		return dto;
	}

	// 이메일 조회
	@SuppressWarnings("static-access")
	@Transactional(readOnly = true)
	public Map<String, Object> getMemberInfo(MemberRequestDto memberRequestDto) {
		Map<String, Object> map = new HashMap<>();
		MemberRequestDto vo = new MemberRequestDto();
		TokenDto dto = new TokenDto();

		try {
			Optional<Member> member = memberRepository.findByEmail(memberRequestDto.getEmail());
			
			System.out.println("test = " + member.isEmpty());
			System.out.println("test = " + member.toString());
			
			if (member.isEmpty() == true) {
				map.put("result", "ERR");
				map.put("reason", "member Not Found");
				return map;
			}
			vo = memberMapper.findByPassword(memberRequestDto);
			if (vo.getPassword() == null) {
				map.put("result", "ERR");
				map.put("reason", "memberPwd Not Found");
				return map;
			}
			String id = vo.getEmail();
			int role = vo.getRole();
			String auth = null ;
			if (role == 1) {
				//member.get().setRole(Integer.valueOf("ROLE_MEMBER"));
				auth = "ROLE_MEMBER";
			} else if (role == 2) {
				//member.get().setRole(Integer.valueOf("ROLE_OWNER"));
				auth = "ROLE_OWNER";
			} else if (role == 3) {
				//member.get().setRole(Integer.valueOf("ROLE_UN_USER"));
				auth = "ROLE_UN_USER";
			}else if(role == 4) {
				//member.get().setRole(Integer.valueOf("ROLE_SOCIAL_USER"));
				auth = "ROLE_SOCIAL_USER";
			}
			
			member.get().builder().email(id).role(role);
			UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
			dto = jwtTokenProvider.generateMemberTokenDto(authenticationToken, auth);
			
			map.put("AccessToken", dto.getAccessToken());
			map.put("RefreshToken", dto.getRefreshToken());
			map.put("email", id);
			map.put("role", role);
		} catch (Exception e) {
			map.put("result", "ERR");
			map.put("reason", "delete member");
			return map;
		}
		return map;
	}

	// 현재 SecurityContext 에 있는 유저 정보 가져오기
	@Transactional(readOnly = true)
	public MemberResponseDto getMyInfo() {
		return memberRepository.findById(SecurityUtil.getCurrentMemberId()).map(MemberResponseDto::of)
				.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
	}

	public LoginMemberResponseDto memberSignInKakao(LoginMemberRequestKokao memberVo) throws Exception {
		LoginMemberResponseDto dto = new LoginMemberResponseDto();
		
		memberVo.setRole(4);
		Integer memberInfo = memberMapper.registerKakaoInfo(memberVo);
		
		if(memberInfo.toString().equals("")) {
			dto.setResult("ERR");
			dto.setReason("member insert fail");
			return dto;
		}
		
		dto = memberMapper.selectKakaoInfo(memberVo.getEmail());
		
		if(dto.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("select kakao info fail");
			return dto;
		}
		dto.setResult("OK");
		dto.setReason("");
		
		return dto;
	}

	public LoginMemberResponseDto memberSignInNaver(LoginMemberRequestNaver memberVo) {
		LoginMemberResponseDto dto = new LoginMemberResponseDto();
		
		memberVo.setRole(4);
		Integer memberInfo = memberMapper.registerNaverInfo(memberVo);
		
		if(memberInfo.toString().equals("")) {
			dto.setResult("ERR");
			dto.setReason("member insert fail");
			return dto;
		}
		
		dto = memberMapper.selectNaverInfo(memberVo.getEmail());
		
		if(dto.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("select kakao info fail");
			return dto;
		}
		dto.setResult("OK");
		dto.setReason("");
		
		return dto;
	}

	public LoginMemberResponseDto memberSignInGoogle(LoginMemberRequestGoogle memberVo) {
		LoginMemberResponseDto dto = new LoginMemberResponseDto();
			
		memberVo.setRole(4);
		Integer memberInfo = memberMapper.registerGoogleInfo(memberVo);
		
		if(memberInfo.toString().equals("")) {
			dto.setResult("ERR");
			dto.setReason("member insert fail");
			return dto;
		}
		
		dto = memberMapper.selectGoogleInfo(memberVo.getEmail());
		
		if(dto.getEmail().equals("")) {
			dto.setResult("ERR");
			dto.setReason("select kakao info fail");
			return dto;
		}
		dto.setResult("OK");
		dto.setReason("");
		
		return dto;
	}

	public MemberVo.MemberResponseDto MemberEditInfo(MemberVo.MemberUpdateInfo memberInfoVo)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		
		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto();
		
		String phone = memberInfoVo.getPhone_num();

		memberInfoVo.setPhone_num(aesUtil.encrypt(phone));

		int chk = memberMapper.updateMemberInfo(memberInfoVo);

		if (chk > 0) {
			dto.setResult("OK");
			dto.setReason("");
		} else {
			dto.setResult("ERR");
			dto.setReason("update Fail");
		}

		return dto;
	}

	public ViewMemberInfoResponseDto ViewMemberInfo(String email)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
//        ApiResponseVo result = new ApiResponseVo();
		ViewMemberInfoResponseDto result = new ViewMemberInfoResponseDto();
		MemberVo.MemberInfo memberInfoVo = new MemberVo.MemberInfo();

		memberInfoVo = memberMapper.viewMemberInfo(email);

		// 핸드폰 복호화
		memberInfoVo.setPhone_num(aesUtil.decrypt(String.valueOf(memberInfoVo.getPhone_num())));

		result.setResult("OK");
		result.setReason("");
		result.setEmail(memberInfoVo.getEmail());
		result.setName(memberInfoVo.getName());
		result.setPhone_num(memberInfoVo.getPhone_num());
		result.setRole(memberInfoVo.getRole());
		result.setInsert_user(memberInfoVo.getInsert_user());
		
		return result;
	}

	public MemberVo.MemberResponseDto MemberWithdraw(MemberDeleteVo deleteVo) {
		MemberVo.MemberResponseDto result = new MemberVo.MemberResponseDto();
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
					result.setResult("OK");
					result.setReason("delete Success");
				} else {
					result.setResult("ERR");
					result.setReason("delete Fail");
				}
			}
		}

		return result;
	}

	public MemberVo.MemberResponseDto FindByPasswd(MemberFindPwdRequest findPwdRequest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		
		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto();
		
		findPwdRequest.setPhone_num(aesUtil.encrypt(findPwdRequest.getPhone_num()));
		
		//String phone = memberMapper.findByPasswd(findPwdRequest.getEmail());
		//String phone_data = aesUtil.decrypt(phone);
		String email = memberMapper.checkEmailByPwd(findPwdRequest);
		
		if (email == null) {
			dto.setResult("ERR");
			dto.setReason("email Not Found");
			return dto;
		}

		Integer insert_user = memberMapper.selectMemberNum(email);

		System.out.println("insert_user = " + insert_user);
		
//		if (insert_user.toString().equals("")) {
//			dto.setResult("ERR");
//			dto.setReason("member_num Not Found");
//			return dto;
//		}

		String key = authUtil.CreateAuthNum();

		MemberEmailAuthInfo req = new MemberEmailAuthInfo();

		req.setEmail_auth_num(key);
		req.setInsert_user(insert_user);

		int data = memberMapper.updateEmailAuthInfo(req);

		if (data == 0) {
			dto.setResult("ERR");
			dto.setReason("auth Not Insert");
			return dto;
		}

		UtilVo.MailRequest mailSender = new MailRequest();

		mailSender.setReceiver(email);
		mailSender.setKey(key);
		// 수신자 메일정보 저장
		mailUtil.sendMail(mailSender);

		dto.setResult("OK");
		dto.setReason("mail send : " + email);

		return dto;
	}

	public MemberVo.MemberResponseDto UpdatePasswd(MemberUpdatePwdRequest updatePwdRequest) throws Exception {
		
		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto();
		
		Integer user_num = memberMapper.selectMemberNum(updatePwdRequest.getEmail());
		
		updatePwdRequest.setUser_num(user_num);
		String auth_num = memberMapper.checkEmailAuthInfo(updatePwdRequest);
		
		if(auth_num == null) {
			dto.setResult("ERR");
			dto.setReason("user_num Not Found");
			return dto;
		}
		
		updatePwdRequest.setPassword(shaUtil.encryptSHA512(updatePwdRequest.getPassword()));
		
		int updatePwd = memberMapper.updatePwdInfo(updatePwdRequest);
		
		if(updatePwd == 0) {
			dto.setResult("ERR");
			dto.setReason("update fail");
			return dto;
		}
		
		dto.setResult("OK");
		dto.setReason("");
		
		return dto;
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

	public MemberVo.MemberResponseDto MemberUpdatePasswd(MemberChangePwdRequest updatePwdRequest) throws Exception {
		
		MemberVo.MemberResponseDto dto = new MemberVo.MemberResponseDto(); 
		
		updatePwdRequest.setPassword(shaUtil.encryptSHA512(updatePwdRequest.getPassword()));
		
		//고객정보 확인
		Map<String, Object> memberInfo = memberMapper.updateCheckEmailByPwd(updatePwdRequest);
		
		if(memberInfo.get("email").equals("")) {
			dto.setResult("ERR");
			dto.setReason("select member check fail");
			return dto;
		}
		
		int update_user = Integer.parseInt((String) memberInfo.get("insert_user"));
		
		updatePwdRequest.setUpdate_user(update_user);
		
		System.out.println("data = " + updatePwdRequest.getUpdate_user());
		
		// 체크 됬다면 변경 진행
		updatePwdRequest.setUpdate_password(shaUtil.encryptSHA512(updatePwdRequest.getUpdate_password()));
		
		int update = memberMapper.updateMemberPwd(updatePwdRequest);
		
		if(update == 0) {
			dto.setResult("ERR");
			dto.setReason("update member password fail");
			return dto;
		}
		
		dto.setResult("OK");
		dto.setReason("");
		
		return dto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
