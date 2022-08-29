package com.hotel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static Date stringToDate(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));

        return formatter.parse(dateStr);
    }
}
