package com.youloft.lilith.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class TopicDetailActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    BaseToolBar toolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolBar.setTitle("星座话题");
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
