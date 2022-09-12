package com.hotel.member.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.member.vo.Member;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long>{
	
	// 이메일 찾기
	Optional<Member> findByEmail(String email);

	//중복 가입 방지
	boolean existsByEmail(String email);
	
	
}
