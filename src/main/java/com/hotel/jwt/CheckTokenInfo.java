package com.hotel.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

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
	
}
