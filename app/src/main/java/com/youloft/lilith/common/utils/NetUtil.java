package com.youloft.lilith.common.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.youloft.lilith.LLApplication;


/**
 * Desc: 网络请求工具类，包括公共参数的封装
 * Change:
 *
 * @author zchao created at 2017/6/26 14:10
 * @see
 */

public class NetUtil {


    /**
     * 获取网络类型名字
     *
     * @param context
     * @return
     */
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = "disconnect";
        if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        }

        String typeName = networkInfo.getTypeName();
        if ("WIFI".equalsIgnoreCase(typeName)) {
            type = "wifi";
        } else {
            type = "MOBILE";
        }
        return type;
    }


    /**
     * 网络是否OK
     *
     * @return
     */
    public static boolean isNetOK() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 获取活动的网络
     *
     * @return
     */
    public static NetworkInfo getActiveNetworkInfo() {

        return ((ConnectivityManager) Utils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }
}
