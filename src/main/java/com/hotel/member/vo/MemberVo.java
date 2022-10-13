package com.hotel.member.vo;

import java.util.Collection;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

import com.hotel.common.CommonResponseVo;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

@Getter
@NoArgsConstructor
@Table(name="member")
public class MemberVo {
	
	@Id
	@Column(value = "member_num")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer member_num;
	
	private String email;
	
	private String password;
	
	private String phone_num;
	
	private Collection<GrantedAuthority> authorities ;
	
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "Response")
	public static class MemberInfoResponse extends CommonResponseVo {
		@Schema(description = "데이터")
		MemberVo.MemberInfo data;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "고객 회원가입 파라미터")
	public static class RegisterMemberRequest {
		@Schema(description = "한글 이름", required = true, example = "홍길동")
		String name;

		@Schema(description = "휴대폰 번호", required = true, example = "01012345678")
		String phone_num;

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;

		//@Schema(description = "휴대폰 인증번호", required = true, example = "123456")
		//String phone_auth_num;

		@Schema(description = "비밀번호", required = true, example = "비밀번호 입력")
		String password;
		
		@Schema(description = "member_num", required = true, example = "백엔드 사용")
		Integer member_num;

	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "고객 회원가입 파라미터")
	public static class RegisterMemberUnMemberRequest {
		@Schema(description = "한글 이름", required = true, example = "홍길동")
		String name;

		@Schema(description = "휴대폰 번호", required = true, example = "01012345678")
		String phone_num;

		@Schema(description = "role", required = true, example = "백엔드 사용")
		Integer role;
		
		@Schema(description = "member_num", required = true, example = "백엔드 사용")
		Integer member_num;
		
		@Schema(description = "reservation_num", required = true, example = "백엔드 사용")
		Integer reservation_num;
		
	}

//	@Data
//	@NoArgsConstructor
//	@AllArgsConstructor
//	@Schema(description = "공통 로그인 파라미터")
//	public static class LoginMemberRequest {
//
//		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
//		String email;
//
//		@Schema(description = "비밀번호", required = true, example = "123456")
//		String password;
//
//		@Schema(description = "role", required = true, example = "고객:1, 사업자:2")
//		String role;
//
//	}
	

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객 카카오 로그인 파라미터")
	public static class LoginMemberRequestKokao {

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;
		
		@Schema(description = "고객 이름", required = true, example = "이름 or 닉네임")
		String name;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객 네이버 로그인 파라미터")
	public static class LoginMemberRequestNaver {

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;
		
		@Schema(description = "고객 이름", required = true, example = "이름 or 닉네임")
		String name;

	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객 구글 로그인 파라미터")
	public static class LoginMemberRequestGoogle {

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;
		
		@Schema(description = "고객 이름", required = true, example = "이름 or 닉네임")
		String name;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ApiModel(description = "고객 정보")
	public static class MemberInfo {

		@Schema(description = "한글 이름", required = true, example = "홍길동")
		String name;

		@Schema(description = "휴대폰 번호", required = true, example = "0212345678")
		String phone_num;

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;
		
		@Schema(description = "role", required = true, example = "고객:1, 사업자:2, 비회원:3")
		Integer role;
		
		@Schema(description = "insert_user", required = true, example = "role + PK")
		String insert_user;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ApiModel(description = "고객 정보 변경")
	public static class MemberUpdateInfo {

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;
		
		@Schema(description = "한글 이름", required = true, example = "홍길동수정")
		String name;

		@Schema(description = "휴대폰 번호", required = true, example = "0212345678")
		String phone_num;

		

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ApiModel(description = "고객 로그인 정보")
	public static class MemberLoginInfo {

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;

		@Schema(description = "비밀번호", required = true, example = "123456")
		String password;

	}
	
	@Data
	@AllArgsConstructor
	@Schema(description = "고객 탈퇴 사유")
	public static class MemberDeleteRequest {
		public MemberDeleteRequest() {
			
		}
		@Schema(description = "고객 탈퇴 사유", required = true, example = "value=1 (타 사이트 이용)")
		int reason;
	}
	
	
	

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MemberDeleteVo {

		Integer member_num;
		
		String email;
		
		int content;
		
		String update_user;
		
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "공통 비밀번호 찾기 파라미터")
	public static class MemberFindPwdRequest {
		@Schema(description = "이메일", required = true, example = "example@naver.com")
		String email;

		@Schema(description = "회원이름", required = true, example = "홍길동")
		String name;

		@Schema(description = "휴대폰 번호", required = true, example = "01012345678")
		String phone_num;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "공통 비밀번호 재설정 파라미터")
	public static class MemberUpdatePwdRequest {

		@Schema(description = "비밀번호", required = true, example = "비밀번호 입력")
		String password;

		@Schema(description = "이메일 인증번호", required = true, example = "123456")
		String email_auth_num;
		
		@Schema(description = "이메일", required = true, example = "백엔드사용")
		String email;
		
		@Schema(description = "insert_user", required = true, example = "백엔드사용")
		Integer user_num;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "공통 비밀번호 변경 파라미터")
	public static class MemberChangePwdRequest {

		@Schema(description = "현재 비밀번호", required = true, example = "1234")
		String password;

		@Schema(description = "변경할 비밀번호", required = true, example = "a1234")
		String update_password;
		
		@Schema(description = "이메일", required = true, example = "백엔드사용")
		String email;
		
		@Schema(description = "일반회원, 사업자 구분값 - 1: 일반회원 2: 사업자",  required = true, example = "1")
		Integer role;
		
		@Schema(description = "update_user", required = true, example = "백엔드사용")
		Integer update_user;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MemberEmailAuthInfo {

		Integer insert_user;

		String email_auth_num;
	}

//	@Data
//	@AllArgsConstructor
//	@NoArgsConstructor
//	@Schema(description = "아이디찾기 응답값")
//	public static class FindIdResponse extends CommonResponseVo {
//		@Schema(description = "데이터")
//		IdInfo data;
//	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(description = "아이디찾기 파라미터")
	public static class FindIdRequest {
	//	@Schema(description = "일반회원, 사업자 구분값 - 1: 일반회원 2: 사업자",  required = true, example = "1")
	//	Integer role;

		@Schema(description = "이름",  required = true, example = "홍길동")
		String name;

		@Schema(description = "핸드폰 번호",  required = true, example = "01012345678")
		String phone_num;
	}
	
	

//	@Data
//	@AllArgsConstructor
//	@NoArgsConstructor
//	@ApiModel(description = "아이디(이메일) 정보")
//	public static class IdInfo {
//		@Schema(description = "이메일", required = true, example = "abc@hotel.com")
//		String email;
//	}
	
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "공통 로그인 response")
	public static class LoginMemberResponseDto {
		
		@Schema(description = "result", required = true, example = "결과값 OK or ERR")
		String result;

		@Schema(description = "reason", required = true, example = "내용")
		String reason;


		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;

		@Schema(description = "role", required = true, example = "고객:1, 사업자:2, 비회원:3, // ERR : 0")
		Integer role;

	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객정보 조회 response")
	public static class ViewMemberInfoResponseDto {
		
		@Schema(description = "result", required = true, example = "결과값 OK or ERR")
		String result;

		@Schema(description = "reason", required = true, example = "내용")
		String reason;

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;
		
		@Schema(description = "고객 이름", required = true, example = "example@naver.com")
		String name;
		
		@Schema(description = "고객 핸드폰번호", required = true, example = "01012345678")
		String phone_num;

		@Schema(description = "role", required = true, example = "고객:1, 사업자:2, 비회원:3")
		Integer role;
		
		@Schema(description = "insert_user", required = true, example = "role + PK")
		String insert_user;

	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ApiModel(description = "api 응답값 response")
	public static class MemberResponseDto {

		@Schema(description = "result", required = true, example = "결과값 OK or ERR")
		String result;

		@Schema(description = "reason", required = true, example = "내용")
		String reason;

		

	}

}
