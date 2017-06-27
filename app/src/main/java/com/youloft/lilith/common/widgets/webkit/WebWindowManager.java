package com.youloft.lilith.common.widgets.webkit;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.youloft.lilith.common.widgets.webkit.WebChromeClientEx.IWebWindowHandler;

import java.util.Stack;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * WebView的窗口管理器
 * <p>
 * Created by coder on 2017/6/27.
 */
public class WebWindowManager<T extends WebView> implements IWebWindowHandler {

    private ViewGroup mViewGroup;

    private Stack<T> mWebViewStack;

    private Context mContext;

    private URLProtocolHandler mProtocolHandler;

    private T mTopView;

    public WebWindowManager(Context context, ViewGroup viewGroup, URLProtocolHandler protocolHandler) {
        this.mViewGroup = viewGroup;
        this.mContext = context;
        this.mProtocolHandler = protocolHandler;
        mWebViewStack = new Stack<>();
    }

    /**
     * 获取顶层View
     *
     * @return
     */
    public T getTopView() {
        return mTopView;
    }

    /**
     * 展示一个新窗口
     */
    public T pushWebView() {
        if (mTopView != null) {
            mTopView.setVisibility(View.INVISIBLE);
            mWebViewStack.push(mTopView);
        }
        //产生新的TopView
        T webView = obtainNewWebView();
        mViewGroup.addView(webView, MATCH_PARENT, MATCH_PARENT);
        mTopView = webView;
        return webView;
    }

    /**
     * 弹出一个窗口
     *
     * @return
     */
    public T popWebView() {
        if (mWebViewStack.empty()) {
            return null;
        }
        if (mTopView != null) {
            mTopView.destroy();
            mViewGroup.removeView(mTopView);
        }
        mTopView = mWebViewStack.pop();
        mTopView.setVisibility(View.VISIBLE);
        return mTopView;
    }


    /**
     * 是否还有子Tab
     *
     * @return
     */
    public boolean hasTab() {
        return mWebViewStack.empty();
    }

    /**
     * 产生一个新的WebView
     *
     * @return
     */
    private T obtainNewWebView() {
        WebViewEx webViewEx = new WebViewEx(mContext);
        if (mProtocolHandler != null) {
            webViewEx.setURLHandler(mProtocolHandler);
        }
        webViewEx.setWindowHandler(this);
        return (T) webViewEx;
    }


    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(obtainNewWebView());
        resultMsg.sendToTarget();
        return true;
    }

    @Override
    public void onCloseWindow(WebView window) {

    }
}
