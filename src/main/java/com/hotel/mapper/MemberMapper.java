package com.hotel.mapper;

import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hotel.member.dto.MemberRequestDto;
import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.FindIdRequest;
import com.hotel.member.vo.MemberVo.LoginMemberResponseDto;
import com.hotel.member.vo.MemberVo.MemberChangePwdRequest;
import com.hotel.member.vo.MemberVo.MemberDeleteVo;
import com.hotel.member.vo.MemberVo.MemberEmailAuthInfo;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberInfo;
import com.hotel.member.vo.MemberVo.MemberUpdateInfo;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;
import com.hotel.member.vo.MemberVo.RegisterMemberUnMemberRequest;

@Mapper
@Repository
public interface MemberMapper {

	String checkByEmail(String email);
	
	int registerMemberInfo(MemberVo.RegisterMemberRequest memberVo);

	Map<String, Object> deleteMemberInfo(String email);

	int deleteMember(MemberDeleteVo deleteVo);

	int insertDeleteMember(MemberDeleteVo deleteVo);

	int updateMemberInfo(MemberUpdateInfo memberInfoVo);

	MemberInfo viewMemberInfo(String email);

	String findByEmail(FindIdRequest findIdRequest);

	String checkEmailByPwd(MemberFindPwdRequest findPwdRequest);

	int selectMemberNum(String email);

	int updateEmailAuthInfo(MemberEmailAuthInfo req);

	String checkEmailAuthInfo(MemberUpdatePwdRequest updatePwdRequest);

	int updatePwdInfo(MemberUpdatePwdRequest updatePwdRequest);

	int registerSocialInfo(MemberVo.LoginMemberRequestSocial memberVo);

	LoginMemberResponseDto selectKakaoInfo(String string);

	//Integer registerNaverInfo(LoginMemberRequestNaver memberVo);

	LoginMemberResponseDto selectNaverInfo(String email);

	//Integer registerGoogleInfo(LoginMemberRequestGoogle memberVo);

	LoginMemberResponseDto selectGoogleInfo(String email);

	//고객 비빌번호 변경 전용
	Map<String, Object> updateCheckEmailByPwd(MemberChangePwdRequest updatePwdRequest);

	int updateMemberPwd(MemberChangePwdRequest updatePwdRequest);

	String checkPhoneNum(String phone_num);

	String findByPasswd(String email);

	MemberRequestDto findByPassword(MemberRequestDto memberRequestDto);

	String loginByEmail(String email);

	Map<String, Object> updateCheckOwnerEmailByPwd(MemberChangePwdRequest updatePwdRequest);

	int registerUnMemberInfo(RegisterMemberUnMemberRequest unMemberVo);

	// 사업자 로그인
	Integer getOwnerNum (MemberRequestDto memberRequestDto);

	int registerMemberUnMemberInfo(RegisterMemberUnMemberRequest unMemberVo);

}
