package com.wdcloud.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 * Created by csf on 2015/5/16.
 *
 * @author csf
 */
@SuppressWarnings(value = "unused")
public class DateUtil {
    private DateUtil() {

    }

    private final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String FORMAT_YMD = "yyyy-MM-dd";

    public static String getTimestamp() {
        return String.valueOf(new Date().getTime());
    }

    public static boolean is8Date(String date) {
        if (StringUtil.isEmpty(date)) {
            return false;
        }
        String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern pattern = Pattern.compile(eL);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public static String getSysDate(String format) {
        if (StringUtil.isEmpty(format)) {
            format = DEFAULT_FORMAT;
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 得到当前月份
     */
    public static int getSysMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String getSysDate() {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);
        return df.format(new Date());
    }

    public static int getSysYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static Date getDateByString(String date, String format) {
        if (StringUtil.isEmpty(format)) {
            format = DEFAULT_FORMAT;
        }
        if (StringUtil.isNotEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException("转换为日期类型错误：DATE：" + date + "  FORMAT:" + format);
            }
        } else {
            return null;
        }
    }

    public static String getFormatDate(Date date, String format) {
        if (StringUtil.isEmpty(format)) {
            format = DEFAULT_FORMAT;
        }
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } else {
            return null;
        }
    }

    public static Date getDayStartOfDate(Date date) {
        String formatDate = getFormatDate(date, "yyyy-MM-dd");
        return StringUtil.isEmpty(formatDate) ? null : getDateByString(formatDate + " 00:00:00", "");
    }

    public static Date getDayEndOfDate(Date date) {
        String formatDate = getFormatDate(date, "yyyy-MM-dd");
        return StringUtil.isEmpty(formatDate) ? null : getDateByString(formatDate + " 23:59:59", "");
    }

    /**
     * 一月的最后一天
     *
     * @param date 日期
     * @return 当月最后一天
     */
    public static Date getDayEndOfMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        Date dayStartOfMonth = getDayStartOfMonth(date);
        Calendar cal = Calendar.getInstance();

        cal.setTime(dayStartOfMonth);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return getDayEndOfDate(cal.getTime());
    }

    /**
     * 一月的第一天
     *
     * @param date 日期
     * @return 当月第一天
     */
    public static Date getDayStartOfMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStartOfDate(cal.getTime());
    }

    public static Date getDateByLongDate(Long millis) {
        if (millis == null) {
            return new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.getTime();

    }

    /**
     * 日期加减运算
     *
     * @param date 日期
     * @param day  加天数（减传负数)
     * @return 返回运算后日期
     */
    public static Date daysOperation(Date date, int day) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //加天
            cal.add(Calendar.DATE, day);
            return cal.getTime();
        } else {
            return null;
        }
    }

    public static Date monthOperation(Date date, int month) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //加天
            cal.add(Calendar.MONTH, month);
            return cal.getTime();
        } else {
            return null;
        }
    }

    public static Date hoursOperation(Date date, int hours) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //加小时
            cal.add(Calendar.HOUR, hours);
            return cal.getTime();
        } else {
            return null;
        }
    }

    public static String secToTime(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 获取当前日期对象
     *
     * @return 当前日期对象
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取当前日期字符串
     *
     * @param format 日期格式
     * @return 当前日期字符串
     */
    public static String now(String format) {
        return format(now(), format);
    }

    /**
     * 格式化日期对象
     *
     * @param date   日期对象
     * @param format 日期格式
     * @return 当前日期字符串
     */
    public static String format(Date date, String format) {
        return new DateTime(date).toString(format);
    }

    /**
     * 格式化日期对象，格式为yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public static String format(Date date) {
        return new DateTime(date).toString(DEFAULT_FORMAT);
    }

    /**
     * 格式化日期对象，格式为yyyy-MM-dd HH:mm:ss
     *
     * @param mills 毫秒
     * @return 日期字符串
     */
    public static String format(Long mills) {
        return new DateTime(mills).toString(DEFAULT_FORMAT);
    }

    /**
     * 格式化日期对象
     *
     * @param mills   毫秒
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String format(Long mills, String pattern) {
        return new DateTime(mills).toString(pattern);
    }

    /**
     * 解析字符串日期(不含时间)
     *
     * @param date    字符日期
     * @param pattern 格式
     * @return LocalDate
     */
    public static LocalDate parseDate(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormat.forPattern(pattern));
    }

    /**
     * 解析字符串日期(不含时间),格式:yyyy-MM-dd
     *
     * @param date 字符日期
     * @return LocalDate
     */
    public static LocalDate parseDate(String date) {
        return parseDate(date, FORMAT_YMD);
    }

    /**
     * 解析字符串日期(不含时间)
     *
     * @param date 字符日期
     * @return Date
     */
    public static Date parseDate2(String date, String pattern) {
        return parseDate(date, pattern).toDate();
    }

    /**
     * 解析字符串日期(不含时间),格式:yyyy-MM-dd
     *
     * @param date 字符日期
     * @return Date
     */
    public static Date parseDate2(String date) {
        return parseDate2(date, FORMAT_YMD);
    }


    /**
     * 解析字符串日期时间(带时间)
     *
     * @param dateTime 字符日期时间
     * @param pattern  格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTime, String pattern) {
        return LocalDateTime.parse(dateTime, DateTimeFormat.forPattern(pattern));
    }

    /**
     * 解析字符串日期时间(带时间),格式:yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime 字符日期时间
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return parseDateTime(dateTime, DEFAULT_FORMAT);
    }

    /**
     * 解析字符串日期时间(带时间)
     *
     * @param dateTime 字符日期时间
     * @param pattern  格式
     * @return Date
     */
    public static Date parseDateTime2(String dateTime, String pattern) {
        return parseDateTime(dateTime, pattern).toDate();
    }

    /**
     * 解析字符串日期时间(带时间),格式:yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime 字符日期时间
     * @return Date
     */
    public static Date parseDateTime2(String dateTime) {
        return parseDateTime2(dateTime, DEFAULT_FORMAT);
    }

    /**
     * 当天剩余时间,秒
     *
     * @return int
     */
    public static int remainSeconds() {
        java.time.LocalDateTime midnight = java.time.LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return (int) ChronoUnit.SECONDS.between(java.time.LocalDateTime.now(), midnight);
    }

    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * 返回指定日期的季的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定日期的上个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }


    /**
     * 返回指定年季的季的第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getFirstDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 1 - 1;
        } else if (quarter == 2) {
            month = 4 - 1;
        } else if (quarter == 3) {
            month = 7 - 1;
        } else if (quarter == 4) {
            month = 10 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的上一季的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        date = DateUtil.monthOperation(date, -3);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定日期的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */

    public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }


    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        return calendar.getTime();
    }

    /**
     * 获取两个日期之间的月数
     *
     * @param bigDate   大的日期
     * @param smallDate 小的日期
     * @return
     */
    public static int getMonthNum(String bigDate, String smallDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(bigDate));
            int bigYear = c.get(Calendar.YEAR);
            int bigMonth = c.get(Calendar.MONTH);

            c.setTime(sdf.parse(smallDate));
            int smallYear = c.get(Calendar.YEAR);
            int smallMonth = c.get(Calendar.MONTH);

            int result;
            if (bigYear == smallYear) {
                result = bigMonth - smallMonth;
            } else {
                result = 12 * (bigYear - smallYear) + bigMonth - smallMonth;
            }
            return result;
        } catch (ParseException e) {
            throw new RuntimeException("日期类型转换错误：DATE：" + bigDate + "  FORMAT:" + "yyyy-MM-dd");
        }
    }

    /**
     * date2比date1多的天数
     *
     * @param date1 小日期
     * @param date2 大日期
     * @return
     */
    public static int getDifferentDayNum(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                //闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 获取某段时间的所有日期
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }


    /**
     * 获取某段时间月份信息
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findMonths(Date dBegin, Date dEnd) {
        List IMonth = new ArrayList();
        IMonth.add(dBegin);
        //定义日期实例
        Calendar dd = Calendar.getInstance();
        //设置日期起始时间
        dd.setTime(dBegin);
        //判断是否到结束日期
        while (dd.getTime().before(dEnd)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = sdf.format(dd.getTime());
            //进行当前日期月份加1
            dd.add(Calendar.MONTH, 1);
            IMonth.add(dd.getTime());
        }
        return IMonth;
    }

    public static List<Date> findHours(Date dBegin, Date dEnd) {
        List IMonth = new ArrayList();
        IMonth.add(dBegin);
        //定义日期实例
        Calendar dd = Calendar.getInstance();
        //设置日期起始时间
        dd.setTime(dBegin);
        //判断是否到结束日期
        while (dd.getTime().before(dEnd)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String str = sdf.format(dd.getTime());
            //进行当前日期月份加1
            dd.add(Calendar.HOUR, 1);
            IMonth.add(dd.getTime());
        }
        return IMonth;
    }


}
