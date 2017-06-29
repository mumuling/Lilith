package com.youloft.lilith.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.ViewUtil;

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

public class PointDetailActivity extends BaseActivity {

    @BindView(R.id.ll_top_root)
    LinearLayout llTopRoot;

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

    }


}
