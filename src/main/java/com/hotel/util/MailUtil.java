package com.hotel.util;

import com.hotel.util.vo.UtilVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

    @Value("${spring.mail.username}")
    String Sender;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(UtilVo.MailRequest mailRequest){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(Sender);
        simpleMailMessage.setTo(mailRequest.getReceiver());
        simpleMailMessage.setSubject(mailRequest.getSubject());
        simpleMailMessage.setText(mailRequest.getText());
        javaMailSender.send(simpleMailMessage);
    }
}
