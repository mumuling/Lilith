package com.youloft.lilith.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

/**
 * 工具类入口用于管理Context
 * Created by coder on 2017/6/26.
 */

public class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }


    /**
     * 反编码Base64到明文
     *
     * @return
     */
    public static String base64Decode(String decodeStr) {
        if (TextUtils.isEmpty(decodeStr)) {
            return decodeStr;
        }
        return new String(Base64.decode(decodeStr, Base64.DEFAULT));
    }


}
