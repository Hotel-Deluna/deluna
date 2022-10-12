package com.hotel.exception;
import com.hotel.exception.status.UnauthorizedException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ExceptionController {

    @RequestMapping("/exception/ExpiredToken")
    public void ExpiredToken(){
        throw new UnauthorizedException();
    }

    @RequestMapping("/exception/MalformedJwt")
    public void MalformedJwt(){
        throw new MalformedJwtException("");
    }

    @RequestMapping("/exception/UnsupportedJwt")
    public void UnsupportedJwt(){
        throw new UnsupportedJwtException("");
    }

    @RequestMapping("/exception/IllegalArgumentJwt")
    public void IllegalArgumentJwt(){
        throw new IllegalArgumentException();
    }
}
