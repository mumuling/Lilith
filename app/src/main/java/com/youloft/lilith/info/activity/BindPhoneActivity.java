package com.youloft.lilith.info.activity;

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
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.LoginUtils;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.MediaPlayerHelper;
import com.youloft.lilith.login.PhoneFocusChangeListener;
import com.youloft.lilith.login.bean.SendSmsBean;
import com.youloft.lilith.login.bean.SmsCodeBean;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.repo.SendSmsRepo;
import com.youloft.lilith.login.repo.SmsCodeRepo;
import com.youloft.lilith.register.bean.CheckPhoneBean;
import com.youloft.lilith.register.repo.CheckPhoneRepo;
import com.youloft.lilith.setting.AppSetting;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 绑定手机界面
 * Created by gyh on 2017/7/11.
 */
@Route(path = "/test/BindPhoneActivity")
public class BindPhoneActivity extends BaseActivity{
    @BindView(R.id.sv_background)
    SurfaceView svBackground;  //背景视频
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
    @BindView(R.id.tv_exist_number)
    TextView tvExistNumber;  //已经存在的电话号码
    @BindView(R.id.iv_number_right)
    ImageView ivNumberRight; //可以使用的手机号码


    private boolean isNumberRight = true;  //验证电话号码是否已经存在之后的标识 true 存在   false 不存在
    private boolean isCodeRight; //验证码是否正确
    private SmsCodeBean mSmsCodeBean; //获取到的验证码的数据模型
    private MediaPlayerHelper mMediaPlayerHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
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
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == '-') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != '-') {
                            sb.insert(sb.length() - 1, '-');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == '-') {
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
                    etPhoneNumber.setText(sb.toString());
                    etPhoneNumber.setSelection(index);
                }
                //当电话号码已经11位数之后做一系列校验
                if (etPhoneNumber.getText().toString().length() == 13) {
                    checkNumber();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhoneNumber.setOnFocusChangeListener(new PhoneFocusChangeListener(etPhoneNumber,ivCleanNumber));
    }


    /**
     * 对电话号码做校验
     */
    private void checkNumber() {
        //手机号码长度满足之后,
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        //发起号码验证请求
        CheckPhoneRepo.checkPhone(phoneNumber)
                .compose(this.<CheckPhoneBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<CheckPhoneBean>() {
                    @Override
                    public void onDataSuccess(CheckPhoneBean checkPhoneBean) {
                        if(checkPhoneBean.data.result == 0){
                            //如果成功了,根据信息进行判断
                            showIsExist(checkPhoneBean);
                        }else {
                            Toaster.showShort("网络错误");
                        }

                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("网络错误");
                    }
                });
    }

    /**
     * 根据回来的信息来显示
     *
     * @param checkPhoneBean
     */
    private void showIsExist(CheckPhoneBean checkPhoneBean) {
        //0：不存在 1：存在 2：无效手机号
        if (checkPhoneBean.isSuccess()) {
            switch (checkPhoneBean.data.result) {
                case 0://显示钩钩
                    ivNumberRight.setVisibility(View.VISIBLE);
                    tvExistNumber.setVisibility(View.INVISIBLE);
                    isNumberRight = false;
                    break;
                case 1://显示已经注册账号
                case 2:
                    tvExistNumber.setVisibility(View.VISIBLE);
                    ivNumberRight.setVisibility(View.INVISIBLE);
                    isNumberRight = true;
                    break;
            }
        }
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
        mMediaPlayerHelper = MediaPlayerHelper.initMediaPlayerHelper(this, svBackground);
    }


    /**
     * 获取验证码点击事件
     */
    @OnClick(R.id.tv_get_code)
    public void getCode() {
        // 1.  先判断电话号码的长度
        // 2.  给电话号码做正则校验
        // 3.  手机号码验证存在与否的标识
        // 4.  是否在一分钟的重发时间内
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        if (TextUtils.isEmpty(phoneNumber)){
            Toaster.showShort("手机码不能为空!");
            return;
        }
        if(!LoginUtils.isPhoneNumber(phoneNumber)){
            Toaster.showShort("手机号码不正确!");
            return;
        }
        if (isNumberRight) {
            Toaster.showShort("该手机号码已绑定！");
            return;
        }
        String disText = tvGetCode.getText().toString();
        if (!getResources().getString(R.string.get_validation_code).equals(disText)) {
            return;
        }
        //发送短信
        SendSmsRepo.sendSms(phoneNumber,"Register")
                .compose(this.<SendSmsBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<SendSmsBean>() {
                    @Override
                    public void onDataSuccess(SendSmsBean sendSmsBean) {
                        //确认短信发送成功了
                    }
                });


        handler.postDelayed(runnable, 0);
    }

    /**
     * 获取验证码是否正确的请求
     */
    private void getSmsCode() {
        //发起获取验证码的请求
        String smsCode = etVerificationCode.getText().toString();
        SmsCodeRepo.getSmsCode(etPhoneNumber.getText().toString().replaceAll("-", ""),"Register",smsCode)
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
        // 1. isNumberRight 为false 代表可以注册
        // 2. isCodeRigth 为true  代表可以注册
        // 3. 手机号码和验证码长度检验
        // 4. 手机正则校验
        if(isNumberRight || !isCodeRight){
            Toaster.showShort("手机号码已存在,或者验证码错误");
            return;
        }
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        String smsCode = etVerificationCode.getText().toString();

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

        final UserBean userInfo = AppSetting.getUserInfo();
        if(userInfo == null){
            Toaster.showShort("内部信息错误");
            return;
        }
        //这些条件都满足后,发起请求
        UpdateUserRepo.bindPhone(smsCode,phoneNumber,String.valueOf(userInfo.data.userInfo.id))
                .compose(this.<UserBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    public void onDataSuccess(UserBean userBean) {
                        if(userBean.data.result == 0){
                            AppSetting.saveUserInfo(userBean);
                            Toaster.showShort("绑定成功");
                            finish();

                        }else {
                            Toaster.showShort("绑定失败");
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("网络错误");
                    }
                });
    }


    //离开时移除活动中的handler
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayerHelper.release();
        handler.removeCallbacks(runnable);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
