package com.youloft.lilith.topic.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.holder.PointHolder;
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.topic.widget.VoteView;

import java.util.ArrayList;
import java.util.List;

/**         话题详情的Adapter
 *version
 *@author  slj
 *@time    2017/7/4 14:08
 *@class   TopicDetailAdapter
 */

public class TopicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static int ITEM_TYPE_HEADER = 1000;//顶部header
    private static int ITEM_TYPE_VOTE_VIEW = 2000;//投票的view
    private static int ITEM_TYPE_COMMENT = 3000;//评论item
    private List<PointBean> pointBeanList = new ArrayList<>();
    public TopicDetailAdapter (Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setPointBeanList(List<PointBean> list) {
        pointBeanList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE_HEADER) {
            holder = new HeadHolder(mInflater.inflate(R.layout.item_topic_detail_title,parent,false));
        } else if (viewType == ITEM_TYPE_VOTE_VIEW){
            holder = new VoteHolder(mInflater.inflate(R.layout.item_topic_detail_vote,parent,false));
        } else {
            holder = new PointHolder(mInflater.inflate(R.layout.item_topic_detail_comment,parent,false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build("/test/PointDetailActivity").navigation();
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoteHolder) {

        } else if (holder instanceof PointHolder) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == 1) {
            return ITEM_TYPE_VOTE_VIEW;
        } else {
            return ITEM_TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return pointBeanList.size() + 12;
    }
    public class HeadHolder extends RecyclerView.ViewHolder {
        public TextView mTopicTitle;
        public ImageView mTopImage;
        public HeadHolder(View itemView) {
            super(itemView);
            mTopicTitle = (TextView) itemView.findViewById(R.id.title_top_text);
            mTopImage = (ImageView) itemView.findViewById(R.id.image_top_title);
        }
    }

    public class VoteHolder extends RecyclerView.ViewHolder {
        private VoteView mVoteView;
        public VoteHolder(View itemView) {
            super(itemView);
            mVoteView = (VoteView) itemView.findViewById(R.id.vote_view);
            final VoteDialog voteDialog = new VoteDialog(mContext);
            mVoteView.setInterface(new VoteView.OnItemClickListener() {
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

            ValueAnimator firstAnimation =  new ValueAnimator();
            firstAnimation.setFloatValues(0.0f,1.0f);
            firstAnimation.setDuration(4000);
            firstAnimation.setRepeatMode(ValueAnimator.RESTART);
            firstAnimation.setRepeatCount(ValueAnimator.INFINITE);
            firstAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue() * 2;
                    if (value > 1.0) value = 1.0f;
                    int changeWidth = (int) ViewUtil.dp2px(12);
                    mVoteView.setChangeValue1(changeWidth * value * 2, (int) (255 * (1 - value)));
                }
            });

            ValueAnimator secondAnimation =  new ValueAnimator();
            secondAnimation.setFloatValues(0.0f,1.0f);
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
                    mVoteView.setChangeValue2(changeWidth * value * 2, (int) (255 * (1 - value)));
                }
            });
            ValueAnimator thirdAnimation =  new ValueAnimator();
            thirdAnimation.setFloatValues(0.0f,1.0f);
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
                    mVoteView.setChangeValue3(changeWidth * value * 2, (int) (255 * (1 - value)));
                }
            });
            firstAnimation.start();
            secondAnimation.start();
            thirdAnimation.start();


        }
    }


}
