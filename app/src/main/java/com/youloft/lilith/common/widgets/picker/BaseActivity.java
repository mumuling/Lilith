package com.youloft.lilith.common.widgets.picker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by zchao on 2017/6/26.
 * desc: Activity基类
 * version:
 */

public class BaseActivity extends Activity {

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
//        Analytics.postActivityResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Analytics.postActivityPause(this);
    }

    /**
     * 使用沉浸式菜单栏
     * 迁移至CFApp
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

}
