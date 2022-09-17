package com.hotel.common.vo;

import lombok.*;


public class JwtTokenDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenDto {
        String grantType;
        String accessToken;
        String refreshToken;
        Long accessTokenExpiresIn;

    }
    @Getter
    @NoArgsConstructor
    public static class TokenRequestDto {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayLoadDto {
        int id;
        String user_role;
    }

}
