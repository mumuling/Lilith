package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.login.activity.LoginActivity;
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

    //登录成功之后接收到的事件  快捷登录, 注册成功, 账号密码登录, 三方登录 都会发送事件到这个地方
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent loginEvent) {
        //登录成功了,图片,昵称
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
            GlideApp.with(mContext).load(headImgUrl).into(ivHeader);
        }
//        ConsManager.getConsSrc("1").pKey
//        GlideApp.with(mContext).load(headImgUrl).transform(new BlurTransformatio).into(ivBlurBg);
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
        //view创建完成之后,检查登录状态,如果是登录的状态,那么把用户数据填上去
        if(AppConfig.LOGIN_STATUS){
            setUserInfo();
        }
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

    @OnClick(R.id.iv_header)
    public void onHeaderClicked() {
        ARouter.getInstance().build("/test/EditInformationActivity").navigation();
    }
}
