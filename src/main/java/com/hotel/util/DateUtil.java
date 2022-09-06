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
     *
     * @param requestDate
     * @return Long Type 시간차 초단위로 제공
     */
    public static Long timeDiffSec(Date requestDate){

        return (System.currentTimeMillis() - requestDate.getTime()) / 1000;
    }
}
