package com.youloft.lilith.share;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/6.
 * desc:
 * version:
 */

@Route(path = "/share/CustomShareActivity")
public class CustomShareActivity extends BaseActivity {
    @BindView(R.id.share_root)
    FrameLayout mShareRoot;
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

    @Autowired
    public String mShareTitle;
    @Autowired
    public String mShareContent;
    @Autowired(name = "mShareUrl") // 通过name来映射URL中的不同参数
    public String mShareUrl;
    public static UMImage mShareBitmap = null;
    public static Bitmap mBGBitmap = null;

    private UMWeb mWeb;
    private ShareAction mShareAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cus_share_activity);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        if (mBGBitmap != null) {
            mShareBg.setImageBitmap(mBGBitmap);
        } else {
            mShareBg.setBackgroundColor(getResources().getColor(R.color.black_70));
        }

        mShareAction = new ShareAction(this);
        //Media只能设置一个，后边的会覆盖前面的
        if (!TextUtils.isEmpty(mShareUrl)) {
            UMWeb web = new UMWeb(mShareUrl);
            if (mShareBitmap != null) {
                web.setThumb(mShareBitmap);
            }
            if (!TextUtils.isEmpty(mShareTitle)) {
                web.setTitle(mShareTitle);
            }
            if (!TextUtils.isEmpty(mShareContent)) {
                web.setDescription(mShareContent);
            }
            mShareAction.withMedia(web);
        } else if (mShareBitmap != null) {
            mShareAction.withMedia(mShareBitmap);
        }

        if (!TextUtils.isEmpty(mShareContent)) {
            mShareAction.withText(mShareContent);
        }

        if (!TextUtils.isEmpty(mShareTitle)) {
            mShareAction.withSubject(mShareTitle);
        }


        mShareRoot.post(new Runnable() {
            @Override
            public void run() {
                startAnim(true);
            }
        });
    }

    /**
     * 进入退出动画
     *
     * @param in
     */
    private void startAnim(final boolean in) {
        ValueAnimator bgAnim = ObjectAnimator.ofFloat(mShareBg, View.ALPHA, in ? 0 : 1f, in ? 1f : 0);
        ValueAnimator transAnim = ObjectAnimator.ofFloat(mShareBottomGroup, View.TRANSLATION_Y, in ? mShareBottomGroup.getHeight() : 0, in ? 0 : mShareBottomGroup.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.playTogether(bgAnim, transAnim);
        set.setDuration(350);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!in) {
                    finish();
                }
            }
        });
        set.start();

    }

    /**
     * 分享给朋友
     */
    @OnClick(R.id.share_wx_hy)
    public void shareHY() {
        mShareAction.setPlatform(SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform)
                .setCallback(listener).share();
    }

    /**
     * 分享到朋友圈
     */
    @OnClick(R.id.share_wx_pyq)
    public void sharePYQ() {
        mShareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform().mPlatform)
                .setCallback(listener).share();

    }

    /**
     * 取消
     */
    @OnClick(R.id.share_cancel)
    public void shareCancel() {
        cancel();
    }

    /**
     * 取消
     */
    @OnClick(R.id.share_bg)
    public void shareCancel1() {
        cancel();
    }

    public void cancel(){
        startAnim(false);
    }

    UMShareListener listener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toaster.showLong("分享成功");
            cancel();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toaster.showLong("分享失败");
            cancel();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toaster.showLong("分享取消");
            cancel();
        }
    };
    private static final String TAG = "CustomShareActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShareBitmap = null;
        mBGBitmap = null;
        UMShareAPI.get(this).release();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}