package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.login.activity.LoginActivity;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.login.event.LoginWithPwdEvent;
import com.youloft.lilith.register.event.RegisterEvent;

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

    @BindView(R.id.btn)
    Button btn;
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

    //这个是从快捷登录那边发过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent loginEvent) {
        //登录成功了,图片,昵称
        String headImgUrl = loginEvent.mUserBean.data.userInfo.headImg;
        String nickName = loginEvent.mUserBean.data.userInfo.nickName;
        tvNickName.setText(nickName);
        GlideApp.with(mContext).load(headImgUrl).into(ivHeader);
//        GlideApp.with(mContext).load(headImgUrl).transform(new BlurTransformatio).into(ivBlurBg);
    }

    //这个是注册成功后,设置完密码 发过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterEvent(RegisterEvent registerEvent){

    }

    //这个是手机号码+密码登录,发过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginWithPwdEvent(LoginWithPwdEvent loginWithPwdEvent){

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return rootView;
    }


    //话题  资料  设置  的点击事件
    @OnClick({R.id.rl_topic, R.id.rl_personal_data, R.id.rl_setting})
    public void clickMyItem(View view) {
        switch (view.getId()) {
            case R.id.rl_topic:
                Toast.makeText(getActivity(), "话题", Toast.LENGTH_SHORT).show();
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
}
