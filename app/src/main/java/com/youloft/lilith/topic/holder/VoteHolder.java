package com.youloft.lilith.topic.holder;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.topic.widget.VoteView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */


public class VoteHolder extends RecyclerView.ViewHolder {

    ImageView imageTop;

    TextView textTopicTitle;

    VoteView voteView;

    private TopicDetailBean.DataBean topicInfo;

    public VoteHolder(View itemView) {
        super(itemView);
        initView();
        final VoteDialog voteDialog = new VoteDialog(itemView.getContext());
        voteView.setInterface(new VoteView.OnItemClickListener() {
            @Override
            public void clickLeft() {
                voteDialog.show();
                voteDialog.setTitle("正方");
            }

            @Override
            public void clickRight() {
                voteDialog.show();
                voteDialog.setTitle("反方");
            }
        });



        ValueAnimator firstAnimation = new ValueAnimator();
        firstAnimation.setFloatValues(0.0f, 1.0f);
        firstAnimation.setDuration(4000);
        firstAnimation.setRepeatMode(ValueAnimator.RESTART);
        firstAnimation.setRepeatCount(ValueAnimator.INFINITE);
        firstAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue() * 2;
                if (value > 1.0) value = 1.0f;
                int changeWidth = (int) ViewUtil.dp2px(12);
                voteView.setChangeValue1(changeWidth * value * 2, (int) (255 * (1 - value)));
            }
        });

        ValueAnimator secondAnimation = new ValueAnimator();
        secondAnimation.setFloatValues(0.0f, 1.0f);
        secondAnimation.setDuration(4000);
        secondAnimation.setRepeatMode(ValueAnimator.RESTART);
        secondAnimation.setRepeatCount(ValueAnimator.INFINITE);
        secondAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < 0.125) {
                    value = 0;
                } else if (value > 0.625) {
                    value = 1;
                } else {
                    value = (float) ((value - 0.125) * 2);
                }
                int changeWidth = (int) ViewUtil.dp2px(12);
                voteView.setChangeValue2(changeWidth * value * 2, (int) (255 * (1 - value)));
            }
        });
        ValueAnimator thirdAnimation = new ValueAnimator();
        thirdAnimation.setFloatValues(0.0f, 1.0f);
        thirdAnimation.setDuration(4000);
        thirdAnimation.setRepeatMode(ValueAnimator.RESTART);
        thirdAnimation.setRepeatCount(ValueAnimator.INFINITE);
        thirdAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < 0.375) {
                    value = 0;
                } else if (value > 0.875) {
                    value = 1;
                } else {
                    value = (float) ((value - 0.3755) * 2);
                }
                int changeWidth = (int) ViewUtil.dp2px(12);
                voteView.setChangeValue3(changeWidth * value * 2, (int) (255 * (1 - value)));
            }
        });
        firstAnimation.start();
        secondAnimation.start();
        thirdAnimation.start();


    }

    private void initView() {
        imageTop = (ImageView) itemView.findViewById(R.id.image_top);
        textTopicTitle = (TextView) itemView.findViewById(R.id.text_topic_title);
        voteView = (VoteView) itemView.findViewById(R.id.vote_view);
    }

    public void bindView(TopicDetailBean.DataBean topicInfo) {
        if (topicInfo == null)return;
        this.topicInfo = topicInfo;
        GlideApp.with(itemView.getContext())
                .asBitmap()
                .load(topicInfo.backImg)
                .into(imageTop);
        textTopicTitle.setText("#" + topicInfo.title);
    }
}
