package com.hotel.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.util.vo.UtilVo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class SMSUtil {

    @Value("${naver.sms.senderPhoneNum}")
    String senderPhoneNum;

    @Value("${naver.sms.accessKey}")
    String accessKey;

    @Value("${naver.sms.serviceId}")
    String serviceId;

    @Value("${naver.sms.secretKey}")
    String secretKey;

    /**
     * SMS 전송
     * @param recipientPhoneNumber : 받는사람 전화번호
     * @param content : 내용
     * @return
     * @throws Exception
     */
    public UtilVo.SmsResponse sendSMS(String recipientPhoneNumber, String content) throws Exception{
        Long time = System.currentTimeMillis();

        List<UtilVo.SmsMessage> messages = new ArrayList<>();
        messages.add(new UtilVo.SmsMessage(recipientPhoneNumber, content));

        UtilVo.SmsRequest smsRequest = new UtilVo.SmsRequest("SMS", "COMM", "82", senderPhoneNum, content, messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        String sig = makeSignature(time); //암호화
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        UtilVo.SmsResponse smsResponse = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+serviceId+"/messages"), body, UtilVo.SmsResponse.class);

        return smsResponse;

    }

    public String makeSignature(Long time) throws Exception {

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

}
