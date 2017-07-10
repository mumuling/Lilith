package com.youloft.lilith.common.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by zchao on 2017/6/26.
 * desc: Activity基类
 * version:
 */

public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (useImmerseToolbar()) {
            immerseToolbar();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * 截屏
     *@param needFixStatus 是否需要去掉statubar
     *@param compress 压缩比例
     * @return
     */
    public Bitmap takeScreenShot(boolean needFixStatus, int compress) {
        try {
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            Bitmap b1 = loadBitmapFromView(view);
            Bitmap dest = null;
            if (needFixStatus) {
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                dest = Bitmap.createBitmap(b1, 0, statusBarHeight, b1.getWidth(), b1.getHeight()
                        - statusBarHeight);
            } else {
                dest = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight());

            }
            if (compress != 0) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                dest.compress(Bitmap.CompressFormat.JPEG, 100, out);
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inSampleSize = compress;
                ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
                dest.recycle();
                dest = BitmapFactory.decodeStream(isBm, null, newOpts);
            }
            return dest;
        } catch (Throwable e) {
            return null;
        }
    }

    protected Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }
    /**
     * 使用沉浸式菜单栏
     */
    private void immerseToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * 默认不实用沉浸式菜单栏,如需使用重写并返回true即可
     *
     * @return
     */
    public boolean useImmerseToolbar() {
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
