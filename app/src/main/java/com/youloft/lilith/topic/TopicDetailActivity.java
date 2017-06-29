package com.youloft.lilith.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.GlideRequest;
import com.youloft.lilith.common.GlideRequests;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.ui.GlideCircleTransform;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 星座话题的详情页
 * version
 *
 * @author slj
 * @time 2017/6/29 14:10
 * @class TopicDetailActivity
 */

public class TopicDetailActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    BaseToolBar toolBar;
    @BindView(R.id.circle_image)
    ImageView circleImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolBar.setTitle("星座话题");
        GlideApp.with(this).asBitmap()
                .load(R.mipmap.er)
                .transform(new GlideCircleTransform(this))
                .into(circleImage);
        toolBar.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
            @Override
            public void OnBackBtnClick() {
                finish();
            }

            @Override
            public void OnTitleBtnClick() {

            }

            @Override
            public void OnShareBtnClick() {

            }
        });
    }
}
