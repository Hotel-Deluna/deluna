package com.hotel.member.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

	private String email;
	private String password;
	private int role;

//	public Member toMember() {
//		if (role == 1) {
//			return Member.builder().email(email).password(passwordEncoder.encode(password))
//					.authority(Authority.ROLE_OWNER).build();
//		} else if (role == 2) {
//			return Member.builder().email(email).password(passwordEncoder.encode(password))
//					.authority(Authority.ROLE_UN_MEMBER).build();
//		}
//		return Member.builder().email(email).password(passwordEncoder.encode(password))
//				.authority(Authority.ROLE_MEMBER).build();
//	}

	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}
}
