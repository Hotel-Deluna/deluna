package com.hotel.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
     * yyyyMMdd 형식으로 온 String 을 Date형식으로 변환 (공휴일 공공데이터 형식)
     * @param dateStr
     * @return Date
     * @throws ParseException
     */
    public static Date stringToDateForHolidayCrawling(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));

        return formatter.parse(dateStr);
    }

    /**
     *  Date 을 String형식으로 변환 - yyyy/MM/dd
     * @param date
     * @return String
     * @throws ParseException
     */
    public static String dateToString(Date date) throws ParseException {
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
    public static String dateToStringForAPI(Date date) throws ParseException {
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

    /**
     *
     * @param date1, date2
     * @return 두 날짜의 차이 정수로 return
     * date1 과 date2가 같으면 0
     * date1 이 date2보다 이전이면 그 차이만큼 음수
     * date1 이 date2보다 이후면 그 차이만큼 양수
     */
    public static int dateDiff(Date date1, Date date2){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));
        formatter.format(date1);
        formatter.format(date2);
        return date1.compareTo(date2);
    }

    /**
     *
     * @param holidayDate
     * @return 오늘이 공휴일이면 true, 아니면 false
     */
    public static boolean checkHoliday(List<Date> holidayDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));
        formatter.format(now);

        for(Date s : holidayDate){
            if(now.compareTo(s) == 0){
                return true;
            }
        }

        return false;
    }

    /**
     * 특정날짜의 요일값 리턴
     * @param date
     * @return
     * @throws Exception
     */
    public static int getDayCode(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 요청날짜가 특정 날짜 사이에 들어가는지 체크
     * @param startDate
     * @param endDate
     * @param requestDate
     * @return
     * @throws Exception
     */
    public static boolean checkDateBetween(Date startDate, Date endDate, Date requestDate) throws Exception {

        LocalDate localdate = requestDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        endLocalDate = endLocalDate.plusDays(1);

        // 시작일보다 이전날짜가 아니면서 종료일(+1일 해준날짜)보다 이전날짜면 ture, 아니면 false
        return ( ! localdate.isBefore( startLocalDate ) ) && ( localdate.isBefore( endLocalDate ) );
    }

    /**
     * 두 날짜 기간이 서로 중복되는지 체크
     * 중복된 날짜가 있으면 true 리턴
     * @return
     * @throws Exception
     */
    public static boolean checkDatePeriodsDuplication(Date startDate1, Date endDate1, Date startDate2, Date endDate2) throws Exception {

        LocalDate startLocalDate1 = startDate1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate1 = endDate1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate startLocalDate2 = startDate2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate2 = endDate2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ! (startLocalDate2.isAfter(endLocalDate1) || endLocalDate2.isBefore(startLocalDate1));
    }

    /**
     * 특정날짜에 요청한 숫자만큼 날짜 추가해서 리턴
     * @param requestDate requestPlus
     * @return
     * @throws Exception
     */
    public static Date plusSomeDay(Date requestDate, int requestPlus) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));
        formatter.format(requestDate);

        Calendar c = Calendar.getInstance();
        c.setTime(requestDate);
        c.add(Calendar.DATE, requestPlus);

        return c.getTime();
    }

    /**
     * Date 형식 날짜만 리턴 (시간 제외)
     * @param date
     * @return Date
     * @throws ParseException
     */
    public static Date DateToDayFormat(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        // 한국 표준시로 변경
        formatter.setTimeZone(TimeZone.getTimeZone("KST"));

        return formatter.parse(formatter.format(date));
    }

}
