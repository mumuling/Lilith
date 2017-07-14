package com.youloft.lilith.info.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.info.event.BindAccountEvent;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.view.BaseToolBar;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 绑定账号界面
 * <p>
 * Created by GYH on 2017/6/30.
 */
@Route(path = "/test/BindAccountActivity")
public class BindAccountActivity extends BaseActivity {
    @BindView(R.id.btl_bind_account)
    BaseToolBar btlBindAccount;

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_bind_weixin)
    TextView tvBindWeixin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_accont);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initTilte();
        //进来的时候看看用户是否有手机号码,有就显示绑定的手机
        initView();
    }

    private void initTilte() {
        btlBindAccount.setTitle(getResources().getString(R.string.bind_account));
        btlBindAccount.setShowShareBtn(false);
        btlBindAccount.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
            @Override
            public void OnBackBtnClick() {
                onBackPressed();
            }

            @Override
            public void OnTitleBtnClick() {

            }

            @Override
            public void OnShareBtnClick() {

            }

            @Override
            public void OnSaveBtnClick() {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindAccount(BindAccountEvent bindAccountEvent) {
        initView();
    }

    private void initView() {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null) {
            return;
        }
        boolean bindWx = userInfo.data.userInfo.bindWx;
        if (bindWx) {
            tvBindWeixin.setVisibility(View.VISIBLE);
        } else {
            tvBindWeixin.setVisibility(View.INVISIBLE);
        }
        String phone = userInfo.data.userInfo.phone;
        if (phone.length() != 11) return;
        phone = phone.substring(0, 3) + "****" + phone.substring(7);
        tvPhoneNumber.setText(phone);
        tvPhoneNumber.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.rl_bind_phone, R.id.rl_bind_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_bind_phone://跳转绑定手机界面
                ARouter.getInstance().build("/test/BindPhoneActivity").navigation();
                break;
            case R.id.rl_bind_wx://直接发起绑定微信的请求
                bindWeiXin();
                break;
        }
    }

    /**
     *
     */
    private void bindWeiXin() {
        //0.先判断用户信息里面有没有电话
        //1.拉起微信的授权
        //2.发起请求
        if (AppSetting.getUserInfo() == null) {
            return;
        }
        SocializeApp.get(this).getPlatformInfo(this, SocializePlatform.WEIXIN, new AuthListener() {
            @Override
            public void onStart(SocializePlatform platform) {
            }

            @Override
            public void onComplete(SocializePlatform platform, int code, Map<String, String> data) {
                thirdAccountBind(platform, data);
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
     * 三方授权成功  发起绑定
     *
     * @param plf  目前不需要判断平台,只有微信
     * @param data
     */
    private void thirdAccountBind(SocializePlatform plf, Map<String, String> data) {
        String openid = data.get("openid");
        String unionid = data.get("unionid");
        String nickName = data.get("name");
        String phone = AppSetting.getUserInfo().data.userInfo.phone;
        String platform = "0";

        UpdateUserRepo.bindWx(openid, unionid, nickName, phone, platform)
                .compose(this.<UserBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    public void onDataSuccess(UserBean userBean) {
                        if (userBean.data.result == 0) {
                            Toaster.showShort("绑定成功");
                            userBean.data.userInfo.bindWx = true;
                            AppSetting.saveUserInfo(userBean);
                            tvBindWeixin.setVisibility(View.VISIBLE);
                        } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocializeApp.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
