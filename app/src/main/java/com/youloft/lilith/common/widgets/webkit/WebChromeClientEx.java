package com.youloft.lilith.common.widgets.webkit;

import android.os.Message;
import android.view.View;
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

    public interface IFullScreenHandler {


        /**
         * 全屏展示
         *
         * @param view
         * @param requestedOrientation
         * @param callback
         */
        void onShowFullScreen(View view, int requestedOrientation, CustomViewCallback callback);

        /**
         * 退出全屏显示
         */
        void onExitFullScreen();
    }


    private IWebWindowHandler mWindowHandler = null;

    //设置窗口处理器
    public void setWindowHandler(IWebWindowHandler handler) {
        this.mWindowHandler = handler;
    }

    private IFullScreenHandler mFullScreenHandler;

    /**
     * 设置全屏处理
     *
     * @param handler
     */
    public void setFullScreenHandler(IFullScreenHandler handler) {
        this.mFullScreenHandler = handler;
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

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (mFullScreenHandler != null) {
            mFullScreenHandler.onShowFullScreen(view, -2, callback);
            return;
        }
        super.onShowCustomView(view, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        if (mFullScreenHandler != null) {
            mFullScreenHandler.onShowFullScreen(view, requestedOrientation, callback);
            return;
        }
        super.onShowCustomView(view, requestedOrientation, callback);

    }


    @Override
    public void onHideCustomView() {
        if (mFullScreenHandler != null) {
            mFullScreenHandler.onExitFullScreen();
            return;
        }
        super.onHideCustomView();
    }

}
