package com.youloft.lilith.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by zchao on 2017/7/5.
 * desc: 日期帮助类
 * version:
 */

public class CalendarHelper {
    public  static  SimpleDateFormat formatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static long UNIT_DAY_MILLS = 24 * 60 * 60 * 1000;
    public static long UNIT_HOUR_MILLS = 60 * 60 * 1000;
    public static long UNIT_MINUTE_MILLS = 60 * 1000;
    public static String format(Date date, String formatString){
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String format1 = format.format(date);
        return format1;
    }

    public static Date parseDate(String dateString, String formatString){
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String[] dayOfWeek = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    public static String getWeekInEN(Calendar date){
        int i = date.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek[i-1];
    }


    public static long getIntervalDays(Calendar calendar1, Calendar calendar2) {
        return calendar1 == null || calendar2 == null? 0L:
                (clearTime(calendar2.getTimeInMillis()) + (long)getUTCOffset(calendar2)) / 86400000L - (clearTime(calendar1.getTimeInMillis()) + (long)getUTCOffset(calendar1)) / 86400000L;
    }
    public static long clearTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    public static int getUTCOffset(Calendar calendar) {
        return calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
    }

    public static String getInterValTime(long times,long interval) {
         interval = System.currentTimeMillis();
        if (Math.abs(interval-times)/UNIT_DAY_MILLS >= 1) {
            return (int)(Math.abs(interval-times)/UNIT_DAY_MILLS) + "天前";
        } else if (Math.abs(interval-times)/UNIT_HOUR_MILLS >=1) {
            return (int)(Math.abs(interval-times)/UNIT_HOUR_MILLS) + "小时前";
        } else  if (Math.abs(interval-times)/UNIT_MINUTE_MILLS == 0) {
            return "刚刚";
        } else {
            return (int)(Math.abs(interval-times)/UNIT_MINUTE_MILLS) + "分钟前";
        }
    }



    public static long getTimeMillisByString(String time) {
        try {
            Date date = formatAll.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getNowTimeString() {

        Calendar calendar = Calendar.getInstance();
        return formatAll.format(calendar.getTime());

    }
}
