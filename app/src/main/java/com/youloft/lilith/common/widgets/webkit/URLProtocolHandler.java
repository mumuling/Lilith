package com.youloft.lilith.common.widgets.webkit;

import android.content.Intent;
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
            return handleProtocolUrl(view, url);
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
    protected boolean handleProtocolUrl(WebView view, String url) {
        return false;
    }
}
