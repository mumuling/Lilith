package com.youloft.lilith.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

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
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by zchao on 2017/6/26.
 * desc:
 * version:
 */

public class Utils {
    public static int dp2Px(Resources resources, float dp) {
        final float scale = resources
                .getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static float sp2Px(Resources resources, @DimenRes int id) {
        return (int) resources.getDimension(id);
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

    /**
     * 获取状态栏的高度
     * @return
     */
    public static int getStatusHeight(Context context) {
        int resheigtId = -1;

        try {
            if (resheigtId < 1) {
                Class clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                resheigtId = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
            }
        } catch (Exception e) {
            return Utils.dp2Px(context.getResources(), 20);
        }
        int statusHeight = context.getResources().getDimensionPixelSize(resheigtId);
        return statusHeight;
    }
}
