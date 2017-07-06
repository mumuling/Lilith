package com.youloft.lilith.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.ui.GlideCircleTransform;

import java.util.List;

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
    @BindView(R.id.ll_load_more)
    FrameLayout llLoadMore;
    private TextView[] replyTextArray = new TextView[3];

    public PointHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        initView();
    }

    private void initView() {
        replyTextArray[0] = textAnswer1;
        replyTextArray[1] = textAnswer2;
        replyTextArray[2] = textAnswer3;
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

    public void bindNormal(PointBean.DataBean point, List<TopicDetailBean.DataBean.OptionBean> topic,boolean isLast) {
        if (point == null || topic == null)return;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/test/PointDetailActivity").navigation();
            }
        });
        //头像
        GlideApp.with(itemView)
                .asBitmap().
                transform(new GlideCircleTransform(itemView.getContext()))
                .load(point.headImg)
                .into(imageCommentUser);
        //用户名字
        textUserName.setText(point.nickName);
        //点赞数
        textZanCount.setText(String.valueOf(point.zan));
        //性别
        if (point.sex == 1) {
            imageUserSex.setImageResource(R.drawable.topic_female_icon);
        } else {
            imageUserSex.setImageResource(R.drawable.topic_male_icon);
        }
        //支持的观点
        for (int i = 0; i < topic.size();i ++) {
            TopicDetailBean.DataBean.OptionBean optionBean = topic.get(i);
            if (optionBean.id == point.topicOptionId) {
                textVoteResult.setText("投票给" + optionBean.title);//投票
            }
        }
        //星座
        textUserConstellation.setText(ConsManager.CONS_NAME[point.signs]);
        //观点
        textCommentContent.setText( point.viewpoint);
        //是否有底部的加载更多
        if (isLast) {
            llLoadMore.setVisibility(View.VISIBLE);
            commentDividerBottom.setVisibility(View.GONE);
        } else {
            llLoadMore.setVisibility(View.GONE);
            commentDividerBottom.setVisibility(View.VISIBLE);
        }
        textCommentAnswerCount.setText(String.valueOf(point.reply));
        //用户评论，最多显示3条
        if (point.replyList!= null && point.replyList.size() > 0) {
            for (int i = 0 ; i < point.replyList.size();i++) {
                if (i >= 3)return;
                PointBean.DataBean.ReplyListBean reply = point.replyList.get(i);
                if (reply != null) {
                    replyTextArray[i].setText(reply.nickName + ": " + reply.contents);
                } else {
                    replyTextArray[i].setVisibility(View.GONE);
                }
            }
            if (point.replyList.size() >= 3) {
                textCommentAnswerRemain.setText("查看全部" + point.reply + "条");
            } else {
                textCommentAnswerRemain.setVisibility(View.GONE);
                for (int j = point.replyList.size(); j < 3 ;j ++) {
                    replyTextArray[j].setVisibility(View.GONE);
                }
            }
        } else {
            llCommentAnswerRoot.setVisibility(View.GONE);
        }

    }


}
