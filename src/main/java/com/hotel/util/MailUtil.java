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
        simpleMailMessage.setSubject(mailRequest.getSubject());
        simpleMailMessage.setText(mailRequest.getText());
        javaMailSender.send(simpleMailMessage);
    }
}
