package com.youloft.lilith.topic.holder;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.LLApplication;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.StringUtil;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.PointAnswerAdapter;
import com.youloft.lilith.topic.bean.ClickLikeBean;
import com.youloft.lilith.topic.bean.ClickLikeEvent;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.topic.db.TopicLikingTable;
import com.youloft.lilith.topic.widget.Rotate3dAnimation;
import com.youloft.lilith.glide.GlideCircleTransform;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */

public class PointAnswerNormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.text_comment_hot)
    TextView textCommentHot;
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
    @BindView(R.id.text_answer_content)
    TextView textAnswerContent;
    @BindView(R.id.text_answer_time)
    TextView textAnswerTime;
    @BindView(R.id.text_reply)
    TextView textReply;
    @BindView(R.id.text_to_name)
    TextView textToName;
    @BindView(R.id.ll_reply)
    LinearLayout llReply;
    private int isZan;
    private ReplyBean.DataBean mData;
    private Context mContext;
    private int zanZount;
    private PointAnswerAdapter adpter;
    private ReplyBean.DataBean replyInfo;
    public PointAnswerNormalHolder(View itemView, PointAnswerAdapter adpter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = itemView.getContext();
        this.adpter =adpter;
        initView();
    }

    private void initView() {

    }

    /**
     *  @param dataBean  回复的数据
     * @param point
     * @param isFirst   是否为列表的第一条
     */
    public void bindView(ReplyBean.DataBean dataBean, PointBean.DataBean point, boolean isFirst) {
        if (dataBean == null)return;
        UserBean userBean = AppSetting.getUserInfo();
        this.mData = dataBean;
        if (isFirst) {
            textCommentHot.setVisibility(View.VISIBLE);
        } else {
            textCommentHot.setVisibility(View.GONE);
        }
        if (userBean != null && userBean.data.userInfo.id == dataBean.uid) {
            textUserName.setText(StringUtil.toNameString(userBean.data.userInfo.nickName));
        } else {
            textUserName.setText(StringUtil.toNameString(dataBean.nickName));
        }
        if (point != null && dataBean.uid == point.userId) {
            textUserName.setText(textUserName.getText().toString() + "(作者)");
        }
        textAnswerContent.setText(dataBean.contents);
        textUserConstellation.setText(ConsManager.getConsName(dataBean.signs));
        if (dataBean.sex == 1 ||dataBean.sex == 0) {
            imageUserSex.setImageResource(R.drawable.topic_female_icon);
        } else {
            imageUserSex.setImageResource(R.drawable.topic_male_icon);
        }
        bindZan(dataBean);
        if (dataBean.pName == null || TextUtils.isEmpty(dataBean.pName)) {
            textToName.setVisibility(View.GONE);
        } else {
            textToName.setVisibility(View.VISIBLE);
            if (userBean != null && userBean.data.userInfo.id == dataBean.pid) {
                textToName.setText(StringUtil.toNameString(userBean.data.userInfo.nickName));
            } else {
                textToName.setText(StringUtil.toNameString(dataBean.pName));
            }

        }
        GlideApp.with(itemView)
                .asBitmap()
                .transform(GlideCircleTransform.getInstance(LLApplication.getInstance()))
                .load(dataBean.headImg)
                .placeholder(R.drawable.default_user_head_img)
                .error(R.drawable.default_user_head_img)
                .into(imageCommentUser);
        imageZan.setOnClickListener(this);
        llReply.setOnClickListener(this);
        textAnswerTime.setText(CalendarHelper.getInterValTime(CalendarHelper.getTimeMillisByString(dataBean.date),System.currentTimeMillis()));
    }

    private void bindZan(ReplyBean.DataBean dataBean) {
        int id = dataBean.id;
        TopicLikingTable table = TopicLikeCache.getIns(mContext).getInforByCode(id,PointDetailActivity.TYPE_ANSWER);
        zanZount = dataBean.zan;
        if (table == null) {
            isZan = dataBean.isclick;
        } else {
            isZan = table.mIsLike;
            if (table.mIsLike == dataBean.isclick) {
                TopicLikeCache.getIns(mContext).deleteData(id,PointDetailActivity.TYPE_ANSWER);
            } else {
                if (table.mIsPost != 1)clickLike();
                if (table.mIsLike == 1) {
                zanZount++;
                }
            }
        }
        if (isZan == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textZanCount.setText(String.valueOf(zanZount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_zan:
            case R.id.text_zan_count:
                if (AppSetting.getUserInfo() == null) {
                    new LogInOrCompleteDialog(mContext).setStatus(LogInOrCompleteDialog.TOPIC_IN).show();
                    return;
                }
                ((BitmapDrawable) imageZan.getDrawable()).setAntiAlias(true);
                Rotate3dAnimation m3DAnimation;
                if (isZan == 1) {
                    m3DAnimation = new Rotate3dAnimation(0, 180,
                            imageZan.getWidth() / 2, imageZan.getHeight() / 2);
                    isZan =0;

                } else {
                    m3DAnimation = new Rotate3dAnimation(180, 0,
                            imageZan.getWidth() / 2, imageZan.getHeight() / 2);
                    isZan = 1;
                }
                m3DAnimation.setDuration(300);
                imageZan.startAnimation(m3DAnimation);
                m3DAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (isZan == 0) {
                            imageZan.setImageResource(R.drawable.topic_like_icon);
                            zanZount--;
                            textZanCount.setText(String.valueOf(zanZount));
                        } else {
                            imageZan.setImageResource(R.drawable.topic_liking_icon);
                            zanZount++;
                            textZanCount.setText(String.valueOf(zanZount));
                        }
                        clickLike();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            case R.id.ll_reply:
                if (itemView.getContext() instanceof PointDetailActivity) {
                    ((PointDetailActivity) itemView.getContext()).clickReply(mData.uid,mData.nickName);
                }
                break;
        }
    }

    private void clickLike() {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo != null) {
            int userId = userInfo.data.userInfo.id;
            TopicRepo.likeReply(String.valueOf(mData.id), String.valueOf(userId))
                    .subscribeOn(Schedulers.newThread())
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxObserver<ClickLikeBean>() {
                        @Override
                        public void onDataSuccess(ClickLikeBean s) {
                            if ( s.data) {
                                updateLikeTable(1);
                            } else {
                                updateLikeTable(0);
                            }
                        }

                        @Override
                        protected void onFailed(Throwable e) {
                            super.onFailed(e);
                            updateLikeTable(0);
                        }
                    });
        }
    }

    /**
     *      更新点赞数据库
     * @param ispost  是否提交成功
     */
    private void updateLikeTable(int ispost) {
        TopicLikingTable topicLikingTable;
        if (isZan == 0) {
            topicLikingTable = new TopicLikingTable(mData.id,0, PointDetailActivity.TYPE_ANSWER,ispost);

        } else {
            topicLikingTable = new TopicLikingTable(mData.id,1,PointDetailActivity.TYPE_ANSWER,ispost);

        }
        TopicLikeCache.getIns(itemView.getContext()).insertData(topicLikingTable);
        EventBus.getDefault().post(new ClickLikeEvent(ClickLikeEvent.TYPE_ANSWER));
    }
}
