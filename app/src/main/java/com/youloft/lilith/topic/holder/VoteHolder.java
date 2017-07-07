package com.youloft.lilith.topic.holder;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.topic.widget.VoteView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */


public class VoteHolder extends RecyclerView.ViewHolder {

    ImageView imageTop;

    TextView textTopicTitle;

    VoteView voteView;

    private TopicDetailBean.DataBean topicInfo;
    private VoteDialog voteDialog ;
    private ValueAnimator firstAnimation;
    private ValueAnimator secondAnimation;
    private ValueAnimator thirdAnimation;
    private boolean needVoteAnimation = true;

    public VoteHolder(View itemView) {
        super(itemView);
        initView();
        firstAnimation = new ValueAnimator();
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

         secondAnimation = new ValueAnimator();
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
         thirdAnimation = new ValueAnimator();
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

    private void voteAniamtion(final float scale) {
        firstAnimation.cancel();
        secondAnimation.cancel();
        thirdAnimation.cancel();
        ValueAnimator rectAnimater = new ValueAnimator();
        rectAnimater.setDuration(2000);
        rectAnimater.setFloatValues(0.0f,1.0f);
        rectAnimater.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                voteView.setRectProportion(value,scale);


            }
        });
        rectAnimater.start();
    }

    private void initView() {
        voteDialog = new VoteDialog(itemView.getContext());
        voteDialog.setListener(new VoteDialog.OnClickConfirmListener() {
            @Override
            public void clickConfirm(String msg,int id) {
                TopicRepo.postVote(String.valueOf(topicInfo.id),String.valueOf(id),"10000",msg)
                        .subscribeOn(Schedulers.newThread())
                        .toObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxObserver<String>() {
                            @Override
                            public void onDataSuccess(String s) {
                                String ms =s;
                            }

                            @Override
                            protected void onFailed(Throwable e) {
                                super.onFailed(e);
                            }
                        });
                topicInfo.totalVote++;
                addOptionVote(id);
                voteAniamtion((float) topicInfo.option.get(0).vote/topicInfo.totalVote);
            }
        });
        imageTop = (ImageView) itemView.findViewById(R.id.image_top);
        textTopicTitle = (TextView) itemView.findViewById(R.id.text_topic_title);
        voteView = (VoteView) itemView.findViewById(R.id.vote_view);
    }
    public void addOptionVote(int id) {
        if (topicInfo.option == null || topicInfo.option.size() == 0 )return;
        for (int i =0; i < topicInfo.option.size();i ++) {
            if (id == topicInfo.option.get(i).id) {
                topicInfo.option.get(i).vote++;
            }
        }
    }
    public void bindView(final TopicDetailBean.DataBean topicInfo) {
        if (topicInfo == null || topicInfo.option == null)return;
        this.topicInfo = topicInfo;
        voteView.setInterface(new VoteView.OnItemClickListener() {
            @Override
            public void clickLeft() {
                voteDialog.show();
                voteDialog.setTitle(topicInfo.option.get(0).shortTitle,topicInfo.option.get(0).id);
            }

            @Override
            public void clickRight() {
                voteDialog.show();
                voteDialog.setTitle(topicInfo.option.get(1).shortTitle,topicInfo.option.get(1).id);
            }
        });
        if (needVoteAnimation) {
            voteAniamtion((float) topicInfo.option.get(0).vote/topicInfo.totalVote);
            needVoteAnimation = false;
        }
        GlideApp.with(itemView.getContext())
                .asBitmap()
                .load(topicInfo.backImg)
                .into(imageTop);
        textTopicTitle.setText("#" + topicInfo.title);
    }
}
