package com.youloft.lilith.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 绑定账号界面
 * <p>
 * Created by GYH on 2017/6/30.
 */

public class BindAccountActivity extends BaseActivity {
    @BindView(R.id.btl_bind_account)
    BaseToolBar btlBindAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_accont);
        ButterKnife.bind(this);
        btlBindAccount.setTitle("绑定账号");
    }
}
