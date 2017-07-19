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
import com.youloft.lilith.login.BaseTextWatcher.OnTextChange;
import com.youloft.lilith.login.MediaPlayerHelper;
import com.youloft.lilith.login.PhoneFocusChangeListener;
import com.youloft.lilith.login.PhoneTextWatcher;
import com.youloft.lilith.login.bean.SendSmsBean;
import com.youloft.lilith.login.bean.SmsCodeBean;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.login.repo.SendSmsRepo;
import com.youloft.lilith.login.repo.SmsCodeRepo;
import com.youloft.lilith.login.repo.UserRepo;
import com.youloft.lilith.setting.AppSetting;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 快捷登录
 * <p>
 * <p>
 * Created by GYH on 2017/6/29.
 */
@Route(path = "/test/UserFunctionActivity")
public class UserFunctionActivity extends BaseActivity implements OnTextChange {


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
    @BindView(R.id.iv_code_right)
    ImageView ivCodeRight; //验证码正确
    @BindView(R.id.iv_code_error)
    ImageView ivCodeError;  //验证码错误
    @BindView(R.id.btn_login)
    Button btnLogin;

    private boolean isCodeRight; //验证码是否正确
    private SmsCodeBean mSmsCodeBean; //获取到的验证码的数据模型


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_function);
        ButterKnife.bind(this);
        phoneNumberSetting();
        verificationCodeSetting();

    }


    /**
     * 号码输入框的设定
     */
    private void phoneNumberSetting() {

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
        //如果是注册账号界面,需要在账号是否存在判断之后才
        String disText = tvGetCode.getText().toString();
        if (!getResources().getString(R.string.get_validation_code).equals(disText)) {
            return;
        }
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        if (TextUtils.isEmpty(phoneNumber)) {
            Toaster.showShort("手机号码不能为空");
            return;
        }

        if (!LoginUtils.isPhoneNumber(phoneNumber)) {
            Toaster.showShort("手机号码不正确");
            return;
        }
        //发送短信
        SendSmsRepo.sendSms(phoneNumber, "Login")
                .compose(this.<SendSmsBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<SendSmsBean>() {
                    @Override
                    public void onDataSuccess(SendSmsBean sendSmsBean) {
                        //确认短信发送成功了
                        if (sendSmsBean == null) {
                            return;
                        }
                        if (sendSmsBean.data.msg.equals("短信发送超出限制")) {
                            Toaster.showShort("超出发送数量限制");
                            return;
                        }

                    }
                });


        handler.postDelayed(runnable, 0);
    }

    /**
     * 获取验证码是否正确的请求
     */
    private void getSmsCode() {
        String smsCode = etVerificationCode.getText().toString().trim();
        smsCode = smsCode.replaceAll(" ","");
        SmsCodeRepo.getSmsCode(etPhoneNumber.getText().toString().replaceAll("-", ""), "Login", smsCode)
                .compose(this.<SmsCodeBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<SmsCodeBean>() {
                    @Override
                    public void onDataSuccess(SmsCodeBean smsCodeBean) {
                        if (smsCodeBean == null) {
                            return;
                        }
                        mSmsCodeBean = smsCodeBean;
                        //这里拿回了验证码的相关信息, 在验证码输入框的监听里面验证用户的验证码是否正确
                        checkSmsCode();
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("网络错误");
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
        //这里做出判断,是哪个界面,做出对应的请求
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        String smsCode = etVerificationCode.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(smsCode)) {
            Toaster.showShort("信息不能为空");
            return;
        }
        if (!LoginUtils.isPhoneNumber(phoneNumber)) {
            Toaster.showShort("手机号码不正确");
            return;
        }
        smsCode = smsCode.replaceAll(" ","").trim();
        if (isCodeRight) {  //这里的变量是验证了验证码之后  正确的时候才为true
            quicklyLogin(phoneNumber, smsCode);
        }

    }

    /**
     * 发起登录请求
     *
     * @param phoneNumber 电话号码
     * @param smsCode     验证码
     */
    private void quicklyLogin(String phoneNumber, String smsCode) {
        UserRepo.loginForUserInfo(phoneNumber, smsCode)
                .compose(this.<UserBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    public void onDataSuccess(UserBean userBean) {
                        if (userBean.data.result == 0) {

                            AppSetting.saveUserInfo(userBean); //保存用户信息
                            EventBus.getDefault().post(new LoginEvent(true));//发送登录事件
                            if (TextUtils.isEmpty(userBean.data.userInfo.birthLongi)) { //资料不完整
                                ARouter.getInstance().build("/test/EditInformationActivity").navigation();
                            }
                            Toaster.showShort("登录成功");
                            finish();
                        } else if (userBean.data.result == 4) {
                            Toaster.showShort("账号已禁用");
                        } else if (userBean.data.result == 3) {
                            Toaster.showShort("无此手机号");
                        } else if (userBean.data.result == 2) {
                            Toaster.showShort("验证码不正确或已失效");
                        } else if (userBean.data.result == 1) {
                            Toaster.showShort("手机号无效");
                        } else {
                            Toaster.showShort("网络异常");
                        }

                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("网络异常");
                    }
                });
    }

    //离开时移除活动中的handler
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        MediaPlayerHelper.getInstance().unregister(this);
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
