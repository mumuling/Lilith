package com.youloft.lilith.info.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.LoginUtils;
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
    TextView tvPhoneNumber;        //绑定手机后面的显示的手机号码
    @BindView(R.id.tv_bind_weixin)
    TextView tvBindWeixin;   //微信后面的  已绑定文字
    @BindView(R.id.ll_third_bind_container)
    LinearLayout llThirdBindContainer;  //三方绑定的最外层容器


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_accont);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initTilte();
        //进来的时候看看用户是否有手机号码,有就显示绑定的手机  确认用户是不是微信登录,如果是就隐藏绑定微信
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

    //绑定手机成功后 发过来的事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindAccount(BindAccountEvent bindAccountEvent) {
        initView();
    }

    private void initView() {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null) {
            return;
        }
        //这个是对手机号码的显示
        String phone = userInfo.data.userInfo.phone;
        if(!TextUtils.isEmpty(phone)){
            if (phone.length() != 11) return;
            phone = phone.substring(0, 3) + "****" + phone.substring(7);
            tvPhoneNumber.setText(phone);
            tvPhoneNumber.setVisibility(View.VISIBLE);
        }

        //判断是不是微信三方登录的,如果是 直接给他隐藏了  后面的绑定微信是否显示就不管了
        if (userInfo.data.userInfo.thirdLogin) {
            llThirdBindContainer.setVisibility(View.INVISIBLE);
            return;
        } else { // 不是就显示出来,继续执行下面的逻辑
            llThirdBindContainer.setVisibility(View.VISIBLE);
        }

        //这个是判断是不是绑定了微信
        boolean bindWx = userInfo.data.userInfo.bindWx;
        if (bindWx) {
            tvBindWeixin.setVisibility(View.VISIBLE);
        } else {
            tvBindWeixin.setVisibility(View.INVISIBLE);
        }
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
        //先校验微信是否已经安装
        if (!LoginUtils.isWxInstall(this)) {
            Toaster.showShort("请先安装微信");
            return;
        }


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
        String id = String.valueOf(AppSetting.getUserInfo().data.userInfo.id);
        String platform = "0";

        UpdateUserRepo.bindWx(openid, unionid, nickName, id, platform)
                .compose(this.<UserBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    public void onDataSuccess(UserBean userBean) {
                        if (userBean == null){
                            Toaster.showShort("网络异常");
                            return;
                        }
                        if (userBean.data.result == 0) {
                            Toaster.showShort("绑定成功");
                            userBean.data.userInfo.bindWx = true;
                            AppSetting.saveUserInfo(userBean);
                            tvBindWeixin.setVisibility(View.VISIBLE);
                        } else {
                            Toaster.showShort(userBean.data.message+"");
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("网络异常");
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
