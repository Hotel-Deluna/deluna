package com.hotel.member.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;

public interface MemberService {
    CommonResponseVo MemberSignUp(MemberVo.RegisterMemberRequest memberVo);
    
    CommonResponseVo MemberSignIn(MemberVo.LoginMemberRequest memberVo);
    
    CommonResponseVo memberSignInKakao(MemberVo.LoginMemberRequestKokao memberVo);
    
    CommonResponseVo memberSignInNaver(MemberVo.LoginMemberRequestNaver memberVo);
    
    CommonResponseVo memberSignInGoogle(MemberVo.LoginMemberRequestGoogle memberVo);
    
    CommonResponseVo MemberEditInfo(MemberVo.MemberUpdateInfo memberInfoVo);

    MemberVo.MemberInfoResponse ViewMemberInfo();

    CommonResponseVo MemberWithdraw(MemberVo.MemberDeleteRequest memberWithdrawRequest);

	CommonResponseVo FindPasswdEmail(MemberFindPwdRequest findPwdRequest);

	CommonResponseVo UpdatePasswd(MemberUpdatePwdRequest updatePwdRequest);

}
