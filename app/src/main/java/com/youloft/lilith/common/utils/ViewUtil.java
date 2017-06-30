package com.youloft.lilith.common.utils;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

/**
 * View相关Util
 * Created by coder on 2017/6/26.
 */

public class ViewUtil {
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

    /**
     * 渲染文字位置居中，这儿做了处理，解决android默认渲染方式上下不平均问题
     * @param canvas
     * @param renderDate
     * @param centerX 位置的中心点x坐标
     * @param centerY   位置的中心点y坐标
     * @param paint
     */
    public static void renderTextByCenter(Canvas canvas, String renderDate, float centerX, float centerY, Paint paint){
        if (TextUtils.isEmpty(renderDate)) {
            return;
        }
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        float startY = centerY - fm.descent + (fm.descent - fm.ascent)/ 2;
        canvas.drawText(renderDate, (centerX - paint.measureText(renderDate)/2), startY, paint);
    }
}
