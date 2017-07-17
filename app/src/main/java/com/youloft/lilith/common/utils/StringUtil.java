package com.youloft.lilith.common.utils;

import android.text.TextUtils;

/**
 *
 */

public class StringUtil {
    /**\
     *    用户名超过7个字部分显示省略号
     * @param name
     * @return
     */
    public static String toNameString(String name) {
        if (TextUtils.isEmpty(name))return "";
        if (name.length() > 7 ) {
            return name.substring(0,7) + "...";
        }
        return name;
    }
}
