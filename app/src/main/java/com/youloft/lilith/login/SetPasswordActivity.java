package com.youloft.lilith.login;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置密码界面
 * <p>
 * Created by GYH on 2017/7/3.
 */
@Route(path = "/test/SetPasswordActivity")
public class SetPasswordActivity extends BaseActivity {

    @BindView(R.id.vv_background)
    VideoView vvBackground;  //背景视频
    @BindView(R.id.et_password)
    EditText etPassword;  //输入密码
    @BindView(R.id.iv_is_show_pwd01)
    ImageView ivIsShowPwd01; //输入密码 是否明文显示
    @BindView(R.id.iv_clean_password01)
    ImageView ivCleanPassword01;   //清除输入密码
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;  //确认密码
    @BindView(R.id.iv_is_show_pwd02)
    ImageView ivIsShowPwd02;    //确认密码是否明文显示
    @BindView(R.id.iv_clean_password02)
    ImageView ivCleanPassword02;  //清除确认密码
    @BindView(R.id.btn_login)
    Button btnLogin;  //登录按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        editTextSetting();
    }

    /**
     * 密码框的UI变化设定
     */
    private void editTextSetting() {
        //输入密码输入框的监听
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
                    ivCleanPassword01.setVisibility(View.VISIBLE);
                    ivIsShowPwd01.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPassword01.setVisibility(View.INVISIBLE);
                    ivIsShowPwd01.setVisibility(View.INVISIBLE);
                }
            }
        });
        //确认密码输入框的监听
        etConfirmPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //变化之后如果有字符串 就显示叉叉和眼睛, 如果没有就隐藏叉叉和眼睛
                if (etConfirmPassword.getText().toString().length() != 0) {
                    ivCleanPassword02.setVisibility(View.VISIBLE);
                    ivIsShowPwd02.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPassword02.setVisibility(View.INVISIBLE);
                    ivIsShowPwd02.setVisibility(View.INVISIBLE);
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

    private boolean isShowPassword01 = false;//是否显示密码的标识
    private boolean isShowPassword02 = false;//是否显示密码的标识
    @OnClick({R.id.iv_is_show_pwd01, R.id.iv_clean_password01,R.id.iv_is_show_pwd02, R.id.iv_clean_password02})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_is_show_pwd01:
                if (isShowPassword01) {
                    etPassword.setTransformationMethod(null);
                    ivIsShowPwd01.setImageResource(R.drawable.login_password_on_icon);
                    isShowPassword01 = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivIsShowPwd01.setImageResource(R.drawable.login_password_off_icon);
                    isShowPassword01 = true;
                }
                etPassword.setSelection(etPassword.getText().toString().length());
                break;
            case R.id.iv_clean_password01:
                etPassword.setText(null);
                break;



            case R.id.iv_is_show_pwd02:
                if (isShowPassword02) {
                    etConfirmPassword.setTransformationMethod(null);
                    ivIsShowPwd02.setImageResource(R.drawable.login_password_on_icon);
                    isShowPassword02 = false;
                } else {
                    etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivIsShowPwd02.setImageResource(R.drawable.login_password_off_icon);
                    isShowPassword02 = true;
                }
                etConfirmPassword.setSelection(etConfirmPassword.getText().toString().length());
                break;
            case R.id.iv_clean_password02:
                etConfirmPassword.setText(null);
                break;


        }
    }
}
