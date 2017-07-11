package com.youloft.lilith.topic;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.net.AbsResponse;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.adapter.PointAnswerAdapter;
import com.youloft.lilith.topic.bean.PointAnswerBean;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointAnswerTable;
import com.youloft.lilith.topic.widget.ScrollFrameLayout;
import com.youloft.lilith.topic.widget.SoftInputLayout;

import java.util.ArrayList;
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
    @Autowired
    public PointBean.DataBean point;
    @Autowired
    public TopicDetailBean.DataBean topic;
    @BindView(R.id.text_confirm)
    TextView textConfirm;
    @BindView(R.id.image_pen)
    ImageView imagePen;
    private LinearLayoutManager mLayoutManager;
    private PointAnswerAdapter adapter;
    private List<ReplyBean.DataBean> replyBeanList = new ArrayList<>();//回复的列表
    public static String TYPE_POINT = "type_point";
    public static String TYPE_ANSWER = "type_answer";
    public int replyId = 0;
    public String replyName;
    private PointAnswerCache pointAnswerCache;
    private InputMethodManager imm ;
    private UserBean.DataBean.UserInfoBean userInfo = null;
    private boolean isReplyAuthor = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        overridePendingTransition(R.anim.slide_in_bottom, 0);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        pointAnswerCache = PointAnswerCache.getIns(this);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        if (AppConfig.LOGIN_STATUS) {
            userInfo = AppSetting.getUserInfo().data.userInfo;
        }

        initView();
        initReplyData();

    }

    /**
     * 请求此观点的回复列表
     */
    public void initReplyData() {
        int userId;
        if (userInfo == null){
            userId = 0;
        } else {
            userId = userInfo.id;
        }
        TopicRepo.getPointReply(String.valueOf(point.id), String.valueOf(userId), "10", null, true)
                .compose(this.<ReplyBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ReplyBean>() {
                    @Override
                    public void onDataSuccess(ReplyBean list) {
                        replyBeanList.clear();
                        replyBeanList.addAll(list.data);
                        handleAnswerDb(replyBeanList);
                        adapter.setReplyList(replyBeanList);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     *   处理回复列表的数据库
     * @param replyList
     */
    private void handleAnswerDb(List<ReplyBean.DataBean> replyList) {
        ArrayList<PointAnswerTable> tableArrayList = pointAnswerCache.getAnswerListByCode(point.id);
        if (userInfo == null)return;
        if (tableArrayList == null || tableArrayList.size() == 0) return;
        PointAnswerTable table = null;

            for (int i = 0 ;i < replyList.size(); i ++) {
                 table = pointAnswerCache.getInforByCode(replyList.get(i).id);
                if (table != null) {
                    pointAnswerCache.deletePointData(point.id);
                    return;
                }
            }
        for (int j = 0; j < tableArrayList.size();j++) {
            PointAnswerTable pointAnswerTable = tableArrayList.get(j);
            ReplyBean.DataBean dataBean = new ReplyBean.DataBean();
            dataBean.headImg = userInfo.headImg;
            dataBean.pName = pointAnswerTable.replyName;
            dataBean.contents = pointAnswerTable.viewPoint;
            dataBean.isclick = 0;
            dataBean.zan = 0;
            dataBean.date = pointAnswerTable.buildDate;
            dataBean.id = pointAnswerTable.rid;
            dataBean.pid = pointAnswerTable.tid;
            dataBean.uid = userInfo.id;
            dataBean.nickName = userInfo.nickName;
            replyList.add(0,dataBean);
        }
    }

    private void initView() {
        replyName ="";
        int statusHeight = ViewUtil.getStatusHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llTopRoot.getLayoutParams();
        params.topMargin = statusHeight;
        llTopRoot.setLayoutParams(params);
        rvCommentAnswer.setInterface(this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new PointAnswerAdapter(this);
        adapter.setPointAndTopic(point, topic.option);
        rvCommentAnswer.setLayoutManager(mLayoutManager);
        rvCommentAnswer.setAdapter(adapter);

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                root.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;

                if (rootInvisibleHeight <= 100) {
                    //软键盘隐藏啦
                    rvCommentAnswer.setNeedScrollDown(true);
                    imagePen.setVisibility(View.VISIBLE);
                    textConfirm.setVisibility(View.GONE);
                    commentEdit.setText("");
                    commentEdit.clearFocus();
                    commentEdit.setHint("你来说点什么吧");
                    isReplyAuthor = true;

                } else {
                    if (!AppConfig.LOGIN_STATUS) {
                        new LogInOrCompleteDialog(PointDetailActivity.this).show();
                        return;
                    }
                    ////软键盘弹出啦
                    if (isReplyAuthor){
                        replyId = 0;
                        replyName = "";
                    }
                    rvCommentAnswer.setNeedScrollDown(false);
                    textConfirm.setVisibility(View.VISIBLE);
                    imagePen.setVisibility(View.GONE);
                }
            }
        });

        if (point != null) {
            if (point.reply == 0) {
                commandNum.setText("暂无评论");
            }else {
                commandNum.setText(point.reply + "条回复");
            }
        }
        /** 滑动到底部的监听
         *
         */
        rvCommentAnswer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
                    if (replyBeanList!= null && replyBeanList.size() != 0) {
                        loadMoreReply();
                    }
                }
            }
        });



    }

    /**
     *   滑动到底部加载更多
     */
    private void loadMoreReply() {
        if (point == null)return;
        int userId;
        if (userInfo == null){
             userId = 0;
        } else {
            userId = userInfo.id;
        }

        TopicRepo.getPointReply(String.valueOf(point.id),String.valueOf(userId),"10",replyBeanList.size() + "",false)
                .compose(this.<ReplyBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ReplyBean>() {
                    @Override
                    public void onDataSuccess(ReplyBean replyBean) {
                        if (replyBean.data != null && replyBean.data.size() != 0) {
                            replyBeanList.addAll(replyBean.data);
                            adapter.setReplyList(replyBeanList);
                        } else {
                            Toaster.showShort("暂无更多评论");
                        }
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("暂无更多评论");

                    }
                });

    }

    public void clickReply(int replyId,String replyName) {
        this.replyId = replyId;
        this.replyName = replyName;
        isReplyAuthor = false;
        if (imm != null) {
            commentEdit.requestFocus();
            imm.showSoftInput(commentEdit,0);
        }

        commentEdit.setHint("回复 " + replyName + ":");

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

    @OnClick({R.id.text_confirm, R.id.comment_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_confirm:
                if (!AppConfig.LOGIN_STATUS) {
                    new LogInOrCompleteDialog(this).show();
                    return;
                }
                if (TextUtils.isEmpty(commentEdit.getText().toString())) {
                    Toast.makeText(this,"请输入内容！",Toast.LENGTH_SHORT).show();
                } else {
                    replyConfirm();
                    InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;
            case R.id.comment_edit:


                break;
        }
    }

    /**
     *   回复
     */
    private void replyConfirm() {
        if (userInfo == null)return;
        final String reply_content = commentEdit.getText().toString();

        TopicRepo.reply(String.valueOf(point.id),String.valueOf(userInfo.id),userInfo.nickName,reply_content,String.valueOf(replyId))
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<PointAnswerBean>() {
                    @Override
                    public void onDataSuccess(PointAnswerBean result) {
                        int answerId = result.data;
                        if (answerId!=0){
                            ReplyBean.DataBean dataBean = new ReplyBean.DataBean();
                            dataBean.date = CalendarHelper.getNowTimeString();
                            dataBean.zan = 0;
                            dataBean.isclick = 0;
                            dataBean.pName = replyName;
                            dataBean.pid = replyId;
                            dataBean.sex = userInfo.sex;
                            dataBean.nickName = userInfo.nickName;
                            dataBean.contents = reply_content;
                            dataBean.headImg = userInfo.headImg;
                            adapter.setAnswerTop(dataBean);
                            updatePointAnswerDb(dataBean,answerId);
                            Toaster.showShort("评论成功！");
                        }

                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     *   更新数据库
     * @param dataBean 回复的数据
     * @param answerId   回复的ID
     */
    private void updatePointAnswerDb(ReplyBean.DataBean dataBean,int answerId) {
        PointAnswerTable pointAnswerTable = new PointAnswerTable();
        pointAnswerTable.tid = replyId;
        pointAnswerTable.replyName = replyName;
        pointAnswerTable.buildDate = dataBean.date;
        pointAnswerTable.pid  = point.id;
        pointAnswerTable.viewPoint = dataBean.contents;
        pointAnswerTable.rid = answerId;
        PointAnswerCache.getIns(this).insertData(pointAnswerTable);

    }
}
