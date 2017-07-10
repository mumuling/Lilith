package com.youloft.lilith.info.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.info.bean.LogoutBean;
import com.youloft.lilith.info.event.LogoutEvent;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.ui.TabManager;
import com.youloft.lilith.ui.view.BaseToolBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置界面
 * <p>
 * Created by GYH on 2017/6/30.
 */
@Route(path = "/test/SettingActivity")
public class SettingActivity extends BaseActivity {
    @BindView(R.id.btl_setting)
    BaseToolBar btlSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        btlSetting.setTitle("设置");
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
                ARouter.getInstance().build("/test/ModifyPasswordActivity").navigation();
                break;
            case R.id.rl_bind_account://绑定账号
                ARouter.getInstance().build("/test/BindAccountActivity").navigation();
                break;
            case R.id.rl_check_update://检查更新
                Toast.makeText(this, "检查更新", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_feedback://意见反馈
                ARouter.getInstance().build("/test/FeedBackActivity").navigation();
                break;
            case R.id.rl_about_me://关于我们
                ARouter.getInstance().build("/test/AboutMeActivity").navigation();
                break;
            case R.id.tv_logout://退出登录
                logoutUser();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logoutUser() {
        String uid = String.valueOf(AppSetting.getUserInfo().data.userInfo.id);
        String accessToken = AppSetting.getUserInfo().data.userInfo.accessToken;
        UpdateUserRepo.logoutUser(uid,accessToken)
                .compose(this.<LogoutBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<LogoutBean>() {
                    @Override
                    public void onDataSuccess(LogoutBean logoutBean) {
                        String data = logoutBean.data;
                        if(data.equals("true")){
                            //1.把tab设置到首页
                            //2.发出事件
                            //3.把存好的user信息情况  把登录状态设置为false
                            //4.关闭当前页面
                            //通知大家登出的事件
                            EventBus.getDefault().post(new LogoutEvent());
                            //tab设置到首页的事件
                            EventBus.getDefault().post(new TabChangeEvent(TabManager.TAB_INDEX_XZ));
                            AppSetting.saveUserInfo(new UserBean());
                            PointCache.getIns(SettingActivity.this).deleteTable();
                            TopicLikeCache.getIns(SettingActivity.this).deleteTable();
                            PointAnswerCache.getIns(SettingActivity.this).deleteTable();
                            finish();
                        }else {
                            Toaster.showShort("退出登录失败");
                        }
                    }
                });
    }
}
