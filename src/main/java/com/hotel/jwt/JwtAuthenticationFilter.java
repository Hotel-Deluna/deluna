package com.hotel.jwt;


import com.hotel.exception.status.ExceptionMessage;
import com.hotel.exception.status.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.Key;

@RequiredArgsConstructor
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider tokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = request.getHeader(AUTHORIZATION_HEADER);

        try{
            boolean isValid = tokenProvider.validateToken(jwt);
            filterChain.doFilter(request, response);

        // 잘못된 토큰 혹은 토큰만료시 예외처리
        }catch (MalformedJwtException e){
            RequestDispatcher dp=request.getRequestDispatcher("/exception/MalformedJwt");
            dp.forward(request, response);
        }catch (UnsupportedJwtException e){
            RequestDispatcher dp=request.getRequestDispatcher("/exception/UnsupportedJwt");
            dp.forward(request, response);
        }catch (UnauthorizedException e){
            RequestDispatcher dp=request.getRequestDispatcher("/exception/ExpiredToken");
            dp.forward(request, response);
        }
        // IllegalArgumentException은 스웨거 첫페이지 진입시 전역 JWT토큰문제때문에 일단 주석처리..
//        catch (IllegalArgumentException e){
//            RequestDispatcher dp=request.getRequestDispatcher("/exception/IllegalArgumentJwt");
//            dp.forward(request, response);
//        }

    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
