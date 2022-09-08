package com.hotel.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateUtil {
    /**
     * yyyy/MM/dd 형식으로 온 String 을 Date형식으로 변환
     * @param dateStr
     * @return Date
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));

        return formatter.parse(dateStr);
    }

    /**
     *  Date 을 String형식으로 변환 - yyyy/MM/dd
     * @param date
     * @return Date
     * @throws ParseException
     */
    public static String DateToString(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));

        return formatter.format(date);
    }

    /**
     *  Date 을 String형식으로 변환. 사업자 등록확인시 공공데이터 처리용 - yyyyMMdd
     * @param date
     * @return Date
     * @throws ParseException
     */
    public static String DateToStringForAPI(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));

        return formatter.format(date);
    }

    /**
     *
     * @param requestDate
     * @return Long Type 시간차 초단위로 제공
     */
    public static Long timeDiffSec(Date requestDate){

        return (System.currentTimeMillis() - requestDate.getTime()) / 1000;
    }
}
