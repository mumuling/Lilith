package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.topic.TopicDetailActivity;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.TopicAdapter;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.ui.WebActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.schedulers.Schedulers;

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
    private List<TopicBean.DataBean> topicBeanList = new ArrayList<>();
    public HTFragment() {
        super(R.layout.fragment_ht);
    }
    @Autowired(name = "/repo/topic")
    TopicRepo mTopicRepo;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        requestTopic();

    }

    private void requestTopic() {
        TopicRepo.getTopicList("0","10")
                .compose(this.<TopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicBean>() {
                    @Override
                    public void onDataSuccess(TopicBean topicBean) {
                        if (topicBean.data == null) return;
                        topicBeanList.addAll(topicBean.data);
                        mAdapter.setData(topicBean.data);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    private void initView() {
        mToolBar = (BaseToolBar) getView().findViewById(R.id.tool_bar);
        mTopicRv = (RecyclerView) getView().findViewById(R.id.lv_topic);
        mToolBar.setTitle("星座话题");
        //设置recycleView
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTopicRv.setLayoutManager(mLayoutManager);
        mAdapter = new TopicAdapter(getContext());
        mTopicRv.setAdapter(mAdapter);
    }


}
