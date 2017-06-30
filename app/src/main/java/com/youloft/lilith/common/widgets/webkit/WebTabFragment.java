package com.youloft.lilith.common.widgets.webkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.common.base.BaseFragment;

/**
 * 描述类作用
 * <p>
 * 作者 coder
 * 创建时间 2017/6/30
 */

public class WebTabFragment extends BaseFragment {

    private URLProtocolHandler mProtocolHandler;

    public WebTabFragment() {
        super(0);
    }

    private WebViewEx mWebView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProtocolHandler = new URLProtocolHandler(getActivity()) {
            @Override
            public boolean handleUrl(WebView view, String url) {
                boolean flag = super.handleUrl(view, url);
                if (!flag && url.startsWith("http")) {
                    ARouter.getInstance().build("/ui/web")
                            .withString("url", url)
                            .navigation();
                }
                return true;
            }

            @Override
            protected void handleClose() {

            }

            @Override
            protected void handleWebBack(WebView view) {

            }
        };
        mWebView.setURLHandler(mProtocolHandler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //协议处理回调
        if (mProtocolHandler != null) {
            mProtocolHandler.onActivityResult(getActivity(), requestCode, resultCode, data);
        }
    }
}
