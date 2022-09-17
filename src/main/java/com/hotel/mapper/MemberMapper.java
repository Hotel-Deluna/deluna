package com.hotel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hotel.member.vo.MemberVo;

@Mapper
@Repository
public interface MemberMapper {

	String findByEmail(String email);
	
	int registerMemberInfo(MemberVo.RegisterMemberRequest memberVo);
	
}
