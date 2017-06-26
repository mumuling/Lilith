package com.youloft.lilith.common.utils;

import android.util.Log;

/**
 * Created by zchao on 2017/6/26.
 * desc: 日志输出工具类，项目中所有日志输出都走这里边
 * version: 1.0
 */

public class LogUtil {
    private static final boolean IS_OPEN_LOG = true;

    public static void e(String tag, String msg){
        if (IS_OPEN_LOG) {
            Log.e(tag, msg);
        }
    }
    public static void d(String tag, String msg){
        if (IS_OPEN_LOG) {
            Log.d(tag, msg);
        }
    }
    public static void v(String tag, String msg){
        if (IS_OPEN_LOG) {
            Log.v(tag, msg);
        }
    }
    public static void i(String tag, String msg){
        if (IS_OPEN_LOG) {
            Log.i(tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (IS_OPEN_LOG) {
            Log.w(tag, msg);
        }
    }
}
