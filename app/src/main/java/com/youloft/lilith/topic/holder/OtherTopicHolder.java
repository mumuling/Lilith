package com.youloft.lilith.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.widgets.view.RoundImageView;
import com.youloft.lilith.glide.GlideBlurTransform;
import com.youloft.lilith.topic.TopicDetailActivity;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.widget.TopicUserImageLayout;
import com.youloft.statistics.AppAnalytics;

/**       话题详情页底部的其他话题推荐
 *version
 *@author  slj
 *@time    2017/7/6 9:31
 *@class   OtherTopicHolder
 */

public class OtherTopicHolder extends RecyclerView.ViewHolder {
    public TextView mTopicContent;
    public TopicUserImageLayout mTopicUserImageLayout;
    public RoundImageView mTopicImage;
    private TextView mOtherTopicText;
    private TopicBean.DataBean topic;
    private boolean isReport = false;


    public OtherTopicHolder(View itemView) {
        super(itemView);
        mTopicContent = (TextView) itemView.findViewById(R.id.topic_content);
        mTopicUserImageLayout = (TopicUserImageLayout) itemView.findViewById(R.id.layout_user_image);
        mTopicImage = (RoundImageView) itemView.findViewById(R.id.image_topic_bg);
        mOtherTopicText = (TextView) itemView.findViewById(R.id.text_other_topic);
    }

    private static final String TAG = "OtherTopicHolder";
    public void bind(TopicDetailAdapter adapter,final TopicBean.DataBean topic, final int position , boolean first) {
        if (topic == null ) {
            return;
        }
        //更多话题展示的埋点
        if (!adapter.hashPosition.contains(String.valueOf(position))) {
            AppAnalytics.onEvent("Commenttopic.IM" , String.valueOf(position));
            adapter.hashPosition.add(String.valueOf(position));
        }

       itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/test/TopicDetailActivity")
                        .withInt("tid",topic.id)
                        .navigation();
                AppAnalytics.onEvent("Commenttopic.C" , String.valueOf(position));
                if (itemView.getContext() instanceof TopicDetailActivity) {
                    ((TopicDetailActivity) itemView.getContext()).finish();
                }
            }
        });
        mTopicContent.setText(topic.title);
        if (first) {
            mOtherTopicText.setVisibility(View.VISIBLE);
        } else {
            mOtherTopicText.setVisibility(View.GONE);
        }
        if (this.topic != null && this.topic.backImg.equals(topic.backImg)) return;
        GlideApp.with(itemView)
                .asBitmap()
                .transform(new GlideBlurTransform(itemView.getContext()))
                .load(topic.backImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(188,75)
                .into(mTopicImage);
        mTopicUserImageLayout.bindData(topic.voteUser,topic.totalVote);

        this.topic = topic;
    }
}

