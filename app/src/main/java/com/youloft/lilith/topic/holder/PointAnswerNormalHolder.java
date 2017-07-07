package com.youloft.lilith.topic.holder;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.topic.db.TopicLikingTable;
import com.youloft.lilith.topic.widget.Rotate3dAnimation;
import com.youloft.lilith.ui.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private int isZan;
    private ReplyBean.DataBean mData;
    private Context mContext;
    public PointAnswerNormalHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mContext = itemView.getContext();
    }

    public void bindView(ReplyBean.DataBean dataBean,boolean isFirst) {
        if (dataBean == null)return;
        this.mData = dataBean;
        if (isFirst) {
            textCommentHot.setVisibility(View.VISIBLE);
        } else {
            textCommentHot.setVisibility(View.GONE);
        }
        textUserName.setText(dataBean.nickName);
        textAnswerContent.setText(dataBean.contents);
        if (dataBean.sex == 1) {
            imageUserSex.setImageResource(R.drawable.topic_female_icon);
        } else {
            imageUserSex.setImageResource(R.drawable.topic_male_icon);
        }
        bindZan(dataBean);
        if (dataBean.pName == null || TextUtils.isEmpty(dataBean.pName)) {
            textToName.setVisibility(View.GONE);
        } else {
            textToName.setVisibility(View.VISIBLE);
            textToName.setText(dataBean.pName);
        }
        GlideApp.with(itemView)
                .asBitmap()
                .transform(new GlideCircleTransform(itemView.getContext()))
                .load(dataBean.headImg)
                .into(imageCommentUser);
        imageZan.setOnClickListener(this);
    }

    private void bindZan(ReplyBean.DataBean dataBean) {
        int id = dataBean.id;
        TopicLikingTable table = TopicLikeCache.getIns(mContext).getInforByCode(id,PointDetailActivity.TYPE_ANSWER);
        if (table == null) {
            isZan = dataBean.isclick;
        } else {
            isZan = table.mIsLike;
            if (table.mIsLike == dataBean.isclick) {
                TopicLikeCache.getIns(mContext).deleteData(id,PointDetailActivity.TYPE_ANSWER);
            } else if (table.mIsLike == 1) {
                mData.zan++;
            }
        }
        if (isZan == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textZanCount.setText(String.valueOf(mData.zan));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_zan:
            case R.id.text_zan_count:

                ((BitmapDrawable) imageZan.getDrawable()).setAntiAlias(true);
                Rotate3dAnimation m3DAnimation;
                if (isZan == 1) {
                    m3DAnimation = new Rotate3dAnimation(0, 180,
                            imageZan.getWidth() / 2, imageZan.getHeight() / 2);
                } else {
                    m3DAnimation = new Rotate3dAnimation(180, 0,
                            imageZan.getWidth() / 2, imageZan.getHeight() / 2);
                }
                m3DAnimation.setDuration(300);
                imageZan.startAnimation(m3DAnimation);
                m3DAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        TopicLikingTable topicLikingTable;
                        if (isZan == 1) {
                            imageZan.setImageResource(R.drawable.topic_like_icon);
                            mData.zan--;
                            textZanCount.setText(String.valueOf(mData.zan));
                            topicLikingTable = new TopicLikingTable(mData.id,0, PointDetailActivity.TYPE_ANSWER);
                            isZan =0;
                        } else {
                            imageZan.setImageResource(R.drawable.topic_liking_icon);
                            mData.zan++;
                            textZanCount.setText(String.valueOf(mData.zan));
                            topicLikingTable = new TopicLikingTable(mData.id,1,PointDetailActivity.TYPE_ANSWER);
                            isZan = 1;
                        }
                        TopicLikeCache.getIns(itemView.getContext()).insertData(topicLikingTable);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            case R.id.text_reply:
                break;
        }
    }
}
