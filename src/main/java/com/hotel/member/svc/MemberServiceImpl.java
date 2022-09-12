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
import com.hotel.member.vo.MemberVo.LoginMemberRequestGoogle;
import com.hotel.member.vo.MemberVo.LoginMemberRequestKokao;
import com.hotel.member.vo.MemberVo.LoginMemberRequestNaver;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;
import com.hotel.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final JwtTokenProvider jwtTokenProvider;

	private final MemberRepository memberRepository;
	
	private final MemberMapper memberMapper;

	public CommonResponseVo MemberSignUp(MemberVo.RegisterMemberRequest memberVo) {

		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("회원 가입 완료");
		
		String findByEmail = memberMapper.findByEmail(memberVo.getEmail());
		
		// 이메일 중복여부 체크
		// 비밀번호 암호화 후 db 인서트
		try {
			if(findByEmail == null) {
				
				//비밀번호 암호화
				// 추가 예정
				
				System.out.println("memberVo = " + memberVo.toString());
				int registerMember = memberMapper.registerMemberInfo(memberVo);
				
				System.out.println("registerMember = " + registerMember);
				
				if(registerMember > 0) {
					result.setMessage("회원가입이 완료되었습니다.");
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	// 이메일 조회
	@Transactional(readOnly = true)
	public TokenDto getMemberInfo(MemberRequestDto memberRequestDto) {
		
		TokenDto dto = new TokenDto();
		
		try {
			Optional<Member> member = memberRepository.findByEmail(memberRequestDto.getEmail());

			if (member.get().getEmail().equals(null) || member.get().getEmail().equals("")) {
				return null;
			}
			
			String id = member.get().getEmail();
			System.out.println("id = " + id);
			UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
			System.out.println("authenticationToken = " + authenticationToken.toString());
			
			dto = jwtTokenProvider.generateTokenDto(authenticationToken);
			System.out.println("dto = " + dto.toString());
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return dto;
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

	public CommonResponseVo MemberEditInfo(MemberVo.MemberUpdateInfo memberInfoVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("고객정보 수정 완료");
		return result;
	}

	public MemberVo.MemberInfoResponse ViewMemberInfo() {
//        ApiResponseVo result = new ApiResponseVo();
		MemberVo.MemberInfoResponse result = new MemberVo.MemberInfoResponse();
		MemberVo.MemberInfo memberInfoVo = new MemberVo.MemberInfo();

		memberInfoVo.setEmail("abc@member.com");
		memberInfoVo.setName("홍길동");
		memberInfoVo.setPhone_num("01098765432");
		result.setMessage("고객정보 조회 완료");
		result.setData(memberInfoVo);
		return result;
	}

	public CommonResponseVo MemberWithdraw(MemberVo.MemberDeleteRequest memberWithdrawRequest) {
		CommonResponseVo result = new CommonResponseVo();
		// 사업자 회원 탈퇴처리
		// ownerWithdrawReasonVo 사유 DB 저장
		result.setMessage("사업자 회원 탈퇴 완료");
		return result;
	}

	public CommonResponseVo FindPasswdEmail(MemberFindPwdRequest findPwdRequest) {
		CommonResponseVo result = new CommonResponseVo();
		if (findPwdRequest.getEmail().equals("")) {
			result.setMessage("email 값 없음");
			return result;
		} else if (findPwdRequest.getPhone_auth_num().equals("")) {
			result.setMessage("인증번호 값 없음");
			return result;
		}
		result.setMessage("이메일 전송 완료(변경 페이지 url 송부 및 인증번호 발송)");

		return result;
	}

	public CommonResponseVo UpdatePasswd(MemberUpdatePwdRequest updatePwdRequest) {
		CommonResponseVo result = new CommonResponseVo();
		if (updatePwdRequest.getPassword().equals("")) {
			result.setMessage("password 값 없음");
			return result;
		} else if (updatePwdRequest.getEmail_auth_num().equals("")) {
			result.setMessage("인증번호 값 없음");
			return result;
		}
		result.setMessage("비밀번호 변경이 완료되었습니다.");

		return result;
	}

	public MemberVo.FindIdResponse FindId(MemberVo.FindIdRequest findIdRequest) {
		MemberVo.FindIdResponse result = new MemberVo.FindIdResponse();
		MemberVo.IdInfo idInfo = new MemberVo.IdInfo();
		// 첫 4글자 제외한 나머지 글자 블러처리 (@ 전까지)
		idInfo.setEmail("abcd**@hotel.com");

		result.setMessage("아이디 조회 완료");
		result.setData(idInfo);

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
