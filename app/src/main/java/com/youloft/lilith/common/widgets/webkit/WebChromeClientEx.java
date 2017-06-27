package com.youloft.lilith.common.widgets.webkit;

import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * WebChromeClientEx增强类
 * <p>
 * Created by coder on 2017/6/27.
 */

public class WebChromeClientEx extends WebChromeClient {

    /**
     * 处理Web窗口变化
     */
    public interface IWebWindowHandler {

        /**
         * 创建窗口
         *
         * @param view
         * @param isDialog
         * @param isUserGesture
         * @param resultMsg
         * @return
         */
        boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg);

        /**
         * 关闭指定窗口
         *
         * @param window
         */
        void onCloseWindow(WebView window);
    }

    private IWebWindowHandler mWindowHandler = null;

    //设置窗口处理器
    public void setWindowHandler(IWebWindowHandler handler) {
        this.mWindowHandler = handler;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        if (mWindowHandler != null) {
            return this.mWindowHandler.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onCloseWindow(WebView window) {
        if (mWindowHandler != null) {
            this.mWindowHandler.onCloseWindow(window);
            return;
        }
        super.onCloseWindow(window);
    }

}
