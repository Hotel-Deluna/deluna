package com.hotel.member.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotel.member.vo.Member;
import com.hotel.member.vo.Member.Authority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

	private String email;
	private String password;
	private int role;

	public Member toMember(PasswordEncoder passwordEncoder) {
		if (role == 1) {
			return Member.builder().email(email).password(passwordEncoder.encode(password))
					.authority(Authority.ROLE_OWNER).build();
		} else if (role == 2) {
			return Member.builder().email(email).password(passwordEncoder.encode(password))
					.authority(Authority.ROLE_UN_MEMBER).build();
		}
		return Member.builder().email(email).password(passwordEncoder.encode(password))
				.authority(Authority.ROLE_MEMBER).build();
	}

	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}
}
