package com.youloft.lilith.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.topic.bean.ReplyBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class PointAnswerNormalHolder extends RecyclerView.ViewHolder {

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

    public PointAnswerNormalHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(ReplyBean.DataBean dataBean) {
        textUserName.setText(dataBean.nickName);
        textAnswerContent.setText(dataBean.contents);
        textZanCount.setText(String.valueOf(dataBean.zan));
        if (dataBean.sex == 1) {
            imageUserSex.setImageResource(R.drawable.topic_female_icon);
        } else {
            imageUserSex.setImageResource(R.drawable.topic_male_icon);
        }
        if (dataBean.isclick == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }

    }
}
