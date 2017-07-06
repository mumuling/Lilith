package com.youloft.lilith.topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.ui.GlideCircleTransform;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    private TopicDetailBean.DataBean topicDtailInfo;
    private List<PointBean.DataBean> pointList = new ArrayList<>();
    private List<TopicBean.DataBean> otherTopicList = new ArrayList<>();
    @Autowired
    public int tid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        initView();
        requestTopicDetail();
        requestPointList();
        requestOtherTopicList();
    }

    private void requestOtherTopicList() {
        TopicRepo.getTopicListBottom(null,true)
                .compose(this.<TopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicBean>() {
                    @Override
                    public void onDataSuccess(TopicBean topicBean) {
                        if (topicBean.data != null) {
                            otherTopicList.addAll(topicBean.data);
                            adapter.setOtherTopicList(topicBean.data);
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    private void requestPointList() {
        TopicRepo.getPointList(String.valueOf(tid),null,"10",null,true)
                .compose(this.<PointBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<PointBean>() {
                    @Override
                    public void onDataSuccess(PointBean pointBean) {
                        if (pointBean.data == null)return;
                        pointList.addAll(pointBean.data);
                        adapter.setPointBeanList(pointList);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    private void requestTopicDetail() {
        TopicRepo.getTopicDetail(String.valueOf(tid))
                .compose(this.<TopicDetailBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicDetailBean>() {
                    @Override
                    public void onDataSuccess(TopicDetailBean topicDetailBean) {
                        TopicDetailBean.DataBean data = topicDetailBean.data;
                        if (data == null )return;
                        topicDtailInfo= data;
                        adapter.setTopicInfo(topicDtailInfo);

                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
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
