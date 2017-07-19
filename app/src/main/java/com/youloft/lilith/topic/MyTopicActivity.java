package com.youloft.lilith.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.adapter.MyTopicAdapter;
import com.youloft.lilith.topic.bean.AnswerEvent;
import com.youloft.lilith.topic.bean.ClickLikeEvent;
import com.youloft.lilith.topic.bean.MyTopicBean;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointAnswerTable;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.PointTable;
import com.youloft.lilith.ui.TabManager;
import com.youloft.lilith.ui.view.BaseToolBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的话题
 * version
 *
 * @author slj
 * @time 2017/7/10 16:30
 * @class MyTopicActivity
 */
@Route(path = "/test/MyTopicActivity")
public class MyTopicActivity extends BaseActivity {
    @BindView(R.id.rv_my_topic)
    RecyclerView rvMyTopic;
    @BindView(R.id.tool_bar)
    BaseToolBar toolBar;
    @BindView(R.id.text_see_topic)
    TextView textSeeTopic;
    @BindView(R.id.ll_no_topic)
    LinearLayout llNoTopic;
    private LinearLayoutManager mLayoutManager;
    private MyTopicAdapter adapter;
    private UserBean.DataBean.UserInfoBean userInfo;
    private ArrayList<MyTopicBean.DataBean> myTopicList = new ArrayList<>();
    private PointCache pointCache;
    private PointAnswerCache pointAnswerCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_topic);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        toolBar.setShowShareBtn(false);
        toolBar.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
            @Override
            public void OnBackBtnClick() {
                onBackPressed();
            }

            @Override
            public void OnTitleBtnClick() {

            }

            @Override
            public void OnShareBtnClick() {

            }

            @Override
            public void OnSaveBtnClick() {

            }
        });
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean  != null) {
            userInfo = userBean.data.userInfo;
        } else {
            userInfo = null;
        }
        pointCache = PointCache.getIns(this);
        pointAnswerCache = PointAnswerCache.getIns(this);
        initView();
        requestMyTopicFirst();
    }

    /**
     *   请求第一批我的话题的信息
     */
    private void requestMyTopicFirst() {
        if (userInfo == null) return;
        TopicRepo.getMyTopic(String.valueOf(userInfo.id), null, "10", true)
                .compose(this.<MyTopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<MyTopicBean>() {
                    @Override
                    public void onDataSuccess(MyTopicBean myTopicBean) {
                        if (myTopicBean.data != null) {
                            readDb(myTopicBean);
                            myTopicList.addAll(myTopicBean.data);
                            addReplyCount(myTopicList,myTopicBean.t);
                            if (myTopicList.size() == 0) {
                                rvMyTopic.setVisibility(View.GONE);
                                llNoTopic.setVisibility(View.VISIBLE);
                            } else {
                                adapter.setMyTopicList(myTopicList);
                            }
                        } else {
                            rvMyTopic.setVisibility(View.GONE);
                            llNoTopic.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     *   添加本地的回复数量
     * @param myTopicList
     * @param t
     */
    private void addReplyCount(ArrayList<MyTopicBean.DataBean> myTopicList, long t) {
        ArrayList<PointAnswerTable> pointAnswerTables = new ArrayList<>();
        for (int i = 0 ; i < myTopicList.size(); i ++) {
            MyTopicBean.DataBean topic = myTopicList.get(i);
            pointAnswerTables = pointAnswerCache.getAnswerListByTime(topic.id,t);
            if (pointAnswerTables == null || pointAnswerTables.size() == 0){
                return;
            }else  {
                int replyCount  = topic.reply;
                myTopicList.get(i).reply = replyCount + pointAnswerTables.size();
                pointAnswerTables.clear();
            }

        }
    }

    /**
     * 查找数据库加载缓存
     *
     * @param data
     */
    private void readDb(MyTopicBean data) {

        ArrayList<PointTable> pointTables = pointCache.getAllTablePoint();
        if (pointTables == null || pointTables.size() == 0) {
            return;
        } else {
            //如果需要数据库的数据。只有第一次请求需要
            PointTable pointTable = null;
                for(int i = 0; i < pointTables.size(); i++) {
                    pointTable = pointTables.get(i);
                    if (pointTable.time < data.t) {
                       continue; //pointCache.deletaDataByPid(pointTable.pid);
                    } else {
                        MyTopicBean.DataBean topic = new MyTopicBean.DataBean();
                        topic.date = pointTable.buildDate;
                        topic.id = pointTable.pid;
                        topic.optionTitle = pointTable.voteTitle;
                        topic.reply = 0;
                        topic.zan = 0;
                        topic.topicOptionId = pointTable.oid;
                        topic.topicIdTitle = pointTable.topicTitle;
                        topic.Viewpoint = pointTable.viewPoint;
                        topic.topicId = pointTable.tid;
                        topic.isclick = 0;
                        myTopicList.add(topic);
                    }
            }
        }

    }


    private void initView() {
        toolBar.setTitle("我的话题");
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMyTopic.setLayoutManager(mLayoutManager);
        adapter = new MyTopicAdapter(this);
        rvMyTopic.setAdapter(adapter);
        //滑动到底部的监听
        rvMyTopic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
                    if (myTopicList!= null &&  rvMyTopic.getBottom() >= getWindowManager().getDefaultDisplay().getHeight()) {
                        if (myTopicList.size() >=9) {
                            loadMoreTopic();
                        } else {
                            Toaster.showShort("没有更多观点了。");
                        }
                    }
                }
            }
        });
    }

    /**
     * 滑到底部时的加载更多
     */
    private void loadMoreTopic() {
        if (userInfo == null) return;
        TopicRepo.getMyTopic(String.valueOf(userInfo.id), String.valueOf(myTopicList.size()), "10", true)
                .compose(this.<MyTopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<MyTopicBean>() {
                    @Override
                    public void onDataSuccess(MyTopicBean myTopicBean) {
                        if (myTopicBean.data != null && myTopicBean.data.size() != 0) {
                            myTopicList.addAll(myTopicBean.data);
                            addReplyCount(myTopicBean.data,myTopicBean.t);
                            adapter.setMyTopicList(myTopicBean.data);
                        } else {
                            Toaster.showShort("没有更多观点了。");
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("请求失败！");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 点赞改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoddingChagne(AnswerEvent event) {


    }
    /**
     * 回复数改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onReplyChagne(AnswerEvent event) {
        if (event.position < 0)return;
        adapter.myTopicList.get(event.position).reply = event.count;
        adapter.notifyItemChanged(event.position);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 无数据，直接查看话题
     */
    @OnClick(R.id.text_see_topic)
    public void onClick() {
        EventBus.getDefault().post(new TabChangeEvent(TabManager.TAB_INDEX_HT));
        finish();
    }
}
