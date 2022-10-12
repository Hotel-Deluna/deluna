package com.hotel.exception.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionMessage {
    ExpiredToken("UnauthorizedException", "토큰이 만료되었습니다.", "JWT-0001"),
    MalformedToken("UnauthorizedException", "토큰이 올바르게 구성되지 않았습니다.", "JWT-0002"),
    UnsupportedToken("UnauthorizedException", "지원되지 않는 유형의 토큰입니다.", "JWT-0003"),
    VerifyFailToken("UnauthorizedException", "유효한 토큰이 아닙니다.", "JWT-0004"),
    SignatureVerifyToken("UnauthorizedException", "토큰의 서명이 유효하지 않습니다.", "JWT-0005")
    ;
//    IsRequiredToken("UnauthorizedException", "전달된 토큰이 없습니다."),
//    AuthVerifyAccessDenied("UnauthorizedException", "접근 권한이 없습니다."),

//    IllegalArgumentToken("UnauthorizedException", "토큰의 값이 비어있습니다.");

    private String type;
    private String message;
    private String code;
}
