package com.hotel.common;

import io.micrometer.core.lang.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static Date stringToDate(String dateStr) throws ParseException {
        java.util.Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        date = formatter.parse(dateStr);
        return date;
    }

    public static Date stringToDateMonth(String dateStr) throws ParseException {
        java.util.Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        date = formatter.parse(dateStr);
        return date;
    }
}
