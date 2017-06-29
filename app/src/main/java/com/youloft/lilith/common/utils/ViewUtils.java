package com.youloft.lilith.common.utils;


/**
 * View相关Util
 * Created by coder on 2017/6/26.
 */

public class ViewUtils {
    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(final float dpValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(final float pxValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(final float spValue) {
        final float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 获取状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight() {
        int resheigtId = -1;
        try {
            if (resheigtId < 1) {
                Class clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                resheigtId = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
            }
        } catch (Exception e) {
            return dp2px(20);
        }
        int statusHeight = Utils.getContext().getResources().getDimensionPixelSize(resheigtId);
        return statusHeight;
    }
}
