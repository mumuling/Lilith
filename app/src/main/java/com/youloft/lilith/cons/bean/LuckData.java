package com.youloft.lilith.cons.bean;

import com.youloft.lilith.common.utils.CalendarHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by zchao on 2017/6/30.
 * desc:
 * version:
 */

public class LuckData extends Object{
    public ArrayList<LuckItem> data;   //每天的数据
    public int type;                   //类型{1：概括运；2：感情运；3工作运 4：财富运；}

    public static class LuckItem {
        public LuckItem() {
        }

        public LuckItem(GregorianCalendar day, int luckLevel) {
            this.day = day;
            this.luckLevel = luckLevel;
        }

        public GregorianCalendar day;
        public int luckLevel;

        public String getDayString(){
            return getString(day);
        }


    }
    public static String getString(GregorianCalendar day){
        long intervalDays = CalendarHelper.getIntervalDays(new GregorianCalendar(), day);
        if (intervalDays < 0) {
            return "昨天";
        } else if (intervalDays == 0) {
            return "今天";
        } else if (intervalDays == 1){
            return "明天";
        } else if (intervalDays == 2) {
            return "后天";
        } else {
            return String.valueOf(day.get(Calendar.DAY_OF_MONTH));
        }
    }
}
