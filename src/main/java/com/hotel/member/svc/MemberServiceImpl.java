package com.hotel.member.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.LoginMemberRequest;
import com.hotel.member.vo.MemberVo.LoginMemberRequestGoogle;
import com.hotel.member.vo.MemberVo.LoginMemberRequestKokao;
import com.hotel.member.vo.MemberVo.LoginMemberRequestNaver;
import com.hotel.owner.vo.*;
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
        result.setMessage("사업자 정보 수정 완료");
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
        result.setMessage("고객 정보 조회 완료");
        result.setData(memberInfoVo);
        return result;
    }
    
    @Override
    public CommonResponseVo MemberWithdraw(MemberVo.MemberWithdrawRequest memberWithdrawRequest) {
        CommonResponseVo result = new CommonResponseVo();
        // 사업자 회원 탈퇴처리
        // ownerWithdrawReasonVo 사유 DB 저장
        result.setMessage("사업자 회원 탈퇴 완료");

        return result;
    }

	



	

	
}
