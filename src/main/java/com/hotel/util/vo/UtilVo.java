package com.hotel.util.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UtilVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "SMS 응답값")
    public static class SmsResponse {
        @Schema(description = "요청 아이디")
        String requestId;
        @Schema(description = "요청 시간")
        LocalDateTime requestTime;
        @Schema(description = "요청 상태 코드 - 202: 성공 그외 실패")
        String statusCode;
        @Schema(description = "요청 상태명 - success: 성공 fail: 실패")
        String statusName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "SMS 전송 요청값")
    public static class SmsRequest {
        @Schema(description = "SMS Type - SMS, LMS, MMS (소문자 가능)",  required = true)
        String type;
        @Schema(description = "메시지 Type - COMM: 일반메시지 AD: 광고메시지",  required = false)
        String contentType;
        @Schema(description = "국가번호",  required = false)
        String countryCode;
        @Schema(description = "발신번호 - 사전 등록된 발신번호만 사용 가능",  required = true)
        String from;
        @Schema(description = "기본 메시지 내용 - SMS: 최대 80byte",  required = true)
        String content;
        @Schema(description = "메시지 정보",  required = true)
        List<SmsMessage> messages;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "SMS 메시지")
    public static class SmsMessage {
        @Schema(description = "수신번호",  required = true)
        String to;

        @Schema(description = "수신자에게만 보내는 개별메시지",  required = false)
        String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "메일 전송 요청값")
    public static class MailRequest {
        @Schema(description = "제목",  required = true)
        String subject;
        @Schema(description = "내용",  required = true)
        String text;
        @Schema(description = "수신자 이메일",  required = true)
        String receiver;
        @Schema(description = "인증키 전송",  required = true)
        String key;
    }
}
