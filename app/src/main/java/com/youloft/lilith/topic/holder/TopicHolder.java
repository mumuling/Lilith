package com.youloft.lilith.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.widget.TopicUserImageLayout;

/**
 *
 */

public class TopicHolder extends RecyclerView.ViewHolder {
    public TextView mTopicContent;
    public TopicUserImageLayout mTopicUserImageLayout;
    public ImageView mTopicImage;
    public TopicHolder(View itemView) {
        super(itemView);
        mTopicContent = (TextView) itemView.findViewById(R.id.topic_content);
        mTopicUserImageLayout = (TopicUserImageLayout) itemView.findViewById(R.id.layout_user_image);
        mTopicImage = (ImageView) itemView.findViewById(R.id.image_topic_bg);
    }

    public void bind(TopicBean.DataBean topic) {
        mTopicContent.setText(topic.title);
        GlideApp.with(itemView)
                .asBitmap()
                .load(topic.backImg)
                .into(mTopicImage);
        mTopicUserImageLayout.bindData(topic.voteUser,topic.totalVote);
    }
}