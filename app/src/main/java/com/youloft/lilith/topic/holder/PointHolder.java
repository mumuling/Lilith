package com.youloft.lilith.topic.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.TopicDetailActivity;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    @BindView(R.id.text_load_more)
    TextView textLoadMore;
    @BindView(R.id.image_loading)
    ImageView imageLoading;
    private TextView[] replyTextArray = new TextView[3];
    private Animation loadAnimation;
    private int isZan;
    private PointBean.DataBean point;
    private Context mContext;
    private TopicDetailAdapter adapter;
    private  int totalPoint;

    public PointHolder(View itemView, TopicDetailAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.adapter = adapter;
        this.mContext = itemView.getContext();
        initView();
    }

    private void initView() {
        replyTextArray[0] = textAnswer1;
        replyTextArray[1] = textAnswer2;
        replyTextArray[2] = textAnswer3;
        imageZan.setOnClickListener(this);
        textZanCount.setOnClickListener(this);
        llLoadMore.setOnClickListener(this);
        loadAnimation = AnimationUtils.loadAnimation(itemView.getContext(),R.anim.rotate_animation);
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
                            point.zan--;
                            textZanCount.setText(String.valueOf(point.zan));
                            topicLikingTable = new TopicLikingTable(point.id,0, PointDetailActivity.TYPE_POINT);
                            isZan =0;
                        } else {
                            imageZan.setImageResource(R.drawable.topic_liking_icon);
                            point.zan++;
                            textZanCount.setText(String.valueOf(point.zan));
                            topicLikingTable = new TopicLikingTable(point.id,1,PointDetailActivity.TYPE_POINT);
                            isZan = 1;
                        }
                        TopicLikeCache.getIns(itemView.getContext()).insertData(topicLikingTable);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case R.id.ll_load_more:
                imageLoading.setVisibility(View.VISIBLE);
                textLoadMore.setVisibility(View.GONE);
                imageLoading.startAnimation(loadAnimation);
                loadMorePiont();
                break;
        }
    }
    public void getMorePiont() {
        if (itemView.getContext() instanceof TopicDetailActivity) {
           if (((TopicDetailActivity) itemView.getContext()).loadMorePiont()){
               textLoadMore.setVisibility(View.VISIBLE);
               textLoadMore.setText("展开更多");
               imageLoading.setVisibility(View.GONE);
               imageLoading.clearAnimation();
           } else {
               textLoadMore.setVisibility(View.VISIBLE);
               textLoadMore.setText("没有数据了..");
               imageLoading.setVisibility(View.GONE);
               imageLoading.clearAnimation();

           }
        }
    }
    /**
     *   加载更多观点
     */

    public void loadMorePiont() {

        TopicRepo.getPointList(String.valueOf(point.id),null,"1","",false)
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<PointBean>() {
                    @Override
                    public void onDataSuccess(PointBean pointBean) {
                        if (pointBean.data == null || pointBean.data.size() == 0 ) {
                            textLoadMore.setVisibility(View.VISIBLE);
                            textLoadMore.setText("没有数据了..");
                            imageLoading.setVisibility(View.GONE);
                            imageLoading.clearAnimation();
                            return;
                        }
                        adapter.setPointBeanList(pointBean.data);
                        totalPoint = adapter.pointBeanList.size();
                        textLoadMore.setVisibility(View.VISIBLE);
                        textLoadMore.setText("展开更多");
                        imageLoading.setVisibility(View.GONE);
                        imageLoading.clearAnimation();
                    }

                    @Override
                    protected void onFailed(Throwable e) {

                        super.onFailed(e);
                        textLoadMore.setVisibility(View.VISIBLE);
                        textLoadMore.setText("没有数据了..");
                        imageLoading.setVisibility(View.GONE);
                        imageLoading.clearAnimation();

                    }
                });


    }

    public void bindNormal(final PointBean.DataBean point, final TopicDetailBean.DataBean option, boolean isLast) {
        if (point == null || option == null)return;
        this.point = point;
        ArrayList<TopicDetailBean.DataBean.OptionBean> topic = option.option;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/test/PointDetailActivity")
                        .withObject("point",point)
                        .withObject("topic",option)
                .navigation();
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
        point.zan = dataBean.zan;
        if (isZan == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textZanCount.setText(String.valueOf(dataBean.zan));
    }



}
