package com.youloft.lilith.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;

/**
 * 快速登录界面
 *
 * Created by GYH on 2017/6/29.
 */

public class QuickLoginActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login);
    }
}
