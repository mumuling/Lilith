package com.youloft.lilith.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.event.ConsChangeEvent;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.NetUtil;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.info.bean.CheckLoginBean;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.view.NavBarLayout;
import com.youloft.lilith.ui.view.NetErrDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Autowired(name = "/repo/cons")
    ConsRepo mConsRepo;
    @BindView(R.id.main_content)
    FrameLayout mContent;
    @BindView(R.id.main_nav_bar)
    NavBarLayout mNavBar;

    @BindView(R.id.main_content_tv)
    TextView tv;
    private TabManager mMainTabManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lilith);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        checkNet();
        //更新配置项
        OnlineConfigAgent.getInstance().onAppStart(getApplicationContext());
        mMainTabManager = new TabManager(this);
        checkLogin();
    }



    /**
     * 检查用户登录状态
     */
    private void checkLogin() {
        final UserBean userInfo = AppSetting.getUserInfo();
        if(userInfo == null || userInfo.data == null || userInfo.data.userInfo == null){
            return;
        }
        if(userInfo.data.userInfo.id == 0){
            return;
        }
        UpdateUserRepo.checkLoginStatus(String.valueOf(userInfo.data.userInfo.id))
                .compose(this.<CheckLoginBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<CheckLoginBean>() {
                    @Override
                    public void onDataSuccess(CheckLoginBean checkLoginBean) {
                        String token = checkLoginBean.data;
                        String accessToken = userInfo.data.userInfo.accessToken;
                        if(token.equals(accessToken)){
                            AppConfig.LOGIN_STATUS = true;//登录状态设置为 登录
                            EventBus.getDefault().post(new LoddingCheckEvent());
                            EventBus.getDefault().post(new LoginEvent(true));
                        } else {
                            AppConfig.LOGIN_STATUS = false;//登录状态置为 未登录
                            EventBus.getDefault().post(new LoginEvent(false));
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 切换tab通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(TabChangeEvent event) {
        int selectTab = event.selectTab;
        if (mMainTabManager != null) {
            mMainTabManager.selectChange(selectTab);
        }
    }

    /**
     * 星座改变通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onConsChagneEvent(ConsChangeEvent event) {
        int consType = event.consType;
        if (mNavBar != null) {
            mNavBar.changConsIcon(consType);
        }
    }

    /**
     * 检查网络
     */
    private void checkNet() {
        if (!NetUtil.isNetOK()) {
            NetErrDialog dialog = new NetErrDialog(this);
            dialog.show();
        }
    }

    /**
     * 处理两次点击返回才退出
     */
    long mFirstBackPress = 0;
    @Override
    public void onBackPressed() {
        long l = System.currentTimeMillis();
        if (l - mFirstBackPress >= 2000) {
            Toaster.showShort(getString(R.string.out_program));
            mFirstBackPress = l;
            return;
        }
        super.onBackPressed();
    }
}
