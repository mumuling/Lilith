package com.youloft.lilith.common.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewCanPullAble;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.youloft.lilith.R;

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



    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
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

    /**
     * recyclerview截图
     * @param view
     * @return
     */
    public static Bitmap shotRecyclerView(RecyclerViewCanPullAble view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount() - 3;
            int height = 0;
            Paint paint = new Paint();
            paint.setTextSize(ViewUtil.dp2px(10));
            paint.setColor(Color.WHITE);
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmapCache = new LruCache<>(cacheSize);
            View shareView = null;
            for (int i = 1; i < size; i++) {
                View itemView = null;
                RecyclerView.ViewHolder holder = view.findViewHolderForAdapterPosition(i);
                if(holder!=null){
                    itemView = holder.itemView;
                }else if(i==1){
                    holder = view.getRecycledViewPool().getRecycledView(view.getAdapter().getItemViewType(i));
                    if(holder!=null){
                        itemView = holder.itemView;
                    }
                }
                if(itemView==null){
                    itemView = view.getCachedViewForPosition(i);
                }
                if(itemView == null){
                   break;
                }

                View viewById = itemView.findViewById(R.id.cons_my_info_share_icon);
                if (viewById != null) {
                    viewById.setVisibility(View.INVISIBLE);
                    shareView = viewById;
                }

                if(itemView.getWidth()==0||itemView.getHeight()==0){
                    itemView.measure(
                            View.MeasureSpec.makeMeasureSpec(view.getWidth() - view.getPaddingLeft() - view.getPaddingRight(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    itemView.layout(0, 0, itemView.getMeasuredWidth(),
                            itemView.getMeasuredHeight());
                }

                itemView.setDrawingCacheEnabled(true);
                itemView.buildDrawingCache();
                Bitmap drawingCache = itemView.getDrawingCache();
                if (drawingCache != null) {
                    bitmapCache.put(String.valueOf(i), drawingCache);
                }
                height += itemView.getMeasuredHeight();
            }

            if (shareView != null) {
                shareView.setVisibility(View.VISIBLE);
            }

            height += ViewUtil.dp2px(122);
            int width = view.getMeasuredWidth() - view.getPaddingLeft() - view.getPaddingRight();
            bigBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);

            bigCanvas.drawColor(view.getResources().getColor(R.color.tab_share_color));

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                if (bitmap == null || bitmap.isRecycled()) {
                    continue;
                }
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
//                bitmap.recycle();
            }
            //二维码
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) Math.ceil(1000/ViewUtil.dp2px(76));
            Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.qrcard, options);

            if (bitmap != null && !bitmap.isRecycled()) {
                bigCanvas.drawBitmap(bitmap, (width - bitmap.getWidth())/2, iHeight + ViewUtil.dp2px(16), paint);
            }
            bigCanvas.drawText("定制我的运势", (width - paint.measureText("定制我的运势"))/2, height - ViewUtil.dp2px(17), paint);
        }
        return bigBitmap;
    }
}
