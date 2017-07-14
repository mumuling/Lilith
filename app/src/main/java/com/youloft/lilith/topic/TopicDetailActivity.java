package com.youloft.lilith.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
import com.youloft.lilith.topic.bean.AnswerEvent;
import com.youloft.lilith.topic.bean.ClickLikeEvent;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointAnswerTable;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.PointTable;
import com.youloft.lilith.topic.db.TopicInfoCache;
import com.youloft.lilith.topic.db.TopicTable;
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.ui.view.BaseToolBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    public int totalPoint = 0;
    public int totalTopic = 0;
    private boolean isMorePoint = true;
    private int isVote = 0;//是否参与过
    private TopicInfoCache topicInfoCache;
    private PointCache pointCache;
    private boolean needLoadMoreTopic = true;//是否需要加载更多话题
    private boolean needAddAnswer = true;//是否需要加缓存回复的数量
    @Autowired
    public int tid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        topicInfoCache = TopicInfoCache.getIns(this);
        pointCache = PointCache.getIns(this);
        initView();
        requestTopicDetail(true);//请求话题的详细信息
        requestPointList();//请求观点列表
        requestOtherTopicList();//请求底部的更多话题
    }

    /**
     * 第一次请求其他话题列表
     */
    private void requestOtherTopicList() {
        TopicRepo.getTopicListBottom("5", null, true)
                .compose(this.<TopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicBean>() {
                    @Override
                    public void onDataSuccess(TopicBean topicBean) {
                        if (topicBean.data != null) {
                            chekTopic(topicBean.data);
                            otherTopicList.addAll(topicBean.data);
                            adapter.setOtherTopicList(topicBean.data);
                            totalTopic = topicBean.data.size();
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     * 第一次请求观点列表
     */
    private void requestPointList() {
        int userId = 0;
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean != null) {
            userId = userBean.data.userInfo.id;
        } else {
            userId = 0;
        }
        TopicRepo.getPointList(String.valueOf(tid), String.valueOf(userId), "10", null, true)
                .compose(this.<PointBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<PointBean>() {
                    @Override
                    public void onDataSuccess(PointBean pointBean) {
                        if (pointBean.data == null) return;
                        handlePointTableInfo(pointBean);
                        pointList.addAll(pointBean.data);
                        handleAnswerTable(pointList,pointBean.t);
                        adapter.setPointBeanList(pointList);
                        totalPoint = pointBean.data.size();
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     *     加上本地缓存的回复数量
     * @param pointList   获得的观点列表
     */
    private void handleAnswerTable(List<PointBean.DataBean> pointList,long time) {
        if (pointList == null )return;
        if (AppSetting.getUserInfo() == null)return;
        PointAnswerCache pointAnswerCache = PointAnswerCache.getIns(this);
        List<PointAnswerTable> pointAnswerTableList = null;
        for (int i = 0 ; i < pointList.size(); i ++) {
            int poitnID = pointList.get(i).id;
            int replyCount = pointList.get(i).reply;
            pointAnswerTableList = pointAnswerCache.getAnswerListByCode(poitnID);
            if (pointAnswerTableList == null){
                continue;
            }
            for (int j = 0; j < pointAnswerTableList.size(); j ++) {
                if (pointAnswerTableList.get(j).time > time) {
                    replyCount = replyCount + 1;
                    PointBean.DataBean.ReplyListBean replyBean = new PointBean.DataBean.ReplyListBean();
                    replyBean.nickName = AppSetting.getUserInfo().data.userInfo.nickName;
                    replyBean.contents = pointAnswerTableList.get(j).viewPoint;
                    pointList.get(i).replyList.add(0,replyBean);
                }
            }
            pointList.get(i).reply = replyCount;
        }
    }

    /**
     * 处理观点信息的数据库。
     *
     * @param pointBeen
     */
    private void handlePointTableInfo(PointBean pointBeen) {
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean == null) return;
        UserBean.DataBean.UserInfoBean userInfo = userBean.data.userInfo;
        int userID = userInfo.id;
        PointTable pointTable = pointCache.getInforByCode(tid);
        if (pointTable != null && pointTable.time > pointBeen.t) {
            PointBean.DataBean dataBean = new PointBean.DataBean();
            dataBean.userId = userInfo.id;
            dataBean.isclick = 0;
            dataBean.zan = 0;
            dataBean.buildDate = pointTable.buildDate;
            dataBean.nickName = userInfo.nickName;
            dataBean.reply = 0;
            dataBean.headImg = userInfo.headImg;
            dataBean.replyList = new ArrayList<>();
            dataBean.sex = userInfo.sex;
            dataBean.signs = userInfo.signs;
            dataBean.topicOptionId = pointTable.oid;
            dataBean.topicId = pointTable.tid;
            dataBean.viewpoint = pointTable.viewPoint;
            dataBean.id = pointTable.pid;
            pointList.add(dataBean);
        } else {
            pointCache.deleteData(tid);
        }

    }

    /**
     * 请求话题详细信息
     */
    private void requestTopicDetail(boolean needCache) {
        int userId = -1;
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean != null) {
            userId = userBean.data.userInfo.id;
        }

        TopicRepo.getTopicDetail(String.valueOf(tid), userId == -1 ? null : String.valueOf(userId),needCache)
                .compose(this.<TopicDetailBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicDetailBean>() {
                    @Override
                    public void onDataSuccess(TopicDetailBean topicDetailBean) {
                        TopicDetailBean.DataBean data = topicDetailBean.data;
                        if (data == null || data.option == null || data.option.size() == 0) return;
                        handleTopicInfoTable(data);
                        topicDtailInfo = data;
                        adapter.setTopicInfo(topicDtailInfo);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    private void handleTopicInfoTable(TopicDetailBean.DataBean data) {
        TopicTable topicTable = topicInfoCache.getInforByCode(data.id);
        if (topicTable != null) {
            isVote = topicTable.mIsVote;
            if (isVote == data.isVote) {
                topicInfoCache.deleteData(data.id);
            } else {
                data.isVote = isVote;
                data.totalVote++;
                for (int i = 0; i < data.option.size(); i++) {
                    if (topicTable.vote_id == data.option.get(i).id) {
                        data.option.get(i).vote++;
                    }
                }
            }
        }

    }

    /**
     * 登录状态改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoddingChagne(LoginEvent event) {
        requestTopicDetail(false);

    }

    /**
     * 点赞状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLikeChagne(ClickLikeEvent event) {
        if (event.type != ClickLikeEvent.TYPE_AUTHOR || adapter == null) return;
        adapter.notifyDataSetChanged();

    }

    /**
     * 回复数量
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onCountChagne(AnswerEvent event) {
        if (event.position <= 0)return;
        adapter.pointBeanList.get(event.position -1).reply = event.count;
        PointBean.DataBean.ReplyListBean replyBean = new PointBean.DataBean.ReplyListBean();
        replyBean.nickName = event.nickNmae;
        replyBean.contents = event.content;
        adapter.pointBeanList.get(event.position -1).replyList.add(0,replyBean);
        adapter.notifyItemChanged(event.position);
    }

    private void initView() {
        toolBar.setTitle("星座话题");
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTopicDetail.setLayoutManager(mLayoutManager);
        adapter = new TopicDetailAdapter(this);
        rvTopicDetail.setAdapter(adapter);
        voteDialog = new VoteDialog(this, R.style.VoteDialog);
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

            @Override
            public void OnSaveBtnClick() {

            }
        });


//滑动到底部的监听
        rvTopicDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && totalItemCount >= 2
                        && lastVisibleItemPosition >= totalItemCount - 2
                        && visibleItemCount > 0) {
                    if (otherTopicList != null && otherTopicList.size() >= 4 && needLoadMoreTopic) {
                        loadMoreTopic();
                    }
                }
            }
        });
    }

    /**
     * 加载更多其他话题
     */
    public void loadMoreTopic() {
        TopicRepo.getTopicListBottom("5", String.valueOf(totalTopic), false)
                .compose(this.<TopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TopicBean>() {
                    @Override
                    public void onDataSuccess(TopicBean topicBean) {
                        if (topicBean.data != null) {
                            if (topicBean.data.size() == 0) needLoadMoreTopic = false;
                            otherTopicList.clear();
                            //otherTopicList.addAll(topicBean.data);
                            chekTopic(topicBean.data);
                            adapter.setOtherTopicList(topicBean.data);
                            totalTopic = totalTopic + topicBean.data.size();
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     * 检查话题是否重复
     *
     * @param data
     */
    private void chekTopic(List<TopicBean.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id == tid) {
                data.remove(i);
                return;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}