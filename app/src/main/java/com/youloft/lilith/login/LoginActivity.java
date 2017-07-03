package com.youloft.lilith.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;

import org.w3c.dom.Text;

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
    @BindView(R.id.iv_clean_number)
    ImageView ivCleanNumber; //清空电话号码
    @BindView(R.id.iv_is_show_pwd)
    ImageView ivIsShowPwd; //是否明文展示密码
    @BindView(R.id.iv_clean_password)
    ImageView ivCleanPassword; //清空密码

    private int mPreNumberLength;//电话号码变化之前的长度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initEditText();
    }

    /**
     * 输入框的设定
     */
    private void initEditText() {
        etPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        //电话号码变化的监听
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPreNumberLength = s.toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //内容发生变化时,判断是否添加-
                boolean flag = etPhoneNumber.getText().toString().length() > mPreNumberLength;
                String text = etPhoneNumber.getText().toString();
                if (text.length() == 3 && flag || text.length() == 8 && flag) {
                    etPhoneNumber.setText(s.toString() + "-");
                    etPhoneNumber.setSelection(etPhoneNumber.getText().toString().length());
                }
                if (text.length() >= 4 && !String.valueOf(text.charAt(3)).equals("-")){
                    String result = text.substring(0, 3) + "-" + text.substring(3);
                    etPhoneNumber.setText(result);
                    etPhoneNumber.setSelection(etPhoneNumber.getText().toString().length());
                }
                if (text.length() >= 9 && !String.valueOf(text.charAt(8)).equals("-")){
                    String result = text.substring(0, 8) + "-" + text.substring(8);
                    etPhoneNumber.setText(result);
                    etPhoneNumber.setSelection(etPhoneNumber.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //变化之后如果有字符串 就显示叉叉, 如果没有就隐藏叉叉
                if (etPhoneNumber.getText().toString().length() != 0) {
                    ivCleanNumber.setVisibility(View.VISIBLE);
                } else {
                    ivCleanNumber.setVisibility(View.INVISIBLE);
                }
            }
        });


        //密码输入框的监听
        etPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //变化之后如果有字符串 就显示叉叉和眼睛, 如果没有就隐藏叉叉和眼睛
                if (etPassword.getText().toString().length() != 0) {
                    ivCleanPassword.setVisibility(View.VISIBLE);
                    ivIsShowPwd.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPassword.setVisibility(View.INVISIBLE);
                    ivIsShowPwd.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
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
    public void login(View view) {
        Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
    }

    //服务条款
    @OnClick(R.id.tv_service_terms)
    public void servicTerms(View view) {
        Toast.makeText(this, "服务条款", Toast.LENGTH_SHORT).show();
    }

    //隐私条款
    @OnClick(R.id.tv_privacy_terms)
    public void privacyTerms(View view) {
        Toast.makeText(this, "隐私条款", Toast.LENGTH_SHORT).show();
    }

    //忘记密码
    @OnClick(R.id.tv_forget_password)
    public void forgetPassword(View view) {
        Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
    }

    //注册
    @OnClick(R.id.tv_register)
    public void register(View view) {
        Toast.makeText(this, "注册账号", Toast.LENGTH_SHORT).show();
    }

    //快捷登录
    @OnClick(R.id.ll_quick_login)
    public void quickLogin(View view) {
        ARouter.getInstance().build("/test/QuickLoginActivity").navigation();
    }

    //微信登录
    @OnClick(R.id.ll_wechat_login)
    public void wechatLogin(View view) {
        Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show();
    }

    private boolean isShowPassword = true;//是否显示密码的标识

    //清空账号密码,和是否明文显示密码的点击事件
    @OnClick({R.id.iv_clean_number, R.id.iv_is_show_pwd, R.id.iv_clean_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_number:
                etPhoneNumber.setText(null);
                break;
            case R.id.iv_is_show_pwd:
                if (isShowPassword) {
                    etPassword.setTransformationMethod(null);
                    ivIsShowPwd.setImageResource(R.drawable.login_password_on_icon);
                    isShowPassword = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivIsShowPwd.setImageResource(R.drawable.login_password_off_icon);
                    isShowPassword = true;
                }
                etPassword.setSelection(etPassword.getText().toString().length());

                break;
            case R.id.iv_clean_password:
                etPassword.setText(null);
                break;
        }
    }
}
