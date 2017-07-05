package com.youloft.lilith.topic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.topic.adapter.PointAnswerAdapter;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.widget.ScrollFrameLayout;
import com.youloft.lilith.topic.widget.SoftInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 参与者观点的详情页
 * version
 *
 * @author slj
 * @time 2017/6/29 14:55
 * @class PointDetailActivity
 */
@Route(path = "/test/PointDetailActivity")
public class PointDetailActivity extends BaseActivity implements ScrollFrameLayout.IscrollChange {

    @BindView(R.id.ll_top_root)
    LinearLayout llTopRoot;
    @BindView(R.id.close_icon)
    ImageView closeIcon;
    @BindView(R.id.command_num)
    TextView commandNum;
    @BindView(R.id.root)
    SoftInputLayout root;
    @BindView(R.id.rv_comment_answer)
    ScrollFrameLayout rvCommentAnswer;
    @BindView(R.id.comment_edit)
    EditText commentEdit;
    @Autowired(name = "/repo/topic")
    TopicRepo mTopicRepo;
    private LinearLayoutManager mLayoutManager;
    private PointAnswerAdapter adapter;
    private List<ReplyBean.DataBean> replyBeanList = new ArrayList<>();//回复的列表
    public static String TYPE_POINT = "type_point";
    public static String TYPE_ANSWER = "type_answer";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        overridePendingTransition(R.anim.slide_in_bottom, 0);
        ButterKnife.bind(this);
        initView();
        initReplyData();
    }

    public void initReplyData() {
        mTopicRepo.getPointReply("1",null,"5")
                .compose(this.<ReplyBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ReplyBean>() {
                    @Override
                    public void onDataSuccess(ReplyBean list) {
                        replyBeanList.clear();
                        replyBeanList.addAll(list.data);
                        adapter.setReplyList(replyBeanList);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    private void initView() {
        int statusHeight = ViewUtil.getStatusHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llTopRoot.getLayoutParams();
        params.topMargin = statusHeight;
        llTopRoot.setLayoutParams(params);
        rvCommentAnswer.setInterface(this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new PointAnswerAdapter(this);
        rvCommentAnswer.setLayoutManager(mLayoutManager);
        rvCommentAnswer.setAdapter(adapter);

    }


    @Override
    public void goFinish() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    public void recover() {
        root.setBackgroundColor(Color.parseColor("#4D000000"));
    }

    @Override
    public void move() {
        root.setBackgroundColor(Color.parseColor("#00000000"));
    }

    @OnClick(R.id.close_icon)
    public void onClick() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
