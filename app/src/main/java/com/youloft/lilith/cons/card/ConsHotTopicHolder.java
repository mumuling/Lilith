package com.youloft.lilith.cons.card;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.TopicAdapter;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.ui.TabManager;

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

    public ConsHotTopicHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_hot_topic);
        ButterKnife.bind(this, itemView);

        init();
        TopicRepo.getTopicList("1", "0","10",true)
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

    private void init() {
        mConsHotTopicList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        adapter = new TopicAdapter(mContext, true);
        mConsHotTopicList.setAdapter(adapter);
    }

    @OnClick(R.id.cons_hot_topic_card_more)
    public void showMore() {
        EventBus.getDefault().post(new TabChangeEvent(TabManager.TAB_INDEX_HT));
    }

    @Override
    public void bindData(Object o) {
        super.bindData(o);
    }
}
