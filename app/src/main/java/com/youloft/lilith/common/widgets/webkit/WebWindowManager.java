package com.youloft.lilith.common.widgets.webkit;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.youloft.lilith.common.widgets.webkit.WebChromeClientEx.IFullScreenHandler;
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

    private IFullScreenHandler mScreenHandler;

    public WebWindowManager(Context context,
                            ViewGroup viewGroup,
                            URLProtocolHandler protocolHandler,
                            IFullScreenHandler screenHandler) {
        this.mViewGroup = viewGroup;
        this.mContext = context;
        this.mProtocolHandler = protocolHandler;
        this.mScreenHandler = screenHandler;
        mWebViewStack = new Stack<>();
        pushWebView();
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
        if (mWebViewStack.size() > 5) {
            return mTopView;
        }
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
        return !mWebViewStack.empty();
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
        webViewEx.setFullScreenHandler(mScreenHandler);
        return (T) webViewEx;
    }


    /**
     * 新开一个Tab
     *
     * @param view
     * @param isDialog
     * @param isUserGesture
     * @param resultMsg
     * @return
     */
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        T t = pushWebView();
        transport.setWebView(t);
        resultMsg.sendToTarget();
        return true;
    }

    /**
     * 关闭指定Tab
     *
     * @param window
     */
    @Override
    public void onCloseWindow(WebView window) {
        if (!hasTab()) {
            return;
        }

        if (mTopView == window) {//要关闭的为当前窗口
            popWebView();
            return;
        }

        //要关闭的Window在历史中
        if (mWebViewStack.contains(window)) {
            mViewGroup.removeView(window);
            window.destroy();
            mWebViewStack.remove(window);
        }
    }
}
