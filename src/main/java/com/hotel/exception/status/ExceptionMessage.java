package com.hotel.exception.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionMessage {

    IsRequiredToken("UnauthorizedException", "전달된 토큰이 없습니다."),
    AuthVerifyAccessDenied("UnauthorizedException", "접근 권한이 없습니다."),
    VerifyFailToken("UnauthorizedException", "유효한 토큰이 아닙니다."),
    ExpiredToken("UnauthorizedException", "토큰이 만료되었습니다."),
    UnsupportedToken("UnauthorizedException", "토큰이 암호화되어 있지 않습니다."),
    MalformedToken("UnauthorizedException", "토큰이 올바르게 구성되지 않았습니다."),
    SignatureVerifyToken("UnauthorizedException", "토큰의 서명이 유효하지 않습니다."),
    IllegalArgumentToken("UnauthorizedException", "토큰의 값이 비어있습니다.");

    private String type;
    private String message;
}
