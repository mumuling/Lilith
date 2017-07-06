package com.youloft.lilith.common.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;

import com.youloft.lilith.LLApplication;

import java.util.ArrayList;
import java.util.List;

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
    public static float dp2px(final float dpValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static float px2dp(final float pxValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return  (pxValue / scale + 0.5f);
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
            return (int) dp2px(20);
        }
        int statusHeight = Utils.getContext().getResources().getDimensionPixelSize(resheigtId);
        return statusHeight;
    }

    /**
     * 获取底部状态栏高度
     * @return
     */
    public static int getNavigationBarHeight() {
        Resources resources = Utils.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
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
    /**
     * 渲染一系列数据
     * @param canvas
     * @param renderDate
     * @param paint
     * @param startX
     * @param startY
     * @param itemWidth
     */
    public static void renderTextList(Canvas canvas, List<String> renderDate, Paint paint, float startX, float startY, int itemWidth){
        for (int i = 0; i < renderDate.size(); i++) {
            String data = SafeUtil.getSafeData(renderDate, i);
            if (!TextUtils.isEmpty(data)) {
                renderTextByCenter(canvas, data, startX + i * itemWidth, startY, paint);
            }
        }
    }


    public static Bitmap blurBitmap(Bitmap bitmap) {
        return blurBitmap(bitmap, Utils.getContext());
    }


    public static Bitmap blurBitmap(Bitmap bitmap, Context context) {

        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 初始化Renderscript，这个类提供了RenderScript context，在创建其他RS类之前必须要先创建这个类，他控制RenderScript的初始化，资源管理，释放
        RenderScript rs = RenderScript.create(context);

        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        // 设定模糊度
        blurScript.setRadius(25.f);

        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        allOut.copyTo(outBitmap);

        bitmap.recycle();

        rs.destroy();

        return outBitmap;
    }

    /**
     * 创建贝塞尔曲线的path
     * @param path 贝塞尔曲线的path，如果传入空则会返回一个新的Path
     * @param data 数据集，里边的连接曲线的所有点；至少两个点才行(两点只是直线)；
     * @param intensity 平滑度，范围[0f, 1f]
     * @return
     */
    public static Path createBezierPath(Path path, ArrayList<Point> data, float intensity){
        if (data.isEmpty() || data.size() <= 1) {   //  至少两个点
            return path;
        }
        if (path == null)path= new Path();
        path.reset();

        Point cur = null;
        Point prev = null;
        Point prePre = null;
        Point next = null;
        int nextIndex = 0;

        cur = prev = prePre = SafeUtil.getSafeData(data, 0);

        path.moveTo(cur.x, cur.y);
        for (int j = 1; j < data.size(); j++) {
            prePre = prev;
            prev = cur;
            cur = nextIndex == j ? next : data.get(j);
            nextIndex = j + 1 < data.size() ? j + 1 : j;
            next = data.get(nextIndex);

            float prevDx = (cur.x - prePre.x) * intensity;
            float prevDy = (cur.y - prePre.y) * intensity;
            float curDx = (next.x - prev.x) * intensity;
            float curDy = (next.y - prev.y) * intensity;

            path.cubicTo(prev.x + prevDx, (prev.y + prevDy),
                    cur.x - curDx,
                    (cur.y - curDy), cur.x, cur.y);
        }
        return path;
    }
}
