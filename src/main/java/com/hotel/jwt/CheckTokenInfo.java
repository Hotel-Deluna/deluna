package com.hotel.jwt;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.hotel.common.vo.JwtTokenDto.PayLoadDto;
import com.hotel.member.svc.MemberServiceImpl;
import com.hotel.util.SHA512Util;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckTokenInfo {

	private final JwtTokenProvider jwtTokenProvider;
	
	
	public String tokenInfo(String accessToken) {
		
		Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
		
		String email = auth.getName();
		
		return email;
		
	}
	
	public Integer tokenBusinessInfo(String accessToken) throws Exception {

		PayLoadDto auth = jwtTokenProvider.getPayload(accessToken);

		Integer num = auth.getId();
		System.out.println("num = " + auth.toString());
		return num;

	}

}
