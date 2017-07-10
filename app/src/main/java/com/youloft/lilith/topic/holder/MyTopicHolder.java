package com.youloft.lilith.topic.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.bean.MyTopicBean;
import com.youloft.lilith.ui.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我参与的话题的Holder
 * version
 *
 * @author slj
 * @time 2017/7/10 16:39
 * @class MyTopicHolder
 */

public class MyTopicHolder extends RecyclerView.ViewHolder {
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
    @BindView(R.id.text_topic_title)
    TextView textTopicTitle;
    @BindView(R.id.text_comment_time)
    TextView textCommentTime;
    @BindView(R.id.text_comment_answer_count)
    TextView textCommentAnswerCount;
    @BindView(R.id.comment_divider_bottom)
    View commentDividerBottom;
    private Context mContext;
    private UserBean.DataBean.UserInfoBean userInfo;
    public MyTopicHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
        mContext = itemView.getContext();

    }

    public void bind(MyTopicBean.DataBean point) {
        userInfo = AppSetting.getUserInfo().data.userInfo;
        if (userInfo == null)return;
        //头像
        GlideApp.with(mContext)
                .asBitmap().transform(new GlideCircleTransform(mContext))
                .load(userInfo.headImg)
                .into(imageCommentUser);
        //昵称
        textUserName.setText(userInfo.nickName);
        //星座
        textUserConstellation.setText(ConsManager.CONS_NAME[userInfo.signs]);
        //性别
        if (userInfo.sex == 2) {
            imageUserSex.setImageResource(R.drawable.topic_male_icon);
        } else {
            imageUserSex.setImageResource(R.drawable.topic_female_icon);
        }
        //点赞数
        textZanCount.setText(String.valueOf(point.zan));
        //点赞状态
        if (point.isclick == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textVoteResult.setText("投票给：" + point.optionTitle);
        textTopicTitle.setText("#" + point.topicIdTitle);
        textCommentTime.setText(CalendarHelper.getInterValTime(CalendarHelper.getTimeMillisByString(point.date),System.currentTimeMillis()));
        textCommentAnswerCount.setText(point.reply);
    }

    @OnClick({R.id.text_topic_title, R.id.text_comment_time, R.id.text_comment_answer_count})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_topic_title:
                break;
            case R.id.text_comment_time:
                break;
            case R.id.text_comment_answer_count:
                break;
        }
    }
}
