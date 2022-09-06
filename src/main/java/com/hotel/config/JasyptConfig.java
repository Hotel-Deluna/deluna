package com.hotel.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 설정파일중 보안 이슈있을 항목들을 암호화하는 Jasypt 라이브러리 설정
 */
@Configuration
public class JasyptConfig {
    @Value("${jasypt.encryptor.password}")
    private String encryptKey;

    @Bean("encryptorBean")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(1);
        encryptor.setPassword(encryptKey);
        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
        return encryptor;
    }
}
