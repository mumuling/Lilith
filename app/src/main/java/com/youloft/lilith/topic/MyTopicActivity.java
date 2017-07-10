package com.youloft.lilith.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.adapter.MyTopicAdapter;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
import com.youloft.lilith.topic.bean.MyTopicBean;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.PointTable;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private LinearLayoutManager mLayoutManager;
    private MyTopicAdapter adapter;
    private UserBean.DataBean.UserInfoBean userInfo;
    private ArrayList<MyTopicBean.DataBean> myTopicList = new ArrayList<>();
    private PointCache pointCache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_topic);
        ButterKnife.bind(this);
        if (AppConfig.LOGIN_STATUS) {
            userInfo = AppSetting.getUserInfo().data.userInfo;
        } else {
            userInfo = null;
        }
        pointCache = PointCache.getIns(this);
        initView();
        requestMyTopicFirst();
    }

    private void requestMyTopicFirst() {
        if (userInfo == null)return;
        TopicRepo.getMyTopic(String.valueOf(userInfo.id),null,"10",true)
                .compose(this.<MyTopicBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<MyTopicBean>() {
                    @Override
                    public void onDataSuccess(MyTopicBean myTopicBean) {
                        if (myTopicBean.data != null && myTopicBean.data.size() != 0) {
                            readDb(myTopicBean.data);//
                            adapter.setMyTopicList(myTopicList);
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     *
     *  查找数据库加载缓存
     * @param data
     */
    private void readDb(ArrayList<MyTopicBean.DataBean> data) {
        ArrayList<PointTable> pointTables =pointCache.getAllTablePoint();
        if (pointTables == null || pointTables.size() == 0)return;
        ArrayList<MyTopicBean.DataBean> list = new ArrayList<>();
        list.addAll(data);
        Iterator iter= list.iterator();
        while (iter.hasNext()) {
            if (iter.next() instanceof MyTopicBean.DataBean) {
                int pid = ((MyTopicBean.DataBean) iter.next()).id;
                if (pointCache.getPointByPid(pid) != null) {
                    pointCache.deletaDataByPid(pid);
                    list.remove(iter.next());
                }
            }
        }
        for (int i  = 0; i < pointTables.size(); i ++) {
            PointTable pointTable = pointTables.get(i);
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
        myTopicList.addAll(list);
    }

    private void initView() {
        toolBar.setTitle("星座话题");
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMyTopic.setLayoutManager(mLayoutManager);
        adapter = new MyTopicAdapter(this);
        rvMyTopic.setAdapter(adapter);
    }
}
