package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.utils.StringUtil;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.BounceableLinearLayout;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.glide.GlideBlurTwoViewTarget;
import com.youloft.lilith.info.event.UserInfoUpDateEvent;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zchao on 2017/6/27.
 * desc: 我fragment，只是个样子。可自己修改
 * version:
 */

public class MEFragment extends BaseFragment {

    public static final String TAG = "MEFragment";

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
    @BindView(R.id.fl_item_container)
    BounceableLinearLayout bllContener; //下面的滑动布局
    @BindView(R.id.fl_anim)
    FrameLayout flAnim; // 动画的容器
    @BindView(R.id.ll_rise)
    LinearLayout llRise;
    @BindView(R.id.ll_sun)
    LinearLayout llSun;
    @BindView(R.id.ll_moon)
    LinearLayout llMoon;
    @BindView(R.id.fl_header_root)
    FrameLayout flHeaderRoot;
    @BindView(R.id.fl_header_container)
    FrameLayout flHeaderContainer;


    public MEFragment() {
        super(R.layout.fragment_me);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    //快捷登录, 注册成功, 账号密码登录, 三方登录 用户登出 都会发送事件到这个地方
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent loginEvent) {
        boolean isLogin = loginEvent.isLogin;
        if (isLogin) {
            //登录成功了,图片,昵称
            setUserInfo();
        } else {
            //登出了  吧所有的图片清空
            ivRise.setImageResource(R.drawable.div);
            ivSun.setImageResource(R.drawable.div);
            ivMoon.setImageResource(R.drawable.div);
            ivConstellation.setImageResource(R.drawable.div);
            ivBlurBg.setImageBitmap(null);
            ivHeader.setImageResource(R.drawable.div);
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
        if (userInfo == null) {
            return;
        }
        String headImgUrl = userInfo.data.userInfo.headImg;

        String showNickName = userInfo.data.userInfo.nickName;
        tvNickName.setText(StringUtil.toNameString(showNickName));
        if (!TextUtils.isEmpty(headImgUrl)) {
            GlideApp.with(mContext).asBitmap().dontAnimate().load(headImgUrl).into(new GlideBlurTwoViewTarget(ivHeader, ivBlurBg));
        } else {
            ivHeader.setImageResource(R.drawable.default_user_head_img);
        }
        //根据不同的key设置不同的星座
        String asceSigns = String.valueOf(userInfo.data.userInfo.asceSigns);//上升
        String sunSigns = String.valueOf(userInfo.data.userInfo.sunSigns);//太阳
        String moonSigns = String.valueOf(userInfo.data.userInfo.moonSigns);//月亮
        String signs = String.valueOf(userInfo.data.userInfo.signs);//我的星座
        ivRise.setImageResource(ConsManager.getAscendantConsIcon(asceSigns));
        ivSun.setImageResource(ConsManager.getSunConsIcon(sunSigns));
        ivMoon.setImageResource(ConsManager.getMoonConsIcon(moonSigns));
        int sex = userInfo.data.userInfo.sex;
        if (sex == 1) {//女
            ivConstellation.setImageResource(ConsManager.getGirlConsIcon(signs));
        } else {
            ivConstellation.setImageResource(ConsManager.getBoyConsIcon(signs));
        }

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

        bllContener.setOnMyScrollerListener(new BounceableLinearLayout.OnMyScrollerListener() {
            @Override
            public void scrollY(float dy) {
                Log.d(TAG, "scrollY() called with: dy = [" + dy + "]");
                moveRise(dy);
                moveSun(dy);
                moveMoon(dy);
                scaleHeader(dy);
//                flHeaderRoot.setScaleY(dy);
//                flHeaderRoot.setScaleX(dy);
            }
        });
        //view创建完成之后,检查登录状态,如果是登录的状态,那么把用户数据填上去
        if (AppSetting.getUserInfo() != null) {
            setUserInfo();
        }
        return rootView;
    }

    private void scaleHeader(float dy) {
        float v = ViewUtil.dp2px(50);
        if (Math.abs(dy) > v) {
            dy = v;
        }
        flHeaderContainer.setScaleX(1 + (Math.abs(dy) / v)*0.2f);
        flHeaderContainer.setScaleY(1 + (Math.abs(dy) / v)*0.2f);
        flHeaderRoot.setScaleX(1 + (Math.abs(dy) / v)*0.2f);
        flHeaderRoot.setScaleY(1 + (Math.abs(dy) / v)*0.2f);
    }

    private void moveRise(float dy) {
        float v = ViewUtil.dp2px(35);
        dy = dy * 0.7f;
//        dy = -0.0005f*dy*dy+dy;
        if (Math.abs(dy) > v) {
            if (dy < 0) {
                dy = -v;
            } else {
                dy = v;
            }
        }

        llRise.setTranslationX(-dy);
        llRise.setTranslationY(-dy);
    }

    private void moveSun(float dy) {
        float v = ViewUtil.dp2px(50);
        if (Math.abs(dy) > v) {
            if (dy < 0) {
                dy = -v;
            } else {
                dy = v;
            }
        }
        llSun.setTranslationX(dy);
        llSun.setTranslationY(-dy);
    }

    private void moveMoon(float dy) {
        float v = ViewUtil.dp2px(20);
        dy = dy * 0.4f;
        if (Math.abs(dy) > v) {
            if (dy < 0) {
                dy = -v;
            } else {
                dy = v;
            }
        }
        llMoon.setTranslationX(dy);
        llMoon.setTranslationY(dy);
    }


    //话题  资料  设置  的点击事件
    @OnClick({R.id.rl_topic, R.id.rl_personal_data, R.id.rl_setting})
    public void clickMyItem(View view) {
        switch (view.getId()) {
            case R.id.rl_topic:
                AppAnalytics.onEvent("Mytopic.C");
                ARouter.getInstance().build("/test/MyTopicActivity").navigation();
                break;
            case R.id.rl_personal_data:
                AppAnalytics.onEvent("Mydata.C");
                ARouter.getInstance().build("/test/EditInformationActivity").navigation();
                break;
            case R.id.rl_setting:
                AppAnalytics.onEvent("Set.C");
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
