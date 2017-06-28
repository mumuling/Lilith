package com.youloft.lilith.common.widgets.webkit;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.youloft.lilith.common.widgets.webkit.utils.WebKitUtils;

/**
 * WebViewClient
 * Created by coder on 2017/6/27.
 */

public class WebViewClientEx extends WebViewClient {

    private URLProtocolHandler mProtocolHandler;

    public WebViewClientEx() {
        mProtocolHandler = new URLProtocolHandler(null);
    }

    public void setURLHandler(URLProtocolHandler handler) {
        this.mProtocolHandler = handler;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        //1.处理支付跳转
        try {
            if (WebKitUtils.handleAlipayUrl(view, url)) {
                return true;
            }
        } catch (Throwable e) {
        }

        //2.处理应用内协议
        try {
            if (mProtocolHandler != null && mProtocolHandler.handleUrl(view, url)) {
                return true;
            }
        } catch (Throwable e) {

        }

        //3.处理AppLink
        try {
            if (!url.toLowerCase().startsWith("http")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            }
        } catch (Throwable e) {

        }
        return super.shouldOverrideUrlLoading(view, url);
    }


}
