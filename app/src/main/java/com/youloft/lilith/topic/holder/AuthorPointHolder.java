package com.youloft.lilith.topic.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.bean.ClickLikeBean;
import com.youloft.lilith.topic.bean.ClickLikeEvent;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.topic.db.TopicLikingTable;
import com.youloft.lilith.topic.widget.Rotate3dAnimation;
import com.youloft.lilith.glide.GlideCircleTransform;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    private int anthorId;
    private PointBean.DataBean point;
    private int zanCount = 0;

    public AuthorPointHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        imageZan.setOnClickListener(this);
        textZanCount.setOnClickListener(this);

    }


    public void bindView(PointBean.DataBean point, ArrayList<TopicDetailBean.DataBean.OptionBean> topic) {
        if (point == null || topic == null) return;
        this.point = point;
        anthorId = point.id;
        //头像
        GlideApp.with(itemView)
                .asBitmap().
                transform(new GlideCircleTransform())
                .load(point.headImg)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        GlideApp.with(itemView)
                                .asBitmap()
                                .transform(new GlideCircleTransform())
                                .load(R.drawable.calendar_work_icon)
                                .into(imageCommentUser);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
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
        for (int i = 0; i < topic.size(); i++) {
            TopicDetailBean.DataBean.OptionBean optionBean = topic.get(i);
            if (optionBean.id == point.topicOptionId) {
                textVoteResult.setText("投票给" + optionBean.title);//投票
            }
        }
        if (point.topicOptionId % 2 == 1) {
            textVoteResult.setTextColor(Color.parseColor("#ff8282"));
        } else {
            textVoteResult.setTextColor(Color.parseColor("#5696df"));
        }
        //星座
        textUserConstellation.setText(ConsManager.CONS_NAME[point.signs]);
        //观点
        textCommentContent.setText(point.viewpoint);
        //时间
        textCommentTime.setText(CalendarHelper.getInterValTime(CalendarHelper.getTimeMillisByString(point.buildDate), System.currentTimeMillis()));


    }

    private void bindZan(PointBean.DataBean dataBean) {
        int id = dataBean.id;
        zanCount = dataBean.zan;
        TopicLikingTable table = TopicLikeCache.getIns(mContext).getInforByCode(id, PointDetailActivity.TYPE_POINT);
        if (table == null) {
            isZan = dataBean.isclick;
        } else {
            isZan = table.mIsLike;
            if (table.mIsLike == dataBean.isclick) {
                TopicLikeCache.getIns(mContext).deleteData(id, PointDetailActivity.TYPE_POINT);
            } else {
                if (table.mIsPost != 1) clickLike();
                if (table.mIsLike == 1) {
                    zanCount++;
                }
            }
        }
        if (isZan == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textZanCount.setText(String.valueOf(zanCount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_zan:
            case R.id.text_zan_count:
                if (AppSetting.getUserInfo() == null) {
                    new LogInOrCompleteDialog(mContext).setStatus(LogInOrCompleteDialog.TOPIC_IN).show();
                    return;
                }
                ((BitmapDrawable) imageZan.getDrawable()).setAntiAlias(true);
                Rotate3dAnimation m3DAnimation;
                if (isZan == 1) {
                    m3DAnimation = new Rotate3dAnimation(0, 180,
                            imageZan.getWidth() / 2, imageZan.getHeight() / 2);
                    isZan = 0;
                } else {
                    m3DAnimation = new Rotate3dAnimation(180, 0,
                            imageZan.getWidth() / 2, imageZan.getHeight() / 2);
                    isZan = 1;
                }
                m3DAnimation.setDuration(300);
                imageZan.startAnimation(m3DAnimation);
                m3DAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (isZan == 0) {
                            imageZan.setImageResource(R.drawable.topic_like_icon);
                            zanCount--;
                            textZanCount.setText(String.valueOf(zanCount));
                        } else {
                            imageZan.setImageResource(R.drawable.topic_liking_icon);
                            zanCount++;
                            textZanCount.setText(String.valueOf(zanCount));
                        }
                        clickLike();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
        }
    }

    public void clickLike() {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo != null) {
            int userId = userInfo.data.userInfo.id;
            TopicRepo.likePoint(String.valueOf(point.id), String.valueOf(userId))
                    .subscribeOn(Schedulers.newThread())
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxObserver<ClickLikeBean>() {
                        @Override
                        public void onDataSuccess(ClickLikeBean s) {
                            if (s.data) {
                                updateClickTable(1);
                            } else {
                                updateClickTable(0);
                            }
                        }

                        @Override
                        protected void onFailed(Throwable e) {
                            super.onFailed(e);
                            updateClickTable(0);
                        }
                    });
        }
    }

    public void updateClickTable(int ispost) {
        TopicLikingTable topicLikingTable;
        if (isZan == 1) {

            topicLikingTable = new TopicLikingTable(anthorId, 1, PointDetailActivity.TYPE_POINT, ispost);

        } else {

            topicLikingTable = new TopicLikingTable(anthorId, 0, PointDetailActivity.TYPE_POINT, ispost);

        }
        TopicLikeCache.getIns(itemView.getContext()).insertData(topicLikingTable);
        EventBus.getDefault().post(new ClickLikeEvent(ClickLikeEvent.TYPE_AUTHOR));
    }

}
