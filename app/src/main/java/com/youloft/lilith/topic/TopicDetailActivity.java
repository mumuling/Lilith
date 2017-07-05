package com.youloft.lilith.topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
import com.youloft.lilith.topic.widget.VoteDialog;
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
@Route(path = "/test/TopicDetailActivity")
public class TopicDetailActivity extends BaseActivity {


    @BindView(R.id.tool_bar)
    BaseToolBar toolBar;
    @BindView(R.id.rv_topic_detail)
    RecyclerView rvTopicDetail;
    private LinearLayoutManager mLayoutManager;
    private TopicDetailAdapter adapter;
    private VoteDialog voteDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolBar.setTitle("星座话题");
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTopicDetail.setLayoutManager(mLayoutManager);
        adapter = new TopicDetailAdapter(this);
        rvTopicDetail.setAdapter(adapter);
        voteDialog = new VoteDialog(this);
        toolBar.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
            @Override
            public void OnBackBtnClick() {
                finish();
            }

            @Override
            public void OnTitleBtnClick() {
                voteDialog.show();
            }

            @Override
            public void OnShareBtnClick() {

            }
        });
    }
}
