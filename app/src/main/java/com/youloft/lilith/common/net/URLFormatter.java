package com.youloft.lilith.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 地址格式化工具用于处理一些通配符
 * <p/>
 * <p/>
 * 此类就在UI线程中使用好了
 * <p/>
 * Created by coder on 16-5-16.
 */
public class URLFormatter {
    private URLFormatter() {
    }

    //标记是否 需要URLENcoding
    static final String KEY_DONTURLENCODE = "[DONTURLENCODE]";

    //标记是否下载后不关闭网页
    static final String KEY_KEEPVIEW = "[KEEPVIEW]";

    //格式化参数缓存
    private static HashMap<String, String> sFormatArgsMap = new HashMap<>();

    //正则来匹配URL中需要填充的字段
    private static final Pattern ARG_PARTTERN = Pattern.compile("\\[\\w+\\]");//Apply的模式

    static {//inflate default args;
        sFormatArgsMap.put("DONTURLENCODE", "");
        sFormatArgsMap.put("KEEPVIEW", "");
    }

    /**
     * 添加参数
     *
     * @param key   键
     * @param value 值
     */
    public static void attachArgs(String key, String value) {
        if (TextUtils.isEmpty(key))
            return;
        sFormatArgsMap.put(key.toUpperCase(), value);
    }


    /**
     * 转换URL
     *
     * @param urlString 网址
     * @param extraArgs 额外的参数
     *                  用于支持本工具不提供支持的第三方参数或者说跟业务有关的参数
     * @return
     */
    public static String parseUrl(String urlString, HashMap<String, String> extraArgs) {
        if (TextUtils.isEmpty(urlString)) {
            return urlString;
        }

        boolean needEncode = urlString.indexOf(KEY_DONTURLENCODE) >= 0;
        Matcher mArgMatcher = ARG_PARTTERN.matcher(urlString);
        StringBuffer resultBuffer = new StringBuffer();
        //开始匹配
        while (mArgMatcher.find()) {
            String argKey = mArgMatcher.group().replaceAll("(\\[|\\])", "").toUpperCase();//提取“[KEY]”中的KEY
            if (extraArgs != null && extraArgs.containsKey(argKey)) {
                mArgMatcher.appendReplacement(resultBuffer, transferArgValue(extraArgs.get(argKey), needEncode));
                continue;
            }
            mArgMatcher.appendReplacement(resultBuffer, transferArgValue(getValue(argKey), needEncode));
        }
        //添加最后的部分
        mArgMatcher.appendTail(resultBuffer);
        return resultBuffer.toString();
    }

    /**
     * 处理值是否需要UrlEncode
     *
     * @param value
     * @param needEncode
     * @return
     */
    private static String transferArgValue(String value, boolean needEncode) {
        if (TextUtils.isEmpty(value))
            return "";
        try {
            if (needEncode)
                return URLEncoder.encode(value, "utf-8");
        } catch (Throwable unused) {
        }
        return value;
    }

    /**
     * 根据Key获取值
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if (TextUtils.isEmpty(key))
            return null;

        if (sFormatArgsMap.containsKey(key)) {
            return sFormatArgsMap.get(key);
        }

        if ("T".equals(key) || "CTS".equals(key) || "STS".equals(key))
            return String.valueOf(System.currentTimeMillis() / 1000);

        if ("DATETIME".equals(key) || "CTIME".equals(key) || "STIME".equals(key))
            return DateFormat.format("yyyy-MM-dd hh:mm:ss", System.currentTimeMillis()).toString();

        if ("DATE".equals(key))
            return DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();

        String value = getCachedValue(key);
        if (value != null) {
            attachArgs(key, value);
            return value;
        }
        return "";
    }

    /**
     * 可以被缓存的参数
     *
     * @param tag
     * @return
     */
    private static String getCachedValue(String tag) {
        if ("OSVERSION".equals(tag)) {
            return Build.VERSION.RELEASE;
        }

        if ("IDFA".equals(tag)) {
            return AppEnv.getAndroidId();
        }

        if ("APPID".equals(tag)) {
            return "wnl_android_lite";
        }

        if ("OSNAME".equals(tag)) {
            return "android";
        }

        if ("APPVERSION".equals(tag)) {
            return AppEnv.VERSION_NAME;
        }

        if ("VERINT".equals(tag)) {
            return String.valueOf(AppEnv.VERSION_CODE);
        }

        if ("MAC".equals(tag)) {
            final String mac = AppEnv.getMacAddress();
            if (TextUtils.isEmpty(mac))
                return "00:00:00:00:00:00";
            return mac;
        }

        if ("CLIENTID".equals(tag)) {
            return AppEnv.CLIENT_ID;
        }

//        if ("DEVICEID".equals(tag) || "OPENUDID".equals(tag)) {
//            try {
//                return LLApplication.getDeviceId();
//            } catch (Exception e) {
//                return "";
//            }
//        }

//        if ("CHANNELID".equals(tag) || "CHANNEL".equals(tag)) {
//            return LLApplication.getChannelId();
//        }

        if ("BD".equals(tag)
                || "BUNDLE".equals(tag)) {
            return AppEnv.BUNDLE;
        }

        if ("CC".equals(tag)) {
            return AppEnv.CC;
        }

        if ("LANG".equals(tag)) {
            return AppEnv.LANG;
        }

        if ("CLIENTNAME".equals(tag)) {
            return "YouLoft.cnCalendar.Android";
        }

        if ("MODEL".equals(tag)) {
            return Build.MODEL.replaceAll(" ", "");
        }

        if ("APPNAME".equals(tag)) {
            return "万年历AndroidLite";
        }

        if ("NETWORK".equals(tag)) {
            return NetUtil.getNetworkTypeName(AppEnv.getAppContext());
        }

        if ("THEMEID".equals(tag)) {
            return "default";
        }

        if ("IMEI".equals(tag)) {
            return AppEnv.getIMEI();
        }

        if ("IMSI".equals(tag)) {
            return AppEnv.getIMSI();
        }

        if ("ICCID".equals(tag)) {
            return AppEnv.getICCID();
        }
        if ("DEVICETYPE".equals(tag)) {
            return Build.MODEL;
        }

        return null;
    }


}
