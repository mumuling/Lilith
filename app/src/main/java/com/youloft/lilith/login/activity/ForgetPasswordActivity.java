package com.youloft.lilith.login.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.LoginUtils;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.login.bean.SendSmsBean;
import com.youloft.lilith.login.bean.SmsCodeBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.login.repo.SendSmsRepo;
import com.youloft.lilith.login.repo.SmsCodeRepo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 忘记密码界面
 * <p>
 * Created by GYH on 2017/7/6.
 */
@Route(path = "/test/ForgetPasswordActivity")
public class ForgetPasswordActivity extends BaseActivity {
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
    @BindView(R.id.tv_title)
    TextView tvTitle; //大标题
    @BindView(R.id.btn_login)
    Button btnLogin;  //登录或者设置密码的大按钮
    @BindView(R.id.iv_code_right)
    ImageView ivCodeRight; //验证码正确
    @BindView(R.id.iv_code_error)
    ImageView ivCodeError;  //验证码错误

    private int mPreNumberLength;//电话号码变化之前的长度
    private boolean isCodeRight; //验证码是否正确
    private SmsCodeBean mSmsCodeBean; //获取到的验证码的数据模型


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        phoneNumberSetting();
        verificationCodeSetting();
        EventBus.getDefault().register(this);
    }
    //登录成功后,收到事件,关闭本页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent loginEvent) {
        boolean isLogin = loginEvent.isLogin;
        if (isLogin) {
            finish();
        } else {
            //登出了
        }

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
                //当电话号码已经11位数之后做一系列校验
                if (etPhoneNumber.getText().toString().length() == 13) {
                    checkNumber();
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

        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//有内容显示,无内容,隐藏
                    if(android.text.TextUtils.isEmpty(etPhoneNumber.getText().toString())){
                        ivCleanNumber.setVisibility(View.INVISIBLE);
                    }else {
                        ivCleanNumber.setVisibility(View.VISIBLE);
                    }

                } else {//无脑隐藏
                    ivCleanNumber.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    /**
     * 对电话号码做校验
     */
    private void checkNumber() {


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
                        etVerificationCode.setHint(R.string.input_validation_code);
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
                //把输入框的验证码 显示到textview上面
                for (int i = 0; i < 6; i++) {
                    if (i > chars.length - 1) {
                        tvCodes[i].setText(null);
                        vCodeLines[i].setBackgroundResource(R.color.white_50);
                    } else {
                        tvCodes[i].setText(chars[i] + "");
                        vCodeLines[i].setBackgroundResource(R.color.white);
                    }
                }
                if (s.toString().length() == 6) {//验证码6位的时候
                    //检验验证码是否正确
                    getSmsCode();
                } else { //验证码不到6位的时候  一律隐藏验证码后面的小图标
                    ivCodeRight.setVisibility(View.INVISIBLE);
                    ivCodeError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 获取验证码是否正确的请求
     */
    private void getSmsCode() {
        String smsCode = etVerificationCode.getText().toString();
        SmsCodeRepo.getSmsCode(etPhoneNumber.getText().toString().replaceAll("-", ""),"FindPwd",smsCode)
                .compose(this.<SmsCodeBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<SmsCodeBean>() {
                    @Override
                    public void onDataSuccess(SmsCodeBean smsCodeBean) {
                        mSmsCodeBean = smsCodeBean;
                        //这里拿回了验证码的相关信息, 在验证码输入框的监听里面验证用户的验证码是否正确
                        checkSmsCode();
                    }
                });
    }

    /**
     * 查看验证码是否正确,来决定验证码后面的小图标显示哪一个
     */
    private void checkSmsCode() {
        String smsCode = etVerificationCode.getText().toString();
        if (TextUtils.isEmpty(smsCode)) {
            return;
        }
        if (mSmsCodeBean == null) {
            ivCodeRight.setVisibility(View.INVISIBLE);
            ivCodeError.setVisibility(View.VISIBLE);
            return;
        }
        if (mSmsCodeBean.data.result) {//验证码正确
            ivCodeRight.setVisibility(View.VISIBLE);
            ivCodeError.setVisibility(View.INVISIBLE);
            isCodeRight = true;
        } else {//验证码错误
            ivCodeRight.setVisibility(View.INVISIBLE);
            ivCodeError.setVisibility(View.VISIBLE);
            isCodeRight = false;
        }
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

        vvBackground.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // 设置当前播放的位置
//                play(progress);
//                isPlaying = false;
                vvBackground.start();
                return true;//如果设置true就可以防止他弹出错误的提示框！
            }
        });
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
        String disText = tvGetCode.getText().toString();
        if (!getResources().getString(R.string.get_validation_code).equals(disText)) {
            return;
        }
        //1.检验手机号码长度
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        if (TextUtils.isEmpty(phoneNumber)) {
            Toaster.showShort("手机号码不能为空");
            return;
        }
        if(!LoginUtils.isPhoneNumber(phoneNumber)){
            Toaster.showShort("手机号码不正确");
            return;
        }
        //2.发起发送验证码的请求
        SendSmsRepo.sendSms(phoneNumber,"FindPwd")
                .compose(this.<SendSmsBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<SendSmsBean>() {
                    @Override
                    public void onDataSuccess(SendSmsBean sendSmsBean) {
                        //证明验证码一件发送到手机了
                    }
                });
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
            if (mTime == 0) {
                tvGetCode.setText(R.string.get_validation_code);
                mTime = 60;
            } else {
                tvGetCode.setText(mTime + getResources().getString(R.string.re_send));
            }
        }
    };

    //下面的的大按钮的点击事件
    @OnClick(R.id.btn_login)
    public void onButtonClick() {
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        String smsCode = etVerificationCode.getText().toString();
        //1. 手机号码和验证是否为空,长度是否正确
        //2. isCodeRight 是否为true
        //3. 跳转设置密码界面

        if(TextUtils.isEmpty(phoneNumber)||TextUtils.isEmpty(smsCode)){
            Toaster.showShort("手机号码或者验证码不能为空");
            return;
        }
        if(!LoginUtils.isPhoneNumber(phoneNumber)){
            Toaster.showShort("手机号码不正确");
            return;
        }

        if(smsCode.trim().length()!= 6){
            Toaster.showShort("请检查验证码");
            return;
        }

        if(!isCodeRight){
            Toaster.showShort("验证码错误");
            return;
        }
        //这些条件都满足后,带着手机号码和验证码到设置密码界面
        ARouter.getInstance()
                .build("/test/SetPasswordActivity")
                .withString("phoneNumber",phoneNumber)
                .withString("smsCode",smsCode)
                .withString("source","20002")
                .navigation();
        //关掉本页面
        finish();
    }


    //离开时移除活动中的handler
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
    }

    //点击清除电话号码
    @OnClick(R.id.iv_clean_number)
    public void onViewClicked() {
        etPhoneNumber.setText(null);
    }

    @OnClick(R.id.iv_back)
    public void onBackClicked() {
        onBackPressed();
    }
}
