package com.hotel.member.svc;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hotel.common.CommonResponseVo;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;

import io.swagger.v3.oas.annotations.servers.Server;

public interface MemberService {
    CommonResponseVo MemberSignUp(MemberVo.RegisterMemberRequest memberVo);
    
    CommonResponseVo MemberSignIn(MemberVo.LoginMemberRequest memberVo);
    
    CommonResponseVo memberSignInKakao(MemberVo.LoginMemberRequestKokao memberVo);
    
    CommonResponseVo memberSignInNaver(MemberVo.LoginMemberRequestNaver memberVo);
    
    CommonResponseVo memberSignInGoogle(MemberVo.LoginMemberRequestGoogle memberVo);
    
    CommonResponseVo MemberEditInfo(MemberVo.MemberUpdateInfo memberInfoVo);

    MemberVo.MemberInfoResponse ViewMemberInfo();

    MemberVo.FindIdResponse FindId(MemberVo.FindIdRequest findIdRequest);
    
    CommonResponseVo EditPassword(MemberVo.EditPasswordRequest editPasswordRequest);

    CommonResponseVo MemberWithdraw(MemberVo.MemberDeleteRequest memberWithdrawRequest);

    CommonResponseVo FindPasswdEmail(MemberFindPwdRequest findPwdRequest);

    CommonResponseVo UpdatePasswd(MemberUpdatePwdRequest updatePwdRequest);

}
