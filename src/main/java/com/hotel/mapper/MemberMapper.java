package com.hotel.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hotel.member.vo.MemberVo;
import com.hotel.member.vo.MemberVo.FindIdRequest;
import com.hotel.member.vo.MemberVo.MemberDeleteRequest;
import com.hotel.member.vo.MemberVo.MemberDeleteVo;
import com.hotel.member.vo.MemberVo.MemberEmailAuthInfo;
import com.hotel.member.vo.MemberVo.MemberFindPwdRequest;
import com.hotel.member.vo.MemberVo.MemberInfo;
import com.hotel.member.vo.MemberVo.MemberUpdateInfo;
import com.hotel.member.vo.MemberVo.MemberUpdatePwdRequest;

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

	String selectMemberNum(String email);

	int updateEmailAuthInfo(MemberEmailAuthInfo req);

	String checkEmailAuthInfo(MemberUpdatePwdRequest updatePwdRequest);

	int updatePwdInfo(MemberUpdatePwdRequest updatePwdRequest);

}
