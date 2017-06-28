package com.youloft.lilith.common.widgets.webkit;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * WebView基础类
 * Created by coder on 2017/6/27.
 */

public class WebViewEx extends WebView {
    public WebViewEx(Context context) {
        this(context, null);
    }

    public WebViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView(context);
    }


    //ChromeClient
    private WebChromeClientEx mWebChromeClientEx;

    private WebViewClientEx mWebViewClientEx;

    /**
     * 初始化WebView
     *
     * @param context
     */
    private void initWebView(Context context) {
        //处理WebChrome
        mWebChromeClientEx = new WebChromeClientEx();
        setWebChromeClient(mWebChromeClientEx);
        //WebviewClient
        mWebViewClientEx = new WebViewClientEx();
        setWebViewClient(mWebViewClientEx);

        //Setting
        WebSettings settings = getSettings();

        //Cookie
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }

        //JavaScript
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setNeedInitialFocus(false);
        settings.setAllowFileAccess(true);

        //cache
        String dbPath = context.getApplicationContext().getDir("web-cache", Context.MODE_PRIVATE).getAbsolutePath();
        // support android API 7-
        try {
            // API 7, LocalStorage/SessionStorage
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setDatabasePath(dbPath);
            // API 7， Web SQL Database, 需要重载方法（WebChromeClient）才能生效，无法只通过反射实现
        } catch (Exception e) {
        }
        try {
            // API 7， Application Storage
            settings.setAppCacheEnabled(true);
            settings.setAppCachePath(dbPath);
            settings.setAppCacheMaxSize(100 * 1024 * 1024);
        } catch (Exception e) {
        }
        try {
            // API 5， Geolocation
            settings.setGeolocationEnabled(true);
            settings.setGeolocationDatabasePath(dbPath);
        } catch (Exception e) {
        }
        settings.setUseWideViewPort(true);
        settings.setSupportMultipleWindows(false);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setTextZoom(100);
        settings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);

        settings.setUserAgentString(WebKitUtils.combinUA(settings.getUserAgentString()));
    }

    /**
     * 设置URLHandler
     *
     * @param urlHandler
     */
    public void setURLHandler(URLProtocolHandler urlHandler) {
        if (mWebViewClientEx != null)
            mWebViewClientEx.setURLHandler(urlHandler);
    }

    /**
     * 设置窗口处理Handler
     *
     * @param handler
     */
    public void setWindowHandler(WebChromeClientEx.IWebWindowHandler handler) {
        if (mWebChromeClientEx != null && handler != null) {
            mWebChromeClientEx.setWindowHandler(handler);
            getSettings().setSupportMultipleWindows(true);
        } else {
            getSettings().setSupportMultipleWindows(false);
        }
    }


    /**
     * 设置全屏处理
     *
     * @param handler
     */
    public void setFullScreenHandler(WebChromeClientEx.IFullScreenHandler handler) {
        if (mWebChromeClientEx != null && handler != null) {
            mWebChromeClientEx.setFullScreenHandler(handler);
        }
    }
}
