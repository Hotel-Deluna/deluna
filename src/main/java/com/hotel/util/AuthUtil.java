package com.hotel.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AuthUtil {
    public String CreateAuthNum(){
        //6자리 인증키 생성, String 으로 변환
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }
}
