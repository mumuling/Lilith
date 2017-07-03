package com.youloft.lilith.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.Toaster;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 快速登录界面
 * <p>
 * Created by GYH on 2017/6/29.
 */

public class QuickLoginActivity extends BaseActivity {
    @BindView(R.id.vv_background)
    VideoView vvBackground;  //背景视频
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode; //输入验证码的editText
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;  //手机号码
    @BindView(R.id.iv_clean_number)
    ImageView ivCleanNumber; //情况手机号码
    @BindViews({R.id.tv_code01, R.id.tv_code02, R.id.tv_code03, R.id.tv_code04, R.id.tv_code05, R.id.tv_code06})
    TextView[] tvCodes;   //验证码数字显示
    @BindViews({R.id.v_code_line01, R.id.v_code_line02, R.id.v_code_line03, R.id.v_code_line04, R.id.v_code_line05, R.id.v_code_line06})
    View[] vCodeLines;   //验证码底部下划线
    @BindView(R.id.ll_code_container)
    LinearLayout llCodeContainer;  //验证码容器
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;   //获取验证码

    private int mPreNumberLength;//电话号码变化之前的长度

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login);
        ButterKnife.bind(this);
        phoneNumberSetting();
        verificationCodeSetting();

    }

    /**
     * 号码输入框的设定
     */
    private void phoneNumberSetting() {
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
                if (text.length() >= 4 && !String.valueOf(text.charAt(3)).equals("-")) {
                    String result = text.substring(0, 3) + "-" + text.substring(3);
                    etPhoneNumber.setText(result);
                    etPhoneNumber.setSelection(etPhoneNumber.getText().toString().length());
                }
                if (text.length() >= 9 && !String.valueOf(text.charAt(8)).equals("-")) {
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
    }

    /**
     * 验证码输入相关
     */
    private void verificationCodeSetting() {
        etVerificationCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //获取到焦点
                if (hasFocus) {
                    llCodeContainer.setVisibility(View.VISIBLE);
                    etVerificationCode.setHint(null);
                    //获取到焦点的时候,始终把光标放在最后面
                    etVerificationCode.setSelection(etVerificationCode.getText().toString().length());
                } else {//失去焦点的时候,判断有没有文字
                    if (etVerificationCode.getText().toString().equals("")) {
                        llCodeContainer.setVisibility(View.INVISIBLE);
                        etVerificationCode.setHint("输入验证码");
                    }
                }
            }
        });
        etVerificationCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        etVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = s.toString();
                char[] chars = content.toCharArray();
                for (int i = 0; i < 6; i++) {
                    if (i > chars.length - 1) {
                        tvCodes[i].setText(null);
                        vCodeLines[i].setBackgroundColor(0x55ffffff);
                    } else {
                        tvCodes[i].setText(chars[i] + "");
                        vCodeLines[i].setBackgroundColor(0xffffffff);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

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


    /**
     * 获取验证码点击事件
     */
    @OnClick(R.id.tv_get_code)
    public void getCode() {
        //当前显示的文字
        String disText = tvGetCode.getText().toString();
        if(!"获取验证码".equals(disText)){
            return;
        }
        handler.postDelayed(runnable, 0);
    }
    //下面的handler是玩倒计时的
    private int mTime = 60;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTime--;
            if (mTime > 0) {
                handler.postDelayed(this, 1000);
            }
            if(mTime == 0){
                tvGetCode.setText("获取验证码");
                mTime = 60;
            }else {
                tvGetCode.setText(mTime + "s后重发");
            }
        }
    };

    //下面的的大按钮的点击事件
    @OnClick(R.id.btn_login)
    public void onButtonClick(){
        startActivity(new Intent(this,SetPasswordActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
