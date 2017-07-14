package com.youloft.lilith.topic.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.glide.GlideCircleTransform;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.bean.ClickLikeBean;
import com.youloft.lilith.topic.bean.MyTopicBean;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.topic.db.TopicLikingTable;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 我参与的话题的Holder
 * version
 *
 * @author slj
 * @time 2017/7/10 16:39
 * @class MyTopicHolder
 */

public class MyTopicHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_comment_user)
    ImageView imageCommentUser;
    @BindView(R.id.text_user_name)
    TextView textUserName;
    @BindView(R.id.image_user_sex)
    ImageView imageUserSex;
    @BindView(R.id.text_user_constellation)
    TextView textUserConstellation;
    @BindView(R.id.image_zan)
    ImageView imageZan;
    @BindView(R.id.text_zan_count)
    TextView textZanCount;
    @BindView(R.id.text_vote_result)
    TextView textVoteResult;
    @BindView(R.id.text_comment_content)
    TextView textCommentContent;
    @BindView(R.id.text_topic_title)
    TextView textTopicTitle;
    @BindView(R.id.text_comment_time)
    TextView textCommentTime;
    @BindView(R.id.text_comment_answer_count)
    TextView textCommentAnswerCount;
    @BindView(R.id.comment_divider_bottom)
    View commentDividerBottom;
    private Context mContext;
    private MyTopicBean.DataBean topicBean;
    private TopicDetailBean.DataBean topicInfo;
    private UserBean.DataBean.UserInfoBean userInfo;
    private PointBean.DataBean pointInfo;
    private int isZan;

    public MyTopicHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();


    }

    /**
     * 绑定数据
     *
     * @param point  观点信息
     * @param isLast 是否是最后一条数据
     */
    public void bind(MyTopicBean.DataBean point, boolean isLast) {
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean == null) return;
        userInfo = userBean.data.userInfo;
        if (userInfo == null) return;
        this.topicBean = point;
        initPointAndTopic(point);
        if (isLast) {
            commentDividerBottom.setVisibility(View.GONE);
        } else {
            commentDividerBottom.setVisibility(View.VISIBLE);
        }
        //头像
        GlideApp.with(mContext)
                .asBitmap().transform(new GlideCircleTransform())
                .load(userInfo.headImg)
                .placeholder(R.drawable.default_user_head_img)
                .error(R.drawable.default_user_head_img)
                .into(imageCommentUser);

        //昵称
        textUserName.setText(userInfo.nickName);
        //星座
        textUserConstellation.setText(ConsManager.getConsName(userInfo.signs));
        //性别
        if (userInfo.sex == 2) {
            imageUserSex.setImageResource(R.drawable.topic_male_icon);
        } else {
            imageUserSex.setImageResource(R.drawable.topic_female_icon);
        }
        bindZan(point);
        textCommentContent.setText(point.Viewpoint);
        textVoteResult.setText("投票给：" + point.optionTitle);
        if (point.isclose != 1) {
            textTopicTitle.setText(point.topicIdTitle);
        } else {
            textTopicTitle.setText("抱歉！此话题已关闭");
        }
        if (point.topicOptionId % 2 == 1) {
            textVoteResult.setTextColor(Color.parseColor("#ff8282"));
        } else {
            textVoteResult.setTextColor(Color.parseColor("#5696df"));
        }
        textCommentTime.setText(CalendarHelper.getInterValTime(CalendarHelper.getTimeMillisByString(point.date), System.currentTimeMillis()));
        textCommentAnswerCount.setText(String.valueOf(point.reply));
    }

    /**
     * 绑定赞的数据
     *
     * @param point
     */
    private void bindZan(MyTopicBean.DataBean point) {
        int zanCount;
        int id = point.id;
        //首先看数据库里有没有操作过的赞的信息
        TopicLikingTable table = TopicLikeCache.getIns(mContext).getInforByCode(id, PointDetailActivity.TYPE_POINT);
        zanCount = point.zan;
        if (table == null) {
            isZan = point.isclick;
        } else {
            isZan = table.mIsLike;
            if (table.mIsLike == point.isclick) {
                TopicLikeCache.getIns(mContext).deleteData(id, PointDetailActivity.TYPE_POINT);
            } else {
                if (table.mIsPost != 1) clickLike();
                // dataBean.isclick = table.mIsLike;
                if (table.mIsLike == 1) {
                    zanCount = point.zan + 1;
                }
            }
        }
        if (isZan == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textZanCount.setText(String.valueOf(zanCount));
    }

    private void clickLike() {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo != null) {
            int userId = userInfo.data.userInfo.id;
            TopicRepo.likeReply(String.valueOf(topicBean.id), String.valueOf(userId))
                    .subscribeOn(Schedulers.newThread())
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxObserver<ClickLikeBean>() {
                        @Override
                        public void onDataSuccess(ClickLikeBean s) {
                            if (s.data) {
                                updateClickTable(1);
                            } else {
                                updateClickTable(0);
                            }
                        }

                        @Override
                        protected void onFailed(Throwable e) {
                            super.onFailed(e);
                            updateClickTable(0);
                        }
                    });
        }
    }

    public void updateClickTable(int ispost) {
        TopicLikingTable topicLikingTable;
        if (isZan == 0) {
            topicLikingTable = new TopicLikingTable(topicBean.id, 0, PointDetailActivity.TYPE_POINT, ispost);
        } else {
            topicLikingTable = new TopicLikingTable(topicBean.id, 1, PointDetailActivity.TYPE_POINT, ispost);
        }
        TopicLikeCache.getIns(itemView.getContext()).insertData(topicLikingTable);
    }

    private void initPointAndTopic(MyTopicBean.DataBean point) {
        pointInfo = new PointBean.DataBean();
        pointInfo.id = point.id;
        pointInfo.reply = point.reply;
        pointInfo.viewpoint = point.Viewpoint;
        pointInfo.topicOptionId = point.topicOptionId;
        pointInfo.signs = userInfo.signs;
        pointInfo.sex = userInfo.sex;
        pointInfo.buildDate = point.date;
        pointInfo.isclick = point.isclick;
        pointInfo.nickName = userInfo.nickName;
        pointInfo.zan = point.zan;
        pointInfo.headImg = userInfo.headImg;
        pointInfo.userId = userInfo.id;
        pointInfo.replyList = new ArrayList<>();

        topicInfo = new TopicDetailBean.DataBean();
        topicInfo.option = new ArrayList<>();
        TopicDetailBean.DataBean.OptionBean optionBean1 = new TopicDetailBean.DataBean.OptionBean();
        TopicDetailBean.DataBean.OptionBean optionBean2 = new TopicDetailBean.DataBean.OptionBean();
        int option1_id;
        int option2_id;
        if (point.topicOptionId / 2 == 0) {
            option1_id = point.topicOptionId - 1;
            option2_id = point.topicOptionId;
        } else {
            option1_id = point.topicOptionId;
            option2_id = point.topicOptionId + 1;
        }
        if (option1_id == point.topicOptionId) {
            optionBean1.vote = 0;
            optionBean1.id = option1_id;
            optionBean1.buildDate = point.date;
            optionBean1.shortTitle = "";
            optionBean1.title = point.optionTitle;
            optionBean2.vote = 0;
            optionBean2.id = option2_id;
            optionBean2.buildDate = point.date;
            optionBean2.shortTitle = "";
            optionBean2.title = "";
        } else {
            optionBean2.vote = 0;
            optionBean2.id = option2_id;
            optionBean2.buildDate = point.date;
            optionBean2.shortTitle = "";
            optionBean2.title = point.optionTitle;
            optionBean1.vote = 0;
            optionBean1.id = option2_id;
            optionBean1.buildDate = point.date;
            optionBean1.shortTitle = "";
            optionBean1.title = "";
        }
        topicInfo.option.add(optionBean1);
        topicInfo.option.add(optionBean2);
        topicInfo.title = point.topicIdTitle;
        topicInfo.isVote = 1;
        topicInfo.backImg = "";
        topicInfo.id = point.topicId;
        topicInfo.totalVote = 1;
    }

    @OnClick({R.id.text_topic_title, R.id.text_comment_time, R.id.text_comment_answer_count})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_topic_title:
                if (topicBean.isclose == 1) return;
                ARouter.getInstance().build("/test/TopicDetailActivity")
                        .withInt("tid", topicBean.topicId)
                        .navigation();
                break;
            case R.id.text_comment_time:
            case R.id.text_comment_answer_count:
                ARouter.getInstance().build("/test/PointDetailActivity")
                        .withObject("point", pointInfo)
                        .withObject("topic", topicInfo)
                        .navigation();
                break;
        }
    }
}
