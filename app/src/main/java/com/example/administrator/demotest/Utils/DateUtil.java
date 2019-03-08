package com.example.administrator.demotest.Utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author zhodngxin
 *         G  Era 标志符  Text  AD
 *         y  年  Year  1996; 96
 *         M  年中的月份  Month  July; Jul; 07
 *         w  年中的周数  Number  27
 *         W  月份中的周数  Number  2
 *         D  年中的天数  Number  189
 *         d  月份中的天数  Number  10
 *         F  月份中的星期  Number  2
 *         E  星期中的天数  Text  Tuesday; Tue
 *         a  Am/pm 标记  Text  PM
 *         H  一天中的小时数（0-23）  Number  0
 *         k  一天中的小时数（1-24）  Number  24
 *         K  am/pm 中的小时数（0-11）  Number  0
 *         h  am/pm 中的小时数（1-12）  Number  12
 *         m  小时中的分钟数  Number  30
 *         s  分钟中的秒数  Number  55
 *         S  毫秒数  Number  978
 *         z  时区  General time zone  Pacific Standard Time; PST; GMT-08:00
 *         Z  时区
 */
public class DateUtil {
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat SDF_DATEANDTIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat SDF_MONTH = new SimpleDateFormat("M");
    private static final SimpleDateFormat SDF_WDAY = new SimpleDateFormat("E");
    private static final SimpleDateFormat SDF_MDAY = new SimpleDateFormat("d");
    private static final SimpleDateFormat CURRENT_DAY = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private static final SimpleDateFormat CURRENT_TIME = new SimpleDateFormat("yyyyMMddhh", Locale.getDefault());

    public static int getNowMonth() {
        return Integer.parseInt(formatMonth());
    }

    public static synchronized String formatDate(Long milliseconds) {
        return SDF_DATE.format(new Date(milliseconds));
    }

    public static synchronized String formatDate(Date date) {
        return SDF_DATE.format(date);
    }

    public static synchronized String formatDate() {
        return SDF_DATE.format(new Date(System.currentTimeMillis()));

    }

    public static synchronized String formatDateForTimeMillis() {
        return SDF_DATEANDTIME.format(new Date(System.currentTimeMillis()));
    }

    public static synchronized Date parseDate(String string) {
        try {
            return SDF_DATE.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static synchronized String formatTime() {
        return SDF_TIME.format(new Date());
    }

    public static synchronized String formatTime(Long milliseconds) {
        return SDF_TIME.format(new Date(milliseconds));
    }

    public static synchronized String formatTime(Date date) {
        return SDF_TIME.format(date);
    }

    public static synchronized Date parseTime(String string) {
        try {
            return SDF_TIME.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized String formatDateAndTime() {
        //		sdfTime.parse(string)
        return SDF_DATEANDTIME.format(new Date());
    }

    public static synchronized String formatDateAndTime(Long milliseconds) {
        return SDF_DATEANDTIME.format(new Date(milliseconds));
    }

    public static synchronized String formatDateAndTime(Date date) {
        //		sdfTime.parse(string)
        return SDF_DATEANDTIME.format(date);
    }

    public static Date parseDateAndTime(String string) {
        try {
            return SDF_DATEANDTIME.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized String formatMonth() {
        return SDF_MONTH.format(new Date());
    }

    public static synchronized String formatMonth(Long milliseconds) {
        return SDF_MONTH.format(new Date(milliseconds));
    }

    public static synchronized String formatMonth(Date date) {
        //		sdfTime.parse(string)
        return SDF_MONTH.format(date);
    }


    public static synchronized String formatWDay() {
        return SDF_WDAY.format(new Date());
    }

    public static synchronized String formatWDay(Long milliseconds) {
        return SDF_WDAY.format(new Date(milliseconds));
    }

    public static synchronized String formatWDay(Date date) {
        //		sdfTime.parse(string)
        return SDF_WDAY.format(date);
    }


    public static synchronized String formatMDay() {
        return SDF_MDAY.format(new Date());
    }

    public static synchronized String formatMDay(Long milliseconds) {
        return SDF_MDAY.format(new Date(milliseconds));
    }

    public static synchronized String formatMDay(Date date) {
        return SDF_MDAY.format(date);
    }


    public static long getTime(String time) {
        try {
            return SDF_DATEANDTIME.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当天前七天的日期
     *
     * @param currentServiceDate 服务器当天日期yyyyMMdd
     * @return 前七天日期yyyyMMdd
     * @throws ParseException
     */
    public static String getLastSeventDate(String currentServiceDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(currentServiceDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -6);
        Date lastSeventDate = calendar.getTime();
        String strLastSeventDate = sdf.format(lastSeventDate);
        return strLastSeventDate;

    }

    /**
     * 返回前七天数组
     *
     * @param currentServiceDate 当前服务器日期
     * @return 前七天数组
     * @throws ParseException
     */
    public static String[] getArrLastSeventDate(String currentServiceDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(currentServiceDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String[] arrSevenDate = new String[7];
        calendar.add(Calendar.DATE, 0);
        Date lastSeventDate = calendar.getTime();
        arrSevenDate[0] = sdf.format(lastSeventDate);

        for (int i = 0; i < arrSevenDate.length; i++) {
            if (i > 0) {
                calendar.setTime(lastSeventDate);
                calendar.add(Calendar.DATE, -1);
                lastSeventDate = calendar.getTime();
                arrSevenDate[i] = sdf.format(lastSeventDate);
            }
        }
        return arrSevenDate;

    }

    /**
     * 返回年月日
     *
     * @return
     */
    public static String getCurrentDay() {
        Calendar now = Calendar.getInstance();
        String dateStr = CURRENT_DAY.format(now.getTime());
        return dateStr;
    }
    /**
     * 返回到小时
     */
    public static String getCurrentHour() {
        Calendar now = Calendar.getInstance();
        String dateStr = CURRENT_TIME.format(now.getTime());
        return dateStr;
    }
    /**
     * 返回两个日期间的相隔天数
     *
     * @return
     */
    public static int getGapCount(String date) {
        try {
            Date d1 = CURRENT_DAY.parse(date);
            Calendar now = Calendar.getInstance();
            int day = (int) ((now.getTime().getTime() - d1.getTime()) / (60 * 60 * 1000 * 24));
            return day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getTimeHour(String date) {
        if (TextUtils.isEmpty(date)) {
            return 2;
        }
        try {
            Date d1 = CURRENT_TIME.parse(date);
            Calendar now = Calendar.getInstance();
            int hour = (int) ((now.getTime().getTime() - d1.getTime()) / (60 * 1000 * 60));
            return hour;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
