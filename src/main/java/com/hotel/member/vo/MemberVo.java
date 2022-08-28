package com.hotel.member.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.common.CommonResponseVo;
import com.hotel.owner.vo.OwnerVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MemberVo {

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

		@Schema(description = "고객 전화번호", required = true, example = "0212345678")
		String phone_num;

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;

		@Schema(description = "인증번호", required = true, example = "abcde")
		String auth;

		@Schema(description = "비밀번호", required = true, example = "123456")
		String password;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객 로그인 파라미터")
	public static class LoginMemberRequest {

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;

		@Schema(description = "비밀번호", required = true, example = "123456")
		String password;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객 카카오 로그인 파라미터")
	public static class LoginMemberRequestKokao {

		@Schema(description = "클라이언트 키", required = true, example = "백엔드처리(REST APIKey)")
		String client_id;

		@Schema(description = "리다이렉트url", required = true, example = "리다이렉트할 페이지 정보")
		String redirect_uri;

		@Schema(description = "response_type", required = true, example = "code 로 고정")
		String response_type;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객 네이버 로그인 파라미터")
	public static class LoginMemberRequestNaver {

		@Schema(description = "클라이언트 키", required = true, example = "백엔드처리(REST APIKey)")
		String client_id;
		
		@Schema(description = "클라이언트 시크릿 키", required = true, example = "백엔드처리(시크릿 Key)")
		String client_secret;

		@Schema(description = "리다이렉트url", required = true, example = "리다이렉트할 페이지 정보")
		String redirect_uri;

	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "고객 구글 로그인 파라미터")
	public static class LoginMemberRequestGoogle {

		@Schema(description = "클라이언트 키", required = true, example = "백엔드처리(REST APIKey)")
		String client_id;
		
		@Schema(description = "클라이언트 시크릿 키", required = true, example = "백엔드처리(시크릿 Key)")
		String client_secret;

		@Schema(description = "리다이렉트url", required = true, example = "리다이렉트할 페이지 정보")
		String redirect_uri;
		
		@Schema(description = "response_type", required = true, example = "인증코드 반환여부 확인")
		String response_type;
		
		@Schema(description = "동의범위", required = true, example = "Oauth 동의 법위")
		String scope;
		
		@Schema(description = "엑세스토큰을 새로고칠수 있는지 여부", required = true, example = "액세스토큰 새로코칠수 있는지 여부")
		String accessType;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ApiModel(description = "고객 정보")
	public static class MemberInfo {

		@Schema(description = "한글 이름", required = true, example = "홍길동")
		String name;

		@Schema(description = "고객 전화번호", required = true, example = "0212345678")
		String phone_num;

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ApiModel(description = "고객 정보 변경")
	public static class MemberUpdateInfo {

		@Schema(description = "한글 이름", required = true, example = "홍길동수정")
		String name;

		@Schema(description = "고객 전화번호", required = true, example = "0212345678")
		String phone_num;

		@Schema(description = "고객 아이디(이메일)", required = true, example = "example@naver.com")
		String email;

		@Schema(description = "인증번호", required = true, example = "441321")
		String auth;

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
	public static class MemberWithdrawRequest {
		@Schema(description = "고객 탈퇴 사유", required = true, example = "사용안함111")
		String reason;

		@JsonIgnore
		String ignores;
	}


}
