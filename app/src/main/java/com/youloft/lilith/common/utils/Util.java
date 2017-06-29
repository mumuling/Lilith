package com.youloft.lilith.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by zchao on 2017/6/29.
 * desc:
 * version:
 */

public class Util {
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
