package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.glide.GlideBlurTwoViewTarget;
import com.youloft.lilith.info.event.UserInfoUpDateEvent;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by zchao on 2017/6/27.
 * desc: 我fragment，只是个样子。可自己修改
 * version:
 */

public class MEFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_constellation)
    ImageView ivConstellation; //用户星座
    @BindView(R.id.iv_header)
    ImageView ivHeader; //用户头像
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;  //用户昵称
    @BindView(R.id.iv_rise)
    ImageView ivRise; //上升星座
    @BindView(R.id.iv_sun)
    CircleImageView ivSun; //太阳星座
    @BindView(R.id.iv_moon)
    CircleImageView ivMoon; //月亮星座
    @BindView(R.id.iv_blur_bg)
    ImageView ivBlurBg; //模糊背景


    public MEFragment() {
        super(R.layout.fragment_me);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

//    @Override
//    public void onResume() {
//        Log.d(TAG, "onResume() called");
//        super.onResume();
//    }
//
//    private static final String TAG = "MEFragment";
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        Log.d(TAG, "setUserVisibleHint() called with: isVisibleToUser = [" + isVisibleToUser + "]");
//        if (isVisibleToUser && !AppConfig.LOGIN_STATUS) {
//            ARouter.getInstance().build("/test/LoginActivity")
//                    .navigation();
//        }
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden &&!AppConfig.LOGIN_STATUS ) {
//            ARouter.getInstance().build("/test/LoginActivity")
//                    .navigation();
//        }
//    }

    //快捷登录, 注册成功, 账号密码登录, 三方登录 用户登出 都会发送事件到这个地方
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent loginEvent) {
        boolean isLogin = loginEvent.isLogin;
        if (isLogin) {
            //登录成功了,图片,昵称
            setUserInfo();
        } else {
            //登出了
        }

    }

    //用户修改信息过后,发出的事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoUpDate(UserInfoUpDateEvent userInfoUpDateEvent) {
        setUserInfo();
    }


    /**
     * 填充用户数据
     */
    private void setUserInfo() {
        UserBean userInfo = AppSetting.getUserInfo();
        String headImgUrl = userInfo.data.userInfo.headImg;
        String nickName = userInfo.data.userInfo.nickName;
        tvNickName.setText(nickName);
        if (!TextUtils.isEmpty(headImgUrl)) {
            GlideApp.with(mContext).asBitmap().dontAnimate().load(headImgUrl).into(new GlideBlurTwoViewTarget(ivHeader, ivBlurBg));
        }
/**
 * 只想问写得是他妈的什么鬼
 */
//            GlideApp.with(mContext).load(headImgUrl).into(ivHeader);
//            GlideApp.with(mContext).asBitmap().load(headImgUrl).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                    ivBlurBg.setImageBitmap(ViewUtil.blurBitmap(resource));
//                }
//            });
            GlideApp.with(mContext).load(headImgUrl).into(ivHeader);
            GlideApp.with(mContext).asBitmap().load(headImgUrl).into(ivBlurBg);
        }


        //根据不同的key设置不同的星座
        String asceSigns = String.valueOf(userInfo.data.userInfo.asceSigns);//上升
        String moonSigns = String.valueOf(userInfo.data.userInfo.moonSigns);//月亮
        String sunSigns = String.valueOf(userInfo.data.userInfo.sunSigns);//太阳
        String signs = String.valueOf(userInfo.data.userInfo.signs);//我的星座

        ivRise.setImageResource(ConsManager.getConsIconSrc(ConsManager.getConsSrc(asceSigns).pKey)[0]);
        ivMoon.setImageResource(ConsManager.getConsIconSrc(ConsManager.getConsSrc(moonSigns).pKey)[0]);
        ivSun.setImageResource(ConsManager.getConsIconSrc(ConsManager.getConsSrc(sunSigns).pKey)[0]);
        ivConstellation.setImageResource(ConsManager.getConsIconSrc(ConsManager.getConsSrc(signs).pKey)[0]);

    }

    /**
     * 根据不同的key设置不同的icon
     * @param pKey
     * @param flag
     */
    private void setStarIcon(String pKey,int flag){
        Integer integer = ConsManager.getConsIconSrc(pKey)[0];

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        //view创建完成之后,检查登录状态,如果是登录的状态,那么把用户数据填上去
        if (AppConfig.LOGIN_STATUS) {
            setUserInfo();
        }
        return rootView;
    }


    //话题  资料  设置  的点击事件
    @OnClick({R.id.rl_topic, R.id.rl_personal_data, R.id.rl_setting})
    public void clickMyItem(View view) {
        switch (view.getId()) {
            case R.id.rl_topic:
                ARouter.getInstance().build("/test/MyTopicActivity").navigation();
                break;
            case R.id.rl_personal_data:
                ARouter.getInstance().build("/test/EditInformationActivity").navigation();
                break;
            case R.id.rl_setting:
                ARouter.getInstance().build("/test/SettingActivity").navigation();
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_header)
    public void onHeaderClicked() {
        ARouter.getInstance().build("/test/EditInformationActivity").navigation();
    }
}
