package com.hotel.util;

import com.hotel.util.vo.UtilVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application.yml")
public class MailUtil {

    @Value("${spring.mail.username}")
    String Sender;

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 메일전송
     * @param mailRequest : 받는사람 메일주소, 제목, 내용
     */
    public void sendMail(UtilVo.MailRequest mailRequest){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(Sender);
        simpleMailMessage.setTo(mailRequest.getReceiver());
        simpleMailMessage.setSubject("호텔 델루나 비밀번호 변경");
        
        // 메일 양식 작성
        StringBuffer sb = new StringBuffer();
  	    sb.append("<html><body>");
  		sb.append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>");
  		sb.append("<h1>"+"비밀번호 변경"+"<h1><br>");
  		sb.append("비밀번호 변경할 url 정보를 보내드립니다. 클릭 후 비밀번호 변경 바랍니다.<br><br>");
  		sb.append("localhost Test(삭제예정)<br><br>");
  		sb.append("<a href='http://localhost:3000/auth/changePassword'>");
  		sb.append("<img  src='http://image.kyobobook.co.kr/images/book/xlarge/425/x9788956746425.jpg' /> </a><br>");
  		sb.append("</a>");
  		sb.append("prod test<br><br>");
  		sb.append("<a href='http://52.8.235.242:3000/auth/changePassword'>");
  		sb.append("<img  src='http://image.kyobobook.co.kr/images/book/xlarge/425/x9788956746425.jpg' /> </a><br>");
  		sb.append("</a>");
  		sb.append("인증키 6자리");
  		sb.append(mailRequest.getKey());
  		sb.append("</body></html>");
  		String str=sb.toString();
  		simpleMailMessage.setText(str);
        
  		try {
  			javaMailSender.send(simpleMailMessage);
		} catch (Exception e) {
			
		}
  		
        
        
        
    }
}
