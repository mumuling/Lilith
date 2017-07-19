package com.youloft.lilith.cons.card;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.youloft.lilith.R;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.TopicAdapter;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.ui.TabManager;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zchao on 2017/7/5.
 * desc: 热门话题推荐
 * version:
 */

public class ConsHotTopicHolder extends CardHolder {
    @BindView(R.id.cons_hot_topic_card_title)
    TextView mConsHotTopicCardTitle;
    @BindView(R.id.cons_hot_topic_card_more)
    TextView mConsHotTopicCardMore;
    @BindView(R.id.cons_hot_topic_list)
    RecyclerView mConsHotTopicList;
    private TopicAdapter adapter;
    private boolean isReport = false;

    public ConsHotTopicHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_hot_topic);
        ButterKnife.bind(this, itemView);

        init();

    }

    private void init() {
        mConsHotTopicList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        adapter = new TopicAdapter(mContext, true);
        mConsHotTopicList.setAdapter(adapter);
        //产品说每左滑一次都上报一次，所以。。
        mConsHotTopicList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0  && !isReport) {
                    isReport = true;
                    AppAnalytics.onEvent("Slideview.C");
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isReport = false;
                }
            }
        });
    }

    @OnClick(R.id.cons_hot_topic_card_more)
    public void showMore() {
        AppAnalytics.onEvent("Hometopicmore.C");
        EventBus.getDefault().post(new TabChangeEvent(TabManager.TAB_INDEX_HT));
    }

    @Override
    public void bindData(Object o) {
        TopicRepo.getTopicList("1", "0", "10", true, 10 * 60 * 1000)
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicBean>() {
                    @Override
                    public void onDataSuccess(TopicBean topicBean) {
                        if (topicBean == null || topicBean.data == null) return;
                        adapter.setData(topicBean.data);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }
}
