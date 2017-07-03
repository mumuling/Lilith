package com.youloft.lilith.topic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.topic.widget.ScrollFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 参与者观点的详情页
 * version
 *
 * @author slj
 * @time 2017/6/29 14:55
 * @class PointDetailActivity
 */

public class PointDetailActivity extends BaseActivity implements ScrollFrameLayout.IscrollChange{

    @BindView(R.id.ll_top_root)
    LinearLayout llTopRoot;
    @BindView(R.id.close_icon)
    ImageView closeIcon;
    @BindView(R.id.command_num)
    TextView commandNum;
    @BindView(R.id.root)
    ScrollFrameLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        int statusHeight = ViewUtil.getStatusHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llTopRoot.getLayoutParams();
        params.topMargin = statusHeight;
        llTopRoot.setLayoutParams(params);
        root.setInterface(this);

    }


    @Override
    public void goFinish() {
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void recover() {
        root.setBackgroundColor(Color.parseColor("#4D000000"));
    }

    @Override
    public void move() {
        root.setBackgroundColor(Color.parseColor("#00000000"));
    }
}
