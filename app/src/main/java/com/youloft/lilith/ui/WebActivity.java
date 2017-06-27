package com.youloft.lilith.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.webkit.URLProtocolHandler;
import com.youloft.lilith.common.widgets.webkit.WebViewEx;
import com.youloft.lilith.common.widgets.webkit.WebWindowManager;

/**
 * WebActivity
 */
public class WebActivity extends AppCompatActivity {

    private ViewGroup mWebGroup;

    private WebWindowManager<WebViewEx> mWebWindowManager;

    private URLProtocolHandler mProtocolHandler = new URLProtocolHandler() {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebGroup = (ViewGroup) findViewById(R.id.web_container);
        mWebWindowManager = new WebWindowManager<>(this, mWebGroup, mProtocolHandler);
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


}
