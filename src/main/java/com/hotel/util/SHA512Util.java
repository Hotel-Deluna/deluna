package com.hotel.util;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class SHA512Util {
    /**
     * SHA512 암호화처리
     * @param text
     * @return
     * @throws Exception
     */
    public String encryptSHA512(String text) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        //알고리즘을 이용해서 byte단위로 암호화 처리
        byte[] bytes=text.getBytes(Charset.forName("UTF-8"));
        md.update(bytes);
        return Base64.getEncoder().encodeToString(md.digest());
    }
}
