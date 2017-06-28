package com.youloft.lilith.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.webkit.URLProtocolHandler;
import com.youloft.lilith.common.widgets.webkit.WebChromeClientEx;
import com.youloft.lilith.common.widgets.webkit.WebViewEx;
import com.youloft.lilith.common.widgets.webkit.WebWindowManager;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 网页Activity
 */
public class WebActivity extends AppCompatActivity implements WebChromeClientEx.IFullScreenHandler {

    private ViewGroup mWebGroup;

    private ViewGroup mFullScreenGroup;

    private WebWindowManager<WebViewEx> mWebWindowManager;

    /**
     * 协议处理
     */
    private URLProtocolHandler mProtocolHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mProtocolHandler = new URLProtocolHandler(this) {
            @Override
            protected void handleClose() {
                finish();
            }

            @Override
            protected void handleWebBack(WebView view) {
                if (!backToWeb()) {
                    handleClose();
                }
            }
        };
        mFullScreenGroup = (ViewGroup) findViewById(R.id.full_container);
        mWebGroup = (ViewGroup) findViewById(R.id.web_container);
        mWebWindowManager = new WebWindowManager<>(this, mWebGroup, mProtocolHandler, this);
    }

    /**
     * 加载Url
     *
     * @param url
     */
    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //对于不是以Http打头的URL直接进行协议处理后关闭网页
        if (!url.toLowerCase().startsWith("http")) {
            if (mProtocolHandler != null) {
                mProtocolHandler.handleUrl(mWebWindowManager.getTopView(), url);
            }
            finish();
            return;
        }
        mWebWindowManager.getTopView().loadUrl(url);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mProtocolHandler != null) {
            mProtocolHandler.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        if (backToWeb()) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * 回退
     *
     * @return
     */
    private boolean backToWeb() {
        //处理当前页面后退
        WebViewEx topView = mWebWindowManager.getTopView();
        if (topView != null && topView.canGoBack()) {
            topView.goBack();
            return true;
        }

        //处理Web弹栈
        if (mWebWindowManager != null
                && mWebWindowManager.hasTab()) {
            mWebWindowManager.popWebView();
            return true;
        }
        return false;
    }

    /**
     * 显示全屏视图
     *
     * @param view
     * @param requestedOrientation
     * @param callback
     */
    @Override
    public void onShowFullScreen(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        if (requestedOrientation != -2) {
            setRequestedOrientation(requestedOrientation);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        mFullScreenGroup.addView(view, MATCH_PARENT, MATCH_PARENT);
        mFullScreenGroup.setVisibility(View.VISIBLE);
        mWebGroup.setVisibility(View.INVISIBLE);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     */
    @Override
    public void onExitFullScreen() {
        mFullScreenGroup.removeAllViews();
        mFullScreenGroup.setVisibility(View.GONE);
        mWebGroup.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFullScreenGroup.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}
