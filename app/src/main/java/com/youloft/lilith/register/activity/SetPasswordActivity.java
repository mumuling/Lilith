package com.youloft.lilith.register.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.info.activity.SettingActivity;
import com.youloft.lilith.login.MediaPlayerHelper;
import com.youloft.lilith.login.bean.ModifyPasswordBean;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.login.event.ModifyPasswordEvent;
import com.youloft.lilith.login.repo.ModifyPasswordRepo;
import com.youloft.lilith.register.repo.RegisterUserRepo;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.TopicInfoCache;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.ui.TabManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置密码界面
 * <p>
 * Created by GYH on 2017/7/3.
 */
@Route(path = "/test/SetPasswordActivity")
public class SetPasswordActivity extends BaseActivity {

    @BindView(R.id.sv_background)
    SurfaceView svBackground;  //背景视频
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
    Button btnLogin;
    @BindView(R.id.tv_back)
    TextView tvBack;
    private String phoneNumber; //上个页面传来的手机号码
    private String smsCode;  //上个页面传来的验证码
    private String source;  //从哪个页面来的,用于区分,最后该发起哪个请求   20001:注册页面   20002:忘记密码页面  20003:修改密码界面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        //获取上个页面传过来的手机号码和验证码
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        smsCode = getIntent().getStringExtra("smsCode");
        //从哪个界面来的
        source = getIntent().getStringExtra("source");

        if (source.equals("20001")) {  //注册账号
            tvBack.setText("注册账号");
            btnLogin.setText("登录");
        } else if (source.equals("20002")) { //忘记密码
            tvBack.setText("忘记密码");
            btnLogin.setText("确认");
        } else {   //修改密码
            tvBack.setText("修改密码");
            btnLogin.setText("确认");
        }

        editTextSetting();
    }


    private boolean isPwdEmpty = false;
    private boolean isConfirmPwdEmpty = false;

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
                if (android.text.TextUtils.isEmpty(etPassword.getText().toString())) {
                    isPwdEmpty = false;
                } else {
                    isPwdEmpty = true;
                }
                if (isPwdEmpty && isConfirmPwdEmpty) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
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
                if (android.text.TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
                    isConfirmPwdEmpty = false;
                } else {
                    isConfirmPwdEmpty = true;
                }
                if (isPwdEmpty && isConfirmPwdEmpty) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerHelper.getInstance().register(this, svBackground);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerHelper.getInstance().unregister(this);
    }


    private boolean isShowPassword01 = false;//是否显示密码的标识
    private boolean isShowPassword02 = false;//是否显示密码的标识

    @OnClick({R.id.iv_is_show_pwd01, R.id.iv_clean_password01, R.id.iv_is_show_pwd02, R.id.iv_clean_password02})
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

    //返回按钮
    @OnClick(R.id.fl_back)
    public void onBackClicked() {
        onBackPressed();
    }

    //登录按钮
    @OnClick(R.id.btn_login)
    public void onButtonClicked() {
        //1.获取两个输入的密码,做非空校验,做比对
        //2.手机号码,验证码,密码一起请求注册接口
        String password = etPassword.getText().toString();
        String confirmPwd = etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPwd)) {
            return;
        }
        if (!password.trim().equals(confirmPwd.trim())) {
            Toaster.showShort("密码不一致");
            return;
        }
        if (password.trim().length() < 6 || confirmPwd.trim().length() < 6) {
            Toaster.showShort("密码长度不能低于6位");
            return;
        }
        if (source.equals("20001")) {//注册账号
            RegisterUserRepo.registerUser(phoneNumber, smsCode, password)
                    .compose(this.<UserBean>bindToLifecycle())
                    .subscribeOn(Schedulers.newThread())
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxObserver<UserBean>() {
                        @Override
                        public void onDataSuccess(UserBean userBean) {
                            if (userBean.data.result == 0) {
                                //这里代表注册成功,并且也登录了
                                AppSetting.saveUserInfo(userBean); //保存用户信息
                                EventBus.getDefault().post(new LoginEvent(true));//发送登录事件
                                if (android.text.TextUtils.isEmpty(userBean.data.userInfo.birthLongi)) { //新用户
                                    ARouter.getInstance().build("/test/EditInformationActivity").navigation();
                                }
                                finish();
                            } else {
                                Toaster.showShort("注册失败");
                            }
                        }

                        @Override
                        protected void onFailed(Throwable e) {
                            super.onFailed(e);
                            Toaster.showShort("网络错误");
                        }
                    });
        } else if(source.equals("20002")){ //忘记密码
            ModifyPasswordRepo.modifyPassword(phoneNumber, smsCode, password)
                    .compose(this.<ModifyPasswordBean>bindToLifecycle())
                    .subscribeOn(Schedulers.newThread())
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxObserver<ModifyPasswordBean>() {
                        @Override
                        public void onDataSuccess(ModifyPasswordBean modifyPasswordBean) {

                            if (modifyPasswordBean.data.result == 0) {//找回密码成功  打开登录页面
                                ARouter.getInstance().build("/test/LoginActivity").navigation();
                                Toaster.showShort("密码设置成功");
                                finish();
                            } else {//失败
                                Toaster.showShort("网络错误");
                            }
                        }

                        @Override
                        protected void onFailed(Throwable e) {
                            super.onFailed(e);
                            Toaster.showShort("网络错误");
                        }
                    });
        }else {//修改密码
            ModifyPasswordRepo.modifyPassword(phoneNumber, smsCode, password)
                    .compose(this.<ModifyPasswordBean>bindToLifecycle())
                    .subscribeOn(Schedulers.newThread())
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxObserver<ModifyPasswordBean>() {
                        @Override
                        public void onDataSuccess(ModifyPasswordBean modifyPasswordBean) {

                            if (modifyPasswordBean.data.result == 0) {//修改密码成功  打开登录页面
                                ARouter.getInstance().build("/test/LoginActivity").navigation();
                                Toaster.showShort("密码修改成功");
                                //这个时候需要退出登录 需要通知settingActivity关闭
                                AppSetting.clearUserInfo();
                                PointCache.getIns(SetPasswordActivity.this).deleteTable();
                                TopicLikeCache.getIns(SetPasswordActivity.this).deleteTable();
                                PointAnswerCache.getIns(SetPasswordActivity.this).deleteTable();
                                TopicInfoCache.getIns(SetPasswordActivity.this).deleteTable();
                                EventBus.getDefault().post(new LoginEvent(false));
                                EventBus.getDefault().post(new TabChangeEvent(TabManager.TAB_INDEX_XZ));
                                EventBus.getDefault().post(new ModifyPasswordEvent());
                                finish();
                            } else {//失败
                                Toaster.showShort("修改密码失败");
                            }
                        }

                        @Override
                        protected void onFailed(Throwable e) {
                            super.onFailed(e);
                            Toaster.showShort("网络错误");
                        }
                    });
        }

    }
}
