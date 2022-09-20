package com.hotel;

import com.hotel.company.dto.HotelMapper;
import com.hotel.util.MailUtil;
import com.hotel.util.vo.UtilVo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@PropertySource("application.yml")
class DelunaApplicationTests {

	@Value("${jasypt.encryptor.password}")
	private String encryptKey;

	@Autowired
	MailUtil mailUtil;

	@Autowired
	HotelMapper hotelMapper;

//	@Test
//	void JasyptTest() {
//
//		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//		encryptor.setProvider(new BouncyCastleProvider());
//		encryptor.setPoolSize(1);
//		encryptor.setPassword(encryptKey);
//		encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
//
//		String plainText = "kys"; // 암호화 할 내용
//		String encryptedText = encryptor.encrypt(plainText); // 암호화
//		System.out.println("ENC("+encryptedText+")"); // application.yml에 넣을 내용
//	}
//
//	@Test
//	void MailSend() {
//
//		// 이메일 DB에서 조회 후 이미 있으면 true, 없으면 false 리턴
//		UtilVo.MailRequest mailRequest = new UtilVo.MailRequest();
//
//		mailRequest.setReceiver("surfingboy0914@gmail.com");
//		mailRequest.setSubject("SMTP TEST");
//		mailRequest.setText("메일 테스트 !@#$");
//
//		mailUtil.sendMail(mailRequest);
//	}

//	@Test
//	void testTest() {
//		Calendar cal = Calendar.getInstance();
//      cal.set(2022, 9, 9);
//		Date now = new Date(cal.getTimeInMillis());
//		log.info(String.valueOf(now));
//		log.info(hotelMapper.selectHoliday("2022-09-09"));
//
//	}

}
