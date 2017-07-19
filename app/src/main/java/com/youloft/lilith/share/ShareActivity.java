package com.youloft.lilith.share;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import com.jakewharton.rxbinding2.view.RxView;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.socialize.SocializeAction;
import com.youloft.socialize.SocializeApp;
import com.youloft.socialize.SocializePlatform;
import com.youloft.socialize.media.ShareImage;
import com.youloft.socialize.media.ShareWeb;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by zchao on 2017/7/6.
 * desc:
 * version:
 */

@Route(path = "/ui/share")
public class ShareActivity extends BaseActivity {
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

    @Autowired(name = "title")
    public String mShareTitle;
    @Autowired(name="content")
    public String mShareContent;
    @Autowired(name = "url") // 通过name来映射URL中的不同参数
    public String mShareUrl;
    public static Bitmap mBGBitmap = null;

    private SocializeAction mShareAction;

    public static ShareImage mShareBitmap;

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

        mShareAction = new SocializeAction(this);
        //Media只能设置一个，后边的会覆盖前面的
        if (!TextUtils.isEmpty(mShareUrl)) {
            ShareWeb web = new ShareWeb(mShareUrl);
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

        bindClick();
    }

    private void bindClick() {
        RxView.clicks(mShareWxHy)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        mShareAction.setPlatform(SocializePlatform.WEIXIN)
                                .setCallback(new ShareEventListener(""){
                                    @Override
                                    public void onResult(SocializePlatform share_media) {
                                        super.onResult(share_media);
                                        cancel();
                                    }
                                }).share();
                    }
                });

        RxView.clicks(mShareWxPyq)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        mShareAction.setPlatform(SocializePlatform.WEIXIN_CIRCLE)
                                .setCallback(new ShareEventListener(""){
                                    @Override
                                    public void onResult(SocializePlatform share_media) {
                                        super.onResult(share_media);
                                        cancel();
                                    }
                                }).share();
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
//    @OnClick(R.id.share_wx_hy)
//    public void shareHY() {
//        mShareAction.setPlatform(SocializePlatform.WEIXIN)
//                .setCallback(new ShareEventListener("xxx"){
//                    @Override
//                    public void onResult(SocializePlatform share_media) {
//                        super.onResult(share_media);
//                        cancel();
//                    }
//                }).share();
//    }

    /**
     * 分享到朋友圈
     */
//    @OnClick(R.id.share_wx_pyq)
//    public void sharePYQ() {
//
//        mShareAction.setPlatform(SocializePlatform.WEIXIN_CIRCLE)
//                .setCallback(new ShareEventListener("xxx"){
//                    @Override
//                    public void onResult(SocializePlatform share_media) {
//                        super.onResult(share_media);
//                        cancel();
//                    }
//                }).share();
//    }

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


    private static final String TAG = "ShareActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        SocializeApp.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShareBitmap = null;
        mBGBitmap = null;
        SocializeApp.get(this).release();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
