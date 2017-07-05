package com.youloft.lilith.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**     话题观点的holder
 *version
 *@author  slj
 *@time    2017/7/4 16:41
 *@class   PointHolder
 */

public class PointHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
    @BindView(R.id.text_comment_time)
    TextView textCommentTime;
    @BindView(R.id.text_comment_answer_count)
    TextView textCommentAnswerCount;
    @BindView(R.id.text_answer1)
    TextView textAnswer1;
    @BindView(R.id.text_answer2)
    TextView textAnswer2;
    @BindView(R.id.text_answer3)
    TextView textAnswer3;
    @BindView(R.id.text_comment_answer_remain)
    TextView textCommentAnswerRemain;
    @BindView(R.id.ll_comment_answer_root)
    LinearLayout llCommentAnswerRoot;
    @BindView(R.id.comment_divider_bottom)
    View commentDividerBottom;

    public PointHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        initView();
    }

    private void initView() {
        imageZan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_zan:
                imageZan.setImageResource(R.drawable.topic_like_icon);
                break;
        }
    }

    public void bind(PointBean point) {
        textUserName.setText(point.mName);
        textZanCount.setText(String.valueOf(point.mZanCount));//点赞数
        if (point.mSex == 1) {                               //性别
            imageUserSex.setImageResource(R.drawable.topic_female_icon);
        } else {
            imageUserSex.setImageResource(R.drawable.topic_male_icon);
        }
        //textUserConstellation.setText(ConsManager.CONS[point.mSigns]);//星座
        textVoteResult.setText(point.mViewPoint);//观点

    }
}
