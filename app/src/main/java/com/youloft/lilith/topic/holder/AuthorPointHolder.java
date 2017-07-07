package com.youloft.lilith.topic.holder;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.topic.db.TopicLikingTable;
import com.youloft.lilith.topic.widget.Rotate3dAnimation;
import com.youloft.lilith.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者观点的holder
 * version
 *
 * @author slj
 * @time 2017/7/6 20:29
 * @class AuthorPointHolder
 */

public class AuthorPointHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
    @BindView(R.id.comment_divider_bottom)
    View commentDividerBottom;

    private TextView textCommentHot;
    private int isZan;
    private Context mContext;
    private int zanCount;
    private int anthorId;

    public AuthorPointHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        mContext = itemView.getContext();
        imageZan.setOnClickListener(this);
        textZanCount.setOnClickListener(this);

    }



    public void bindView(PointBean.DataBean point, ArrayList<TopicDetailBean.DataBean.OptionBean> topic) {
        if (point == null || topic == null )return;
        anthorId = point.id;
        //头像
        GlideApp.with(itemView)
                .asBitmap().
                transform(new GlideCircleTransform(itemView.getContext()))
                .load(point.headImg)
                .into(imageCommentUser);
        //用户名字
        textUserName.setText(point.nickName);
        //点赞数
        bindZan(point);

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



    }

    private void bindZan(PointBean.DataBean dataBean) {
        int id = dataBean.id;
        TopicLikingTable table = TopicLikeCache.getIns(mContext).getInforByCode(id,PointDetailActivity.TYPE_POINT);
        if (table == null) {
            isZan = dataBean.isclick;
        } else {
            isZan = table.mIsLike;
            if (table.mIsLike == dataBean.isclick) {
                TopicLikeCache.getIns(mContext).deleteData(id,PointDetailActivity.TYPE_POINT);
            } else if (table.mIsLike == 1 && dataBean.zan == 0) {
                dataBean.zan++;
            }
        }
        zanCount = dataBean.zan;
        if (isZan == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textZanCount.setText(String.valueOf(dataBean.zan));
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
                            zanCount--;
                            textZanCount.setText(String.valueOf(zanCount));
                            topicLikingTable = new TopicLikingTable(anthorId,0, PointDetailActivity.TYPE_POINT);
                            isZan =0;
                        } else {
                            imageZan.setImageResource(R.drawable.topic_liking_icon);
                            zanCount++;
                            textZanCount.setText(String.valueOf(zanCount));
                            topicLikingTable = new TopicLikingTable(anthorId,1,PointDetailActivity.TYPE_POINT);
                            isZan = 1;
                        }
                        TopicLikeCache.getIns(itemView.getContext()).insertData(topicLikingTable);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
        }
    }
}
