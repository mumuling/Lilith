package com.youloft.lilith.common.widgets.webkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

/**
 * URL协议处理器
 * Created by coder on 2017/6/27.
 */

public class URLProtocolHandler {

    /**
     * 处理URL
     *
     * @param view
     * @param url
     * @return
     */
    public boolean handleUrl(WebView view, String url) {
        if (url.toLowerCase().startsWith("protocol")) {
            handleProtocolUrl(view, url);
            return true;
        }
        if (url.toLowerCase().startsWith("mailto")) {
            return handleMailToProtocol(view, url);
        }
        return false;
    }

    /**
     * 处理邮件协议
     *
     * @param view
     * @param url
     * @return
     */
    private boolean handleMailToProtocol(WebView view, String url) {
        try {
            String mail = url.substring(url.indexOf(":") + 1);
            if (TextUtils.isEmpty(mail)) {
                return true;
            }
            Intent i = new Intent(Intent.ACTION_SEND);
            // i.setType("text/plain"); //use this line for testing in the emulator
            i.setType("message/rfc822"); // use from live device
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT, "");
            view.getContext().startActivity(Intent.createChooser(i, "发送邮件"));
        } catch (Throwable e) {
        }
        return true;
    }

    /**
     * 处理Protcol://这样的的URL
     *
     * @param view
     * @param url
     * @return
     */
    private void handleProtocolUrl(WebView view, String url) {
        try {
            String actionUrl = url.substring("protocol://".length());
            int dotIndex = actionUrl.indexOf("#");
            if (dotIndex < 0) {
                dotIndex = actionUrl.indexOf(":");
                if (dotIndex < 0) {
                    dotIndex = actionUrl.length();
                }
            }
            String action = actionUrl.substring(0, dotIndex);
            String paramStr = actionUrl.substring(dotIndex);
            handleProtocolImpl(view, url, action.toLowerCase(), paramStr);
        } catch (Exception e) {
        }
    }

    /**
     * 处理Protocol协议
     *
     * @param view
     * @param url
     * @param action
     * @param paramStr
     * @return
     */
    private void handleProtocolImpl(WebView view, String url, String action, String paramStr) {
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            case "rate":
                handleRateApp(view.getContext());
            case "back":
                handleWebBack(view);
            case "exit":
                handleClose();
            case "share":
                handleShare(view, url, action, paramStr);
            case "getfilecode":
                handleFileChoose(view, url, action, paramStr);
        }
    }

    /**
     * 处理文件选择
     *
     * @param view
     * @param url
     * @param action
     * @param paramStr
     */
    protected void handleFileChoose(WebView view, String url, String action, String paramStr) {

    }

    /**
     * 处理分享支持
     *
     * @param view
     * @param url
     * @param action
     * @param paramStr
     */
    protected void handleShare(WebView view, String url, String action, String paramStr) {

    }

    /**
     * 处理关闭
     */
    protected void handleClose() {
    }

    /**
     * 处理网页回退
     *
     * @param view
     */
    protected void handleWebBack(WebView view) {
        if (view.canGoBack()) {
            view.goBack();
        }
    }

    /**
     * 处理应用评分
     *
     * @param context
     */
    private void handleRateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "选择评论的市场"));
    }


}
