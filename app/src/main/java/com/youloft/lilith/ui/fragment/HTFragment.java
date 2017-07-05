package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.topic.TopicDetailActivity;
import com.youloft.lilith.topic.adapter.TopicAdapter;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.ui.WebActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.schedulers.RxThreadFactory;

/**
 * Created by zchao on 2017/6/27.
 * desc: 话题fragment，只是个样子。可自己修改
 * version:
 */

public class HTFragment extends BaseFragment{
    private BaseToolBar mToolBar;
    private RecyclerView mTopicRv;
    private TopicAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<TopicBean> topicBeanList = new ArrayList<>();
    public HTFragment() {
        super(R.layout.fragment_ht);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        mToolBar = (BaseToolBar) getView().findViewById(R.id.tool_bar);
        mTopicRv = (RecyclerView) getView().findViewById(R.id.lv_topic);
        mToolBar.setTitle("星座话题");
        //设置recycleView
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTopicRv.setLayoutManager(mLayoutManager);
        topicBeanList.addAll(TopicBean.getTopicListDefault());
        mAdapter = new TopicAdapter(getContext());
        mAdapter.setData(topicBeanList);
        mTopicRv.setAdapter(mAdapter);
    }


}
