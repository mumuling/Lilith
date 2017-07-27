package com.youloft.lilith.common.widgets.webkit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
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

    /**
     * 重新处理JSConfirm避免由Webview处理弹出后导致WebView被阻塞
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle(url).setMessage(message)
                .setCancelable(false)
                .setNegativeButton("确定", new Dialog.OnClickListener() {
                    /**
                     * This method will be invoked when a button in the dialog is clicked.
                     *
                     * @param dialog The dialog that received the click.
                     * @param which  The button that was clicked (e.g.
                     *               {@link DialogInterface#BUTTON1}) or the position
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        result.confirm();
                    }
                })
                .show();
        return true;
    }

    /**
     * 重新处理JSConfirm避免由Webview处理弹出后导致WebView被阻塞
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle(url)
                .setMessage(message)
                .setCancelable(false)
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("确定", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                        dialog.dismiss();

                    }
                }).show();
        return true;
    }

    /**
     * 直接不支持Prompt
     *
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.cancel();
        return true;
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
