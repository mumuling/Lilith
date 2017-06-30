package com.youloft.lilith.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.router.AppRouter;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 * <p>
 * Created by GYH on 2017/6/30.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.btl_setting)
    BaseToolBar btlSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        btlSetting.setTitle("设置");
    }


    @OnClick({R.id.rl_modify_password, R.id.rl_bind_account, R.id.rl_check_update, R.id.rl_feedback, R.id.rl_about_me, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_modify_password://修改密码
                Toast.makeText(this, "修改密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_bind_account://绑定账号
                startActivity(new Intent(this,BindAccountActivity.class));
                break;
            case R.id.rl_check_update://检查更新
                Toast.makeText(this, "检查更新", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_feedback://意见反馈
                startActivity(new Intent(this,FeedBackActivity.class));
                break;
            case R.id.rl_about_me://关于我们
                startActivity(new Intent(this,AboutMeActivity.class));
                break;
            case R.id.tv_logout://退出登录
                Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
