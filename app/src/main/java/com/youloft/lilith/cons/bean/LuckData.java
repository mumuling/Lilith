package com.youloft.lilith.cons.bean;

import java.util.ArrayList;

/**
 * Created by zchao on 2017/6/30.
 * desc:
 * version:
 */

public class LuckData extends Object{
    public ArrayList<LuckItem> data;   //每天的数据
    public int type;                   //类型{1：概括运；2：感情运；3：财富运；4工作运}

    public static class LuckItem {
        public LuckItem(String day, int luckLevel) {
            this.day = day;
            this.luckLevel = luckLevel;
        }

        public String day;
        public int luckLevel;
    }
}
