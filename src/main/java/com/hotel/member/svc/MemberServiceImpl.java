package com.hotel.member.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.LoginMemberRequest;
import com.hotel.member.vo.MemberVo.LoginMemberRequestGoogle;
import com.hotel.member.vo.MemberVo.LoginMemberRequestKokao;
import com.hotel.member.vo.MemberVo.LoginMemberRequestNaver;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

	@Override
	public CommonResponseVo MemberSignUp(MemberVo.RegisterMemberRequest memberVo) {

		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("회원 가입 완료");

		return result;
	}

	@Override
	public CommonResponseVo MemberSignIn(LoginMemberRequest memberVo) {

		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("로그인 성공!!");

		return result;
	}

	@Override
	public CommonResponseVo memberSignInKakao(LoginMemberRequestKokao memberVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("code정보");

		return result;
	}

	@Override
	public CommonResponseVo memberSignInNaver(LoginMemberRequestNaver memberVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("code정보");

		return result;
	}

	@Override
	public CommonResponseVo memberSignInGoogle(LoginMemberRequestGoogle memberVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("code정보, 수정 예정");

		return result;
	}

	@Override
	public CommonResponseVo MemberEditInfo(MemberVo.MemberUpdateInfo memberInfoVo) {
		CommonResponseVo result = new CommonResponseVo();
		result.setMessage("고객정보 수정 완료");
		return result;
	}

	@Override
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

	@Override
	public CommonResponseVo MemberWithdraw(MemberVo.MemberDeleteRequest memberWithdrawRequest) {
		CommonResponseVo result = new CommonResponseVo();
		// 사업자 회원 탈퇴처리
		// ownerWithdrawReasonVo 사유 DB 저장
		result.setMessage("사업자 회원 탈퇴 완료");
		return result;
	}

	@Override
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

	@Override
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

}
