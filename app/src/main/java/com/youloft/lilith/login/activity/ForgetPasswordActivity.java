package com.youloft.lilith.login.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.LoginUtils;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.login.BaseTextWatcher;
import com.youloft.lilith.login.MediaPlayerHelper;
import com.youloft.lilith.login.PhoneFocusChangeListener;
import com.youloft.lilith.login.PhoneTextWatcher;
import com.youloft.lilith.login.bean.SendSmsBean;
import com.youloft.lilith.login.bean.SmsCodeBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.login.repo.SendSmsRepo;
import com.youloft.lilith.login.repo.SmsCodeRepo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 忘记密码界面
 * 注意:修改密码也会走这边了
 * <p>
 * Created by GYH on 2017/7/6.
 */
@Route(path = "/test/ForgetPasswordActivity")
public class ForgetPasswordActivity extends BaseActivity implements BaseTextWatcher.OnTextChange {
    @BindView(R.id.sv_background)
    SurfaceView svBackground;  //背景视频
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode; //输入验证码的editText
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;  //手机号码
    @BindView(R.id.iv_clean_number)
    ImageView ivCleanNumber; //情况手机号码
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

    private boolean isCodeRight; //验证码是否正确
    private SmsCodeBean mSmsCodeBean; //获取到的验证码的数据模型
    public static final String FORGET_PASSWORD_FLAG = "22";//这是代表忘记密码
    public static final String MODIFY_PASSWORD_FLAG = "33";//这是代修修改密码
    private String mFlag; //上个界面传过来的flag  判断是修改密码,还是忘记密码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mFlag = getIntent().getStringExtra("flag");
        ButterKnife.bind(this);
        initView();
        phoneNumberSetting();
        verificationCodeSetting();
        EventBus.getDefault().register(this);
    }

    /**
     * 根据上个界面传来的flag,来判断显示忘记密码 还是 修改密码
     */
    private void initView() {
        if (mFlag.equals(FORGET_PASSWORD_FLAG)) {//忘记密码
            tvTitle.setText(R.string.forget_password);
        } else {//修改密码
            tvTitle.setText(R.string.modify_password);
        }
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
        //电话号码变化的监听
        etPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        etPhoneNumber.addTextChangedListener(new PhoneTextWatcher(etPhoneNumber, ivCleanNumber, this));
        etPhoneNumber.setOnFocusChangeListener(new PhoneFocusChangeListener(etPhoneNumber, ivCleanNumber));
    }


    /**
     * 验证码输入相关
     */
    private void verificationCodeSetting() {

        etVerificationCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        etVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == 11) {//验证码6位的时候
                    //检验验证码是否正确
                    getSmsCode();
                } else { //验证码不到6位的时候  一律隐藏验证码后面的小图标
                    ivCodeRight.setVisibility(View.INVISIBLE);
                    ivCodeError.setVisibility(View.INVISIBLE);
                }

                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 1 && i != 3 && i != 5 && i != 7 && i != 9 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 2 || sb.length() == 4 || sb.length() == 6 || sb.length() == 8 || sb.length() == 10)
                                && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    etVerificationCode.setText(sb.toString());
                    etVerificationCode.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isCodeEmpty = !TextUtils.isEmpty(etVerificationCode.getText().toString());
                if (isCodeEmpty && isPhoneEmpty) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }

    private boolean isCodeEmpty = false;
    private boolean isPhoneEmpty = false;

    @Override
    public void onChange(boolean isValid, EditText view) {
        if (view == etPhoneNumber) {
            isPhoneEmpty = isValid;
        }
        if (isCodeEmpty && isPhoneEmpty) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    /**
     * 获取验证码是否正确的请求
     */
    private void getSmsCode() {
        String smsCode = etVerificationCode.getText().toString();
        smsCode = smsCode.replaceAll(" ","");
        SmsCodeRepo.getSmsCode(etPhoneNumber.getText().toString().replaceAll("-", ""), "FindPwd", smsCode)
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
        MediaPlayerHelper.getInstance().register(this, svBackground);
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
        if (!LoginUtils.isPhoneNumber(phoneNumber)) {
            Toaster.showShort("手机号码不正确");
            return;
        }
        //2.发起发送验证码的请求
        SendSmsRepo.sendSms(phoneNumber, "FindPwd")
                .compose(this.<SendSmsBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<SendSmsBean>() {
                    @Override
                    public void onDataSuccess(SendSmsBean sendSmsBean) {
                        //证明验证码一件发送到手机了
                        if(sendSmsBean == null){
                            return;
                        }
                        if(sendSmsBean.data.msg.equals("短信发送超出限制")){
                            Toaster.showShort("超出发送数量限制");
                            handler.removeCallbacks(runnable);
                            tvGetCode.setText(R.string.get_validation_code);
                            return;
                        }
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

        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(smsCode)) {
            Toaster.showShort("手机号码或者验证码不能为空");
            return;
        }
        if (!LoginUtils.isPhoneNumber(phoneNumber)) {
            Toaster.showShort("手机号码不正确");
            return;
        }
        smsCode = smsCode.replaceAll(" ","").trim();
        if (smsCode.trim().length() != 6) {
            Toaster.showShort("请检查验证码");
            return;
        }

        if (!isCodeRight) {
            Toaster.showShort("验证码错误");
            return;
        }
        //这些条件都满足后,带着手机号码和验证码到设置密码界面
        //判断是应该携带哪个source
        String source;
        if (mFlag.equals(FORGET_PASSWORD_FLAG)) {//忘记密码
            source = "20002";
        } else {//修改密码
            source = "20003";
        }

        ARouter.getInstance()
                .build("/test/SetPasswordActivity")
                .withString("phoneNumber", phoneNumber)
                .withString("smsCode", smsCode)
                .withString("source", source)
                .navigation();
        //关掉本页面
        finish();
    }


    //离开时移除活动中的handler
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerHelper.getInstance().unregister(this);
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
