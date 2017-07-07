package com.youloft.lilith.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.info.UserRepo;
import com.youloft.lilith.share.CustomShareActivity;
import com.youloft.lilith.share.ShareBuilder;
import com.youloft.lilith.ui.view.NavBarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    @Autowired(name = "/repo/user")
    UserRepo mUserRepo;

    @Autowired(name = "/repo/cons")
    ConsRepo mConsRepo;

    private static final String TAG = "MainActivity";
    @BindView(R.id.main_content)
    FrameLayout mContent;
    @BindView(R.id.main_nav_bar)
    NavBarLayout mNavBar;
    @BindView(R.id.share)
    Button mBtn;

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
        //更新配置项
        OnlineConfigAgent.getInstance().onAppStart(getApplicationContext());
        mMainTabManager = new TabManager(this);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(TabChangeEvent event) {
        int selectTab = event.selectTab;
        if (mMainTabManager != null) {
            mMainTabManager.selectChange(selectTab);
        }
    }

    @OnClick(R.id.share)
    public void share(){
        new ShareBuilder(this)
                .withTitle("title")
                .withUrl("https://www.baidu.com")
                .withImg(null)
                .withContent("内容")
                .share();
    }

}
