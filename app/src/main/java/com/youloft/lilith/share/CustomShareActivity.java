package com.youloft.lilith.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/6.
 * desc:
 * version:
 */

public class CustomShareActivity extends BaseActivity {
    @BindView(R.id.share_bg)
    ImageView mShareBg;
    @BindView(R.id.share_wx_hy)
    LinearLayout mShareWxHy;
    @BindView(R.id.share_wx_pyq)
    LinearLayout mShareWxPyq;
    @BindView(R.id.share_cancel)
    TextView mShareCancel;
    @BindView(R.id.share_bottom_group)
    RelativeLayout mShareBottomGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cus_share_activity);
        ButterKnife.bind(this);
    }

    /**
     * 分享给朋友
     */
    @OnClick(R.id.share_wx_hy)
    public void shareHY() {
        new ShareAction(CustomShareActivity.this).withText("aweijgpo;aweg")
                .setPlatform(SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform)
                .setCallback(listener).share();
    }

    /**
     * 分享到朋友圈
     */
    @OnClick(R.id.share_wx_pyq)
    public void sharePYQ() {
//        new ShareAction(CustomShareActivity.this).withText("aweijgpo;aweg")
//                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform().mPlatform)
//                .setCallback(listener).share();

        UMShareAPI.get(this).getPlatformInfo(CustomShareActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 取消
     */
    @OnClick(R.id.share_cancel)
    public void shareCancel() {
        finish();
    }

    UMShareListener listener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.d(TAG, "onStart() called with: share_media = [" + share_media + "]");
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.d(TAG, "onResult() called with: share_media = [" + share_media + "]");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.d(TAG, "onError() called with: share_media = [" + share_media + "], throwable = [" + throwable + "]");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.d(TAG, "onCancel() called with: share_media = [" + share_media + "]");
        }
    };
    private static final String TAG = "CustomShareActivity";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
