package com.youloft.lilith.info.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.widgets.dialog.CheckVersionCodeDialog;
import com.youloft.lilith.common.widgets.dialog.DownloadSelectDialog;
import com.youloft.lilith.info.bean.CheckVersionBean;
import com.youloft.lilith.info.bean.LogoutBean;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.activity.ForgetPasswordActivity;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.login.event.ModifyPasswordEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.TopicInfoCache;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.ui.TabManager;
import com.youloft.lilith.ui.view.BaseToolBar;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置界面
 * <p>
 * Created by GYH on 2017/6/30.
 */
@Route(path = "/test/SettingActivity")
public class SettingActivity extends BaseActivity {
    private static final String TAG = "SettingActivity";
    @BindView(R.id.btl_setting)
    BaseToolBar btlSetting;
    @BindView(R.id.rl_modify_password)
    RelativeLayout rlModifyPassword;  //修改密码条目
    @BindView(R.id.v_devider)
    View vDevider;  //修改密码下面的线

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initTitle();
        initView();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModifyPassword(ModifyPasswordEvent modifyPasswordEvent){
        finish();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        //判断是不是三方登录
        if (AppSetting.getUserInfo() != null) {
            boolean thirdLogin = AppSetting.getUserInfo().data.userInfo.thirdLogin;
            if (thirdLogin) {
                rlModifyPassword.setVisibility(View.GONE);
                vDevider.setVisibility(View.GONE);
            } else {
                rlModifyPassword.setVisibility(View.VISIBLE);
                vDevider.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 对Title进行初始化
     */
    private void initTitle() {
        btlSetting.setTitle(getResources().getString(R.string.setting));
        btlSetting.setShowShareBtn(false);
        btlSetting.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
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


    @OnClick({R.id.rl_modify_password, R.id.rl_bind_account, R.id.rl_check_update, R.id.rl_feedback, R.id.rl_about_me, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_modify_password://修改密码
                AppAnalytics.onEvent("Set.modifypassword.C");
                ARouter.getInstance()
                        .build("/test/ForgetPasswordActivity")
                        .withString("flag", ForgetPasswordActivity.MODIFY_PASSWORD_FLAG)
                        .navigation();
                break;
            case R.id.rl_bind_account://绑定账号
                AppAnalytics.onEvent("Set.binding.C");
                ARouter.getInstance().build("/test/BindAccountActivity").navigation();
                break;
            case R.id.rl_check_update://检查更新
                AppAnalytics.onEvent("Set.update.C");
                checkVersionCode();

                break;
            case R.id.rl_feedback://意见反馈
                AppAnalytics.onEvent("Set.feedback.C");
                ARouter.getInstance().build("/test/FeedBackActivity").navigation();
                break;
            case R.id.rl_about_me://关于我们
                AppAnalytics.onEvent("Set.about.C");
                ARouter.getInstance().build("/test/AboutMeActivity").navigation();
                break;
            case R.id.tv_logout://退出登录
                AppAnalytics.onEvent("Set.signout.C");
                logoutUser();
                break;
        }
    }

    /**
     * 检查版本
     */
    private void checkVersionCode() {
        //如果正在下载,就不去检查更新
        if (AppConfig.DOWNLOAD_STATUS) {
            Toaster.showShort("新版本下载中...");
            return;
        }
        final CheckVersionCodeDialog mVersionCodeDialog = new CheckVersionCodeDialog(this);
        mVersionCodeDialog.show();
        UpdateUserRepo.checkVersion()
                .compose(this.<CheckVersionBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<CheckVersionBean>() {
                    @Override
                    public void onDataSuccess(CheckVersionBean checkVersionBean) {
                        if (checkVersionBean == null || checkVersionBean.data == null
                                || TextUtils.isEmpty(checkVersionBean.data.version) ||
                                TextUtils.isEmpty(checkVersionBean.data.contents)
                                || TextUtils.isEmpty(checkVersionBean.data.downPath)) {
                            //防止运营瞎填
                            Toaster.showShort("您当前版本为最新版本");
                            mVersionCodeDialog.dismiss();
                            return;
                        }
                        String version = checkVersionBean.data.version;
                        if (version.equals(AppSetting.getVersionCode())) {
                            Toaster.showShort("您当前版本为最新版本");
                            mVersionCodeDialog.dismiss();
                        } else {
                            mVersionCodeDialog.dismiss();
                            //弹出对话框,让用户选择是否下载
                            DownloadSelectDialog downloadSelectDialog = new DownloadSelectDialog(SettingActivity.this, checkVersionBean);
                            downloadSelectDialog.show();
                        }

                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        mVersionCodeDialog.dismiss();
                        Toaster.showShort("网络错误");
                    }

                });

    }

    /**
     * 退出登录
     */
    private void logoutUser() {
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean == null) {
            return;
        }
        String uid = String.valueOf(userBean.data.userInfo.id);
        String accessToken = userBean.data.userInfo.accessToken;
        UpdateUserRepo.logoutUser(uid, accessToken)
                .compose(this.<LogoutBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<LogoutBean>() {
                    @Override
                    public void onDataSuccess(LogoutBean logoutBean) {
                        String data = logoutBean.data;
                        if (data.equals("true")) {
                            //1.把tab设置到首页
                            //2.发出事件
                            //3.把存好的user信息情况  把登录状态设置为false
                            //4.关闭当前页面
                            //通知大家登出的事件
                            //tab设置到首页的事件
                            AppSetting.clearUserInfo();
                            PointCache.getIns(SettingActivity.this).deleteTable();
                            TopicLikeCache.getIns(SettingActivity.this).deleteTable();
                            PointAnswerCache.getIns(SettingActivity.this).deleteTable();
                            TopicInfoCache.getIns(SettingActivity.this).deleteTable();
                            EventBus.getDefault().post(new LoginEvent(false));
                            EventBus.getDefault().post(new TabChangeEvent(TabManager.TAB_INDEX_XZ));
                            Toaster.showShort("退出成功");
                            finish();
                        } else {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
