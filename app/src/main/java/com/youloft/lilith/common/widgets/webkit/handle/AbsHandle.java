package com.youloft.lilith.common.widgets.webkit.handle;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;

/**
 * 协议处理
 * Created by coder on 2017/6/28.
 */

public abstract class AbsHandle {


    /**
     * 协议处理入口
     *
     * @param activity
     * @param webView
     * @param url
     * @param action
     * @param params
     */
    public abstract void handle(Activity activity, WebView webView, String url, String action, String params);

    /**
     * 系统回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    }
}
