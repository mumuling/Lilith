package com.youloft.lilith.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.Toaster;
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

    int count = 0;
    Long time = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);
        btlAboutMe.setShowShareBtn(false);
        btlAboutMe.setTitle(getResources().getString(R.string.about_me));
        btlAboutMe.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
            @Override
            public void OnBackBtnClick() {
                onBackPressed();
            }

            @Override
            public void OnTitleBtnClick() {
                Long time2 = System.currentTimeMillis();
                if (time == null || (time2 - time < 400)) {
                    time = time2;
                    count++;
                } else {
                    time = time2;
                    count = 1;
                }
                if (count == 5) {
                    Toaster.showShort("Install Channel:" + AppConfig.CHANNEL + " Report Channel:" + AppConfig.CHANNEL);
                    count = 0;
                }
            }

            @Override
            public void OnShareBtnClick() {

            }

            @Override
            public void OnSaveBtnClick() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
