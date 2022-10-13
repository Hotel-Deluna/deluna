package com.hotel.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.common.vo.JwtTokenDto;
import com.hotel.exception.status.ExceptionMessage;
import com.hotel.exception.status.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Slf4j
@Component
@PropertySource(value = {"classpath:application.yml"})
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 1;  // 30일

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //재발급에 사용
    public JwtTokenDto.TokenDto generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Map<String, Object> claimMap = (Map<String, Object>) authentication.getDetails();

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .claim("id", claimMap.get("id"))
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtTokenDto.TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    //추가 - 2022/09/21 한동희
    //고객용 발급 사용
    public JwtTokenDto.TokenDto generateMemberTokenDto(Authentication authentication, String role) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if(authorities.equals("")) {
        	authorities = role;
        }

        Map<String, Object> test = new HashMap<>();
        test.put("auth", authorities);
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtTokenDto.TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    //사업자 발급 사용
    public JwtTokenDto.TokenDto generateMemberTokenDto(Authentication authentication, String role, int id) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if(authorities.equals("")) {
            authorities = role;
        }

        Map<String, Object> test = new HashMap<>();
        test.put("auth", authorities);
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .claim("id", id)
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtTokenDto.TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }


    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명이거나 올바른 토큰이 아닙니다.");
            throw new MalformedJwtException(ExceptionMessage.MalformedToken.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new UnauthorizedException(ExceptionMessage.ExpiredToken.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new UnsupportedJwtException(ExceptionMessage.MalformedToken.getMessage());
        } catch (IllegalArgumentException e) {
//            log.info("JWT 토큰이 잘못되었습니다.");
//            throw new IllegalArgumentException(ExceptionMessage.VerifyFailToken.getMessage());
        }
        return false;
    }

    public JwtTokenDto.PayLoadDto getPayload(String accessToken) throws Exception{
        JwtTokenDto.PayLoadDto result = new JwtTokenDto.PayLoadDto();
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        result.setId((Integer) claims.get("id"));

        return result;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
