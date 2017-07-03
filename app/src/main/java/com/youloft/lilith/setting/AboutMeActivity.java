package com.youloft.lilith.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于我们
 * <p>
 * Created by GYH on 2017/6/30.
 */
@Route(path = "/test/AboutMeActivity")
public class AboutMeActivity extends BaseActivity {
    @BindView(R.id.btl_about_me)
    BaseToolBar btlAboutMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);
        btlAboutMe.setTitle("关于我们");
    }
}
