package com.youloft.lilith.login.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.LoginUtils;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.widgets.dialog.PrivacyDialog;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.login.repo.LoginUserRepo;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.socialize.SocializeApp;
import com.youloft.socialize.SocializePlatform;
import com.youloft.socialize.wrapper.AuthListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录界面
 * <p>
 * Created by GYH on 2017/6/29.
 */
@Route(path = "/test/LoginActivity")
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocializeApp.get(this).onActivityResult(requestCode, resultCode, data);
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
        //1.判断手机号 和 密码是否为空
        //2.判断手机号码的位数
        //3.发起登录请求
        String phoneNumber = etPhoneNumber.getText().toString().replaceAll("-", "");
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
            Toaster.showShort("手机号码或者密码不能为空");
            return;
        }
        if (phoneNumber.length() != 11) {
            Toaster.showShort("手机号码不正确");
            return;
        }
        if(!LoginUtils.isPhoneNumber(phoneNumber)){
            Toaster.showShort("手机号码不正确");
            return;
        }
        LoginUserRepo.loginWithPassword(phoneNumber, password)
                .compose(this.<UserBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    public void onDataSuccess(UserBean userBean) {
                        if (userBean.data.result == 0) {
                            AppSetting.saveUserInfo(userBean); //保存用户信息
                            AppConfig.LOGIN_STATUS = true; //设置登录标识
                            EventBus.getDefault().post(new LoginEvent(true));//发送登录事件
                            finish();

                        } else {
                            Toaster.showShort("密码错误");
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("网络错误");
                    }
                });
    }


    //隐私条款
    @OnClick(R.id.ll_privacy_terms)
    public void privacyTerms(View view) {
        PrivacyDialog privacyDialog = new PrivacyDialog(this);
        privacyDialog.show();
    }

    //忘记密码
    @OnClick(R.id.tv_forget_password)
    public void forgetPassword(View view) {
        ARouter.getInstance()
                .build("/test/ForgetPasswordActivity")
                .navigation();
    }

    //注册
    @OnClick(R.id.tv_register)
    public void register(View view) {
        ARouter.getInstance()
                .build("/test/RegisterActivity")
                .navigation();
    }

    //快捷登录
    @OnClick(R.id.ll_quick_login)
    public void quickLogin(View view) {
        ARouter.getInstance()
                .build("/test/UserFunctionActivity")
                .navigation();
    }

    //微信登录
    @OnClick(R.id.ll_wechat_login)
    public void wechatLogin(View view) {
        SocializeApp.get(this).getPlatformInfo(this, SocializePlatform.WEIXIN, new AuthListener() {
            @Override
            public void onStart(SocializePlatform platform) {
            }

            @Override
            public void onComplete(SocializePlatform platform, int code, Map<String, String> data) {
                thirdLogin(platform, data);
            }

            @Override
            public void onError(SocializePlatform platform, int code, Throwable err) {
            }

            @Override
            public void onCancel(SocializePlatform platform, int code) {
            }
        });
    }

    /**
     * 三方登录回调成功
     *
     * @param plf  暂时不用
     * @param data
     */
    private void thirdLogin(SocializePlatform plf, Map<String, String> data) {
        String nickName = data.get("name");
        String platform = "0";
        String headimgurl = data.get("iconurl");
        String openid = data.get("openid");
        String gender = data.get("gender");
        String nickName64 = Base64.encodeToString(nickName.getBytes(), Base64.DEFAULT);
        //由于服务器对性别的区分是1 2 所以做一下转换
        if ("男".equals(gender)) {
            gender = "1";
        } else if ("女".equals(gender)) {
            gender = "2";
        } else {
            gender = "0";
        }
        LoginUserRepo.wechatLogin(nickName64, platform, headimgurl, openid, gender)
                .compose(this.<UserBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    public void onDataSuccess(UserBean userBean) {
                        if (userBean.data.result == 0) {
                            AppSetting.saveUserInfo(userBean); //保存用户信息
                            AppConfig.LOGIN_STATUS = true; //设置登录标识
                            EventBus.getDefault().post(new LoginEvent(true));//发送登录事件
                            finish();
                        } else {
                            Toaster.showShort("登录失败");
                        }

                    }

                });
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


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
