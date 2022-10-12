package com.hotel.exception;

import com.hotel.exception.status.ExceptionMessage;
import com.hotel.exception.status.UnauthorizedException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UnauthorizedException.class})
    public Object ExpiredToken(Exception e) {
        Map<String, String> result = new HashMap<>();
        result.put("result", "ERROR");
        result.put("reason", ExceptionMessage.ExpiredToken.getCode());
        return result;
    }

    @ExceptionHandler(value = { MalformedJwtException.class})
    public Object MalformedJwt(Exception e) {
        Map<String, String> result = new HashMap<>();
        result.put("result", "ERROR");
        result.put("reason", ExceptionMessage.MalformedToken.getCode());
        return result;
    }

    @ExceptionHandler(value = { UnsupportedJwtException.class})
    public Object UnsupportedJwt(Exception e) {
        Map<String, String> result = new HashMap<>();
        result.put("result", "ERROR");
        result.put("reason", ExceptionMessage.UnsupportedToken.getCode());
        return result;
    }

//    @ExceptionHandler(value = { IllegalArgumentException.class})
//    public Object IllegalArgumentJwt(Exception e) {
//        Map<String, String> result = new HashMap<>();
//        result.put("result", "ERROR");
//        result.put("reason", ExceptionMessage.VerifyFailToken.getMessage());
//        return result;
//    }

}
