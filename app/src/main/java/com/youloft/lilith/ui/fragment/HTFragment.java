package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.NetUtil;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.widgets.view.PullToRefreshLayout;
import com.youloft.lilith.glide.GlideBlurTransform;
import com.youloft.lilith.topic.TopicDetailActivity;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.TopicAdapter;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.ui.WebActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zchao on 2017/6/27.
 * desc: 话题fragment，只是个样子。可自己修改
 * version:
 */

public class HTFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener{
    private BaseToolBar mToolBar;
    private RecyclerView mTopicRv;
    private TopicAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<TopicBean.DataBean> topicBeanList = new ArrayList<>();
    private PullToRefreshLayout llPullRefresh;
    private ImageView imageNoNet;
    private boolean needLoadMoreTopic = true;
    public HTFragment() {
        super(R.layout.fragment_ht);
    }
    @Autowired(name = "/repo/topic")
    TopicRepo mTopicRepo;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        requestTopic(llPullRefresh);

    }

    /**
     *  请求话题信息
     */
    private void requestTopic(final PullToRefreshLayout layout) {
        if (!NetUtil.isNetOK()) {
            mTopicRv.setVisibility(View.GONE);
            imageNoNet.setVisibility(View.VISIBLE);
            if (layout != null)layout.refreshFinish(PullToRefreshLayout.FAIL);
            return;
        } else {
            mTopicRv.setVisibility(View.VISIBLE);
            imageNoNet.setVisibility(View.GONE);
        }
        TopicRepo.getTopicList("0","0","10",true)
                .compose(this.<TopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicBean>() {
                    @Override
                    public void onDataSuccess(TopicBean topicBean) {
                        if (topicBean.data == null){
                            if (layout != null)layout.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }

                        for (TopicBean.DataBean dataBean : topicBean.data) {
                            GlideApp.with(getContext())
                                    .asBitmap()
                                    .load(dataBean.backImg)
                                    .transform(new GlideBlurTransform(getContext()))
                                    .preload(50,50);
                        }
                        topicBeanList.clear();
                        topicBeanList.addAll(topicBean.data);
                        mAdapter.setData(topicBean.data);
                        if (layout != null)layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        if (layout != null)layout.refreshFinish(PullToRefreshLayout.FAIL);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        if (layout != null)layout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                });
    }

    private void initView() {
        if (getView() == null)return;
        mToolBar = (BaseToolBar) getView().findViewById(R.id.tool_bar);
        mTopicRv = (RecyclerView) getView().findViewById(R.id.lv_topic);
        imageNoNet = (ImageView) getView().findViewById(R.id.default_image);
        mToolBar.setTitle("星座话题");
        //设置recycleView
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTopicRv.setLayoutManager(mLayoutManager);
        mAdapter = new TopicAdapter(getContext());
        mTopicRv.setAdapter(mAdapter);
        llPullRefresh = (PullToRefreshLayout) getView().findViewById(R.id.rl_pull_refresh);
        llPullRefresh.setOnRefreshListener(this);
        mTopicRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && totalItemCount >=10
                        && lastVisibleItemPosition >= totalItemCount - 2
                        && visibleItemCount > 0) {
                    if (topicBeanList!= null && topicBeanList.size() >= 8) {
                        loadMoreTopic();
                    }
                }
            }
        });
    }

    private void loadMoreTopic() {
        TopicRepo.getTopicList("0",String.valueOf(topicBeanList.size()),"10",false)
                .compose(this.<TopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicBean>() {
                    @Override
                    public void onDataSuccess(TopicBean topicBean) {
                        if (topicBean.data == null) return;
                        if (topicBean.data.size() == 0){
                            needLoadMoreTopic = false;
                            Toaster.showShort("没有更多话题了");
                            return;
                        }
                        topicBeanList.addAll(topicBean.data);
                        mAdapter.setMoreData(topicBean.data);

                    }

                    @Override
                    protected void onFailed(Throwable e) {

                        super.onFailed(e);
                    }
                });
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        needLoadMoreTopic = true;
        requestTopic(pullToRefreshLayout);
    }
}
