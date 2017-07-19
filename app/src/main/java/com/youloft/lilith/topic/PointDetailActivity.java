package com.youloft.lilith.topic;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
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
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.StringUtil;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.adapter.PointAnswerAdapter;
import com.youloft.lilith.topic.bean.AnswerEvent;
import com.youloft.lilith.topic.bean.PointAnswerBean;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointAnswerTable;
import com.youloft.lilith.topic.widget.ScrollFrameLayout;
import com.youloft.lilith.topic.widget.SoftInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    LinearLayout llTopRoot;//顶部title
    @BindView(R.id.close_icon)
    ImageView closeIcon; //关闭按钮
    @BindView(R.id.command_num)
    TextView commandNum;  //评论数
    @BindView(R.id.root)
    SoftInputLayout root;
    @BindView(R.id.rv_comment_answer)
    ScrollFrameLayout rvCommentAnswer;//下滑f返回的ecyclerview
    @BindView(R.id.comment_edit)
    EditText commentEdit;//底部编辑框
    @Autowired
    public PointBean.DataBean point;//观点信息
    @Autowired
    public TopicDetailBean.DataBean topic;//话题信息
    @Autowired
    public int position;//上一级观点列表的位置信息
    @BindView(R.id.text_confirm)
    TextView textConfirm;//确认按钮
    @BindView(R.id.image_pen)
    ImageView imagePen;//
    @BindView(R.id.image_root)
    ImageView imageRoot;
    private LinearLayoutManager mLayoutManager;
    private PointAnswerAdapter adapter;
    private List<ReplyBean.DataBean> replyBeanList = new ArrayList<>();//回复的列表
    public static String TYPE_POINT = "type_point";
    public static String TYPE_ANSWER = "type_answer";
    public int replyId = 0;//回复对象的ID
    public String replyName;//回复的对象
    private PointAnswerCache pointAnswerCache;//回复的数据缓存
    private InputMethodManager imm;
    private UserBean.DataBean.UserInfoBean userInfo = null;
    private boolean isReplyAuthor = true;//是否是评论的作者

    private int replyCount = 0;//回复的条数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        Log.d("time",System.currentTimeMillis() +"");
        overridePendingTransition(R.anim.slide_in_bottom, 0);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        pointAnswerCache = PointAnswerCache.getIns(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        UserBean userBean = AppSetting.getUserInfo();
        if (userBean != null) {
            userInfo = userBean.data.userInfo;
        }

        initView();
        initReplyData();

    }

    /**
     * 登录状态改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoddingChagne(LoginEvent event) {
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean != null) {
            userInfo = userBean.data.userInfo;
        }

    }

    /**
     * 请求此观点的回复列表
     */
    public void initReplyData() {
        int userId;
        if (userInfo == null) {
            userId = 0;
        } else {
            userId = userInfo.id;
        }
        TopicRepo.getPointReply(String.valueOf(point.id), String.valueOf(userId), "10", null, false)
                .compose(this.<ReplyBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ReplyBean>() {
                    @Override
                    public void onDataSuccess(ReplyBean list) {
                        replyBeanList.clear();
                        replyBeanList.addAll(list.data);
                        handleAnswerDb(list);
                        commandNum.setText(String.valueOf(replyCount) + "条回复");
                        adapter.setReplyList(replyBeanList);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                    }
                });
    }

    /**
     * 处理回复列表的数据库
     *
     * @param replyBean
     */
    private void handleAnswerDb(ReplyBean replyBean) {
        ArrayList<PointAnswerTable> tableArrayList = pointAnswerCache.getAnswerListByCode(point.id);
        if (userInfo == null) return;
        if (tableArrayList == null || tableArrayList.size() == 0) return;
        PointAnswerTable pointAnswerTable = null;

        for (int j = 0; j < tableArrayList.size(); j++) {
            pointAnswerTable = tableArrayList.get(j);
            if (pointAnswerTable.time < replyBean.t) {
               continue;// pointAnswerCache.deleteData(pointAnswerTable.rid);
            } else {
                ReplyBean.DataBean dataBean = new ReplyBean.DataBean();
                dataBean.headImg = userInfo.headImg;
                dataBean.pName = pointAnswerTable.replyName;
                dataBean.contents = pointAnswerTable.viewPoint;
                dataBean.isclick = 0;
                dataBean.zan = 0;
                dataBean.sex = userInfo.sex;
                dataBean.date = pointAnswerTable.buildDate;
                dataBean.id = pointAnswerTable.rid;
                dataBean.pid = pointAnswerTable.tid;
                dataBean.uid = userInfo.id;
                dataBean.nickName = userInfo.nickName;
                dataBean.signs = userInfo.signs;
                replyBeanList.add(0, dataBean);
              //  replyCount++;
            }
        }
    }

    private void initView() {
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        replyName = "";
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

                if (rootInvisibleHeight <= 200) {
                    //软键盘隐藏啦
                    rvCommentAnswer.setNeedScrollDown(true);
                    imagePen.setVisibility(View.VISIBLE);
                    textConfirm.setVisibility(View.GONE);
                    commentEdit.setText("");
                    commentEdit.clearFocus();
                    commentEdit.setHint("你来说点什么吧");
                    isReplyAuthor = true;

                } else {
                    ////软键盘弹出啦
                    if (isReplyAuthor) {
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
            } else {
                commandNum.setText(point.reply + "条回复");
            }
            replyCount = point.reply;
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
                    if (replyBeanList != null && replyBeanList.size() >= 10) {
                        loadMoreReply();
                    }
                }
            }
        });
        commentEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (AppSetting.getUserInfo() == null) {
                            new LogInOrCompleteDialog(PointDetailActivity.this).setStatus(LogInOrCompleteDialog.TOPIC_IN).show();
                            return true;
                        }
                        break;
                }

                return false;
            }
        });


    }

    /**
     * 滑动到底部加载更多
     */
    private void loadMoreReply() {
        if (point == null) return;
        int userId;
        if (userInfo == null) {
            userId = 0;
        } else {
            userId = userInfo.id;
        }

        TopicRepo.getPointReply(String.valueOf(point.id), String.valueOf(userId), "10", replyBeanList.size() + "", false)
                .compose(this.<ReplyBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ReplyBean>() {
                    @Override
                    public void onDataSuccess(ReplyBean replyBean) {
                        if (replyBean.data != null && replyBean.data.size() != 0) {
                            replyBeanList.addAll(replyBean.data);
                            adapter.setReplyList(replyBean.data);
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

    public void clickReply(int replyId, String replyName) {
        if (AppSetting.getUserInfo() == null) {
            new LogInOrCompleteDialog(PointDetailActivity.this).setStatus(LogInOrCompleteDialog.TOPIC_IN).show();
            return;
        }
        this.replyId = replyId;
        this.replyName = replyName;
        isReplyAuthor = false;
        if (imm != null) {
            commentEdit.requestFocus();
            imm.showSoftInput(commentEdit, 0);
        }

        commentEdit.setHint("回复 " + StringUtil.toNameString(replyName));

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
        imageRoot.setAlpha(0.3f);
    }

    @Override
    public void move(float distance) {
        imageRoot.setAlpha((float) (0.3 - Math.min(distance/500,0.3)));
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
                if (AppSetting.getUserInfo() == null) {
                    new LogInOrCompleteDialog(this).setStatus(LogInOrCompleteDialog.TOPIC_IN).show();
                    return;
                }
                if (TextUtils.isEmpty(commentEdit.getText().toString())) {
                    Toast.makeText(this, "请输入内容！", Toast.LENGTH_SHORT).show();
                } else {
                    replyConfirm();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;
            case R.id.comment_edit:


                break;
        }
    }

    /**
     * 回复
     */
    private void replyConfirm() {
        if (userInfo == null) return;
        final String reply_content = commentEdit.getText().toString();

        TopicRepo.reply(String.valueOf(point.id), String.valueOf(userInfo.id), userInfo.nickName, reply_content, String.valueOf(replyId))
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<PointAnswerBean>() {
                    @Override
                    public void onDataSuccess(PointAnswerBean result) {
                        int answerId = result.data;
                        if (answerId != 0) {
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
                            dataBean.id = answerId;
                            dataBean.signs = userInfo.signs;
                            adapter.setAnswerTop(dataBean);
                            updatePointAnswerDb(result.t, dataBean, answerId);
                            replyCount++;
                            commandNum.setText(String.valueOf(replyCount) + "条回复");
                            EventBus.getDefault().post(new AnswerEvent(replyCount,position,userInfo.nickName,reply_content));
                            Toaster.showShort("评论成功！");
                        } else {
                            Toaster.showShort("评论失败!");
                        }

                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("评论失败!");
                    }
                });
    }

    /**
     * 更新数据库
     *
     * @param dataBean 回复的数据
     * @param answerId 回复的ID
     */
    private void updatePointAnswerDb(long time, ReplyBean.DataBean dataBean, int answerId) {
        PointAnswerTable pointAnswerTable = new PointAnswerTable();
        pointAnswerTable.tid = replyId;
        pointAnswerTable.replyName = replyName;
        pointAnswerTable.buildDate = dataBean.date;
        pointAnswerTable.pid = point.id;
        pointAnswerTable.viewPoint = dataBean.contents;
        pointAnswerTable.rid = answerId;
        pointAnswerTable.time = time;
        PointAnswerCache.getIns(this).insertData(pointAnswerTable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
