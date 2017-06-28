package com.youloft.lilith.common.widgets.webkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.youloft.lilith.common.widgets.webkit.handle.AbsHandle;
import com.youloft.lilith.common.widgets.webkit.handle.FileHandle;
import com.youloft.lilith.common.widgets.webkit.handle.ShareHandle;

import java.util.HashMap;

/**
 * URL协议处理器
 * Created by coder on 2017/6/27.
 */

public class URLProtocolHandler {

    private Activity mActivity = null;

    private static HashMap<String, AbsHandle> sHandleMap = null;


    //初始化协议
    static {
        sHandleMap = new HashMap<>();
        sHandleMap.put("share", new ShareHandle());
        sHandleMap.put("getfilecode", new FileHandle());
    }


    /**
     * 注册Handle
     *
     * @param action
     * @param handle
     */
    public static void registerHandle(String action, AbsHandle handle) {
        sHandleMap.put(action, handle);
    }

    /**
     * 反注册handle
     *
     * @param action
     */
    public static void unregisterHandle(String action) {
        sHandleMap.remove(action);
    }

    public URLProtocolHandler(Activity activity) {
        this.mActivity = activity;
    }


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
            e.printStackTrace();
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
        if (TextUtils.isEmpty(action) || view == null) {
            return;
        }
        if (mActivity == null
                && view.getContext() != null
                && view.getContext() instanceof Activity) {
            mActivity = (Activity) view.getContext();
        }


        if (sHandleMap.containsKey(action) && mActivity != null) {
            sHandleMap.get(action).handle(mActivity, view, url, action, paramStr);
            return;
        }

        switch (action) {
            case "rate":
                handleRateApp(view.getContext());
            case "back":
                handleWebBack(view);
            case "exit":
                handleClose();
        }
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


    /**
     * Activity回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (sHandleMap != null) {
            for (AbsHandle absHandle : sHandleMap.values()) {
                try {
                    absHandle.onActivityResult(activity, requestCode, resultCode, data);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
