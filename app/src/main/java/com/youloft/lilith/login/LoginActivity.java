package com.youloft.lilith.login;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面
 * <p>
 * Created by GYH on 2017/6/29.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.vv_background)
    VideoView vvBackground;//背景视频
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;//手机号码
    @BindView(R.id.et_password)
    EditText etPassword;//密码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initBackgroundVedio();
    }

    /**
     * 背景视频设置
     */
    private void initBackgroundVedio() {
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.bg_login;
        vvBackground.setVideoURI(Uri.parse(uri));
        vvBackground.start();
        vvBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
    }

    //登录按钮
    @OnClick(R.id.btn_login)
    public void login(View view){
        Toast.makeText(this,"哈哈",Toast.LENGTH_SHORT).show();
    }

    //服务条款
    @OnClick(R.id.tv_service_terms)
    public void servicTerms(View view){
        Toast.makeText(this,"服务条款",Toast.LENGTH_SHORT).show();
    }

    //隐私条款
    @OnClick(R.id.tv_privacy_terms)
    public void privacyTerms(View view){
        Toast.makeText(this,"隐私条款",Toast.LENGTH_SHORT).show();
    }

    //忘记密码
    @OnClick(R.id.tv_forget_password)
    public void forgetPassword(View view){
        Toast.makeText(this,"忘记密码",Toast.LENGTH_SHORT).show();
    }

    //注册
    @OnClick(R.id.tv_register)
    public void register(View view){
        Toast.makeText(this,"注册账号",Toast.LENGTH_SHORT).show();
    }

    //快捷登录
    @OnClick(R.id.ll_quick_login)
    public void quickLogin(View view){
        Toast.makeText(this,"快捷登录",Toast.LENGTH_SHORT).show();
    }

    //微信登录
    @OnClick(R.id.ll_wechat_login)
    public void wechatLogin(View view){
        Toast.makeText(this,"微信登录",Toast.LENGTH_SHORT).show();
    }
}
