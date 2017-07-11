package com.youloft.lilith.topic.holder;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.net.AbsResponse;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.bean.VoteBean;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.PointTable;
import com.youloft.lilith.topic.db.TopicInfoCache;
import com.youloft.lilith.topic.db.TopicTable;
import com.youloft.lilith.topic.widget.BlurFactor;
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.topic.widget.VoteView;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */


public class VoteHolder extends RecyclerView.ViewHolder {

    private ImageView imageTop;

    private TextView textTopicTitle;

    private VoteView voteView;

    private TopicDetailBean.DataBean topicInfo;
    private VoteDialog voteDialog ;
    private ValueAnimator firstAnimation;
    private ValueAnimator secondAnimation;
    private ValueAnimator thirdAnimation;
    private boolean needVoteAnimation = true;
    private int isVote = 0;
    private TopicDetailAdapter adapter;
    private UserBean.DataBean.UserInfoBean userInfo;

    public VoteHolder(View itemView,TopicDetailAdapter adapter) {
        super(itemView);
        initView();
        this.adapter =  adapter;
        firstAnimation = new ValueAnimator();
        firstAnimation.setFloatValues(0.0f, 1.0f);
        firstAnimation.setDuration(4000);
        firstAnimation.setRepeatMode(ValueAnimator.RESTART);
        firstAnimation.setRepeatCount(ValueAnimator.INFINITE);
        firstAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue() * 2;
                if (value > 1.0) value = 1.0f;
                int changeWidth = (int) ViewUtil.dp2px(12);
                voteView.setChangeValue1(changeWidth * value * 2, (int) (255 * (1 - value)));
            }
        });

         secondAnimation = new ValueAnimator();
        secondAnimation.setFloatValues(0.0f, 1.0f);
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
                voteView.setChangeValue2(changeWidth * value * 2, (int) (255 * (1 - value)));
            }
        });
         thirdAnimation = new ValueAnimator();
        thirdAnimation.setFloatValues(0.0f, 1.0f);
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
                voteView.setChangeValue3(changeWidth * value * 2, (int) (255 * (1 - value)));
            }
        });
        firstAnimation.start();
        secondAnimation.start();
        thirdAnimation.start();


    }

    private void voteAniamtion(final float scale) {
        firstAnimation.cancel();
        secondAnimation.cancel();
        thirdAnimation.cancel();
        ValueAnimator rectAnimater = new ValueAnimator();
        rectAnimater.setDuration(2000);
        rectAnimater.setFloatValues(0.0f,1.0f);
        rectAnimater.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                voteView.setRectProportion(value,scale);


            }
        });
        rectAnimater.start();
    }

    private void initView() {
        voteDialog = new VoteDialog(itemView.getContext());
        voteDialog.setListener(new VoteDialog.OnClickConfirmListener() {
            @Override
            public void clickConfirm(final String msg, final int id,final String voteTitle) {
                if (topicInfo !=null && topicInfo.isVote ==1)return;
                if (topicInfo == null) return;
                if (isVote == 1 )return;
                userInfo = AppSetting.getUserInfo().data.userInfo;
                if (userInfo == null)return;

                TopicRepo.postVote(String.valueOf(topicInfo.id),String.valueOf(id),String.valueOf(userInfo.id),msg)
                        .subscribeOn(Schedulers.newThread())
                        .toObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxObserver<VoteBean>() {
                            @Override
                            public void onDataSuccess(VoteBean s) {
                                int poitnID =  s.data;
                                if (poitnID!= -1) {
                                    String time = CalendarHelper.getNowTimeString();
                                    updatePointDb(id,topicInfo.id,poitnID,msg,time,topicInfo.title,voteTitle);
                                    isVote = 1;
                                    topicInfo.totalVote++;
                                    addOptionVote(id);
                                    voteAniamtion((float) topicInfo.option.get(0).vote/topicInfo.totalVote);
                                    needVoteAnimation = false;
                                    addToDb(topicInfo,id);
                                }
                            }

                            @Override
                            protected void onFailed(Throwable e) {

                                super.onFailed(e);
                            }
                        });

            }
        });
        imageTop = (ImageView) itemView.findViewById(R.id.image_top);
        textTopicTitle = (TextView) itemView.findViewById(R.id.text_topic_title);
        voteView = (VoteView) itemView.findViewById(R.id.vote_view);
    }

    private void updatePointDb(int oid ,int tid, int poitnID, String msg,String time,String topicTitle,String voteTitle) {
        PointTable pointTable = new PointTable(oid,tid,poitnID,msg,time,topicTitle,voteTitle);
        PointCache.getIns(itemView.getContext()).insertData(pointTable);
        UserBean.DataBean.UserInfoBean userInfo = AppSetting.getUserInfo().data.userInfo;
        PointBean.DataBean dataBean = new PointBean.DataBean();
        dataBean.userId =  userInfo.id;
        dataBean.isclick = 0;
        dataBean.zan = 0;
        dataBean.buildDate = time;
        dataBean.nickName = userInfo.nickName;
        dataBean.reply = 0;
        dataBean.headImg = userInfo.headImg;
        dataBean.replyList = new ArrayList<>();
        dataBean.sex = userInfo.sex;
        dataBean.signs = userInfo.signs;
        dataBean.topicOptionId = oid;
        dataBean.topicId = tid;
        dataBean.viewpoint = msg;
        dataBean.id = poitnID;
        adapter.setPointOnFirst(dataBean);

    }

    private void addToDb(TopicDetailBean.DataBean info,int id) {
        TopicTable table = new TopicTable(info,id);
        TopicInfoCache.getIns(itemView.getContext()).insertData(table);

    }
    private void addOptionVote(int id) {
        if (topicInfo.option == null || topicInfo.option.size() == 0 )return;
        for (int i =0; i < topicInfo.option.size();i ++) {
            if (id == topicInfo.option.get(i).id) {
                topicInfo.option.get(i).vote++;
            }
        }
    }
    public void bindView(final TopicDetailBean.DataBean topicInfo) {
        if (topicInfo == null || topicInfo.option == null )return;
        this.topicInfo = topicInfo;
        voteView.setInterface(new VoteView.OnItemClickListener() {
            @Override
            public void clickLeft() {
                if (topicInfo.isVote == 1|| isVote == 1)return;
                if (!AppConfig.LOGIN_STATUS) {
                    new LogInOrCompleteDialog(itemView.getContext()).show();
                } else {
                    voteDialog.show();
                    voteDialog.setTitle(topicInfo.option.get(0).shortTitle, topicInfo.option.get(0).id);
                }
            }

            @Override
            public void clickRight() {
                if (topicInfo.isVote == 1|| isVote == 1)return;
                if (!AppConfig.LOGIN_STATUS) {
                    new LogInOrCompleteDialog(itemView.getContext()).show();
                } else {
                    voteDialog.show();
                    voteDialog.setTitle(topicInfo.option.get(1).shortTitle, topicInfo.option.get(1).id);
                }
            }
        });
        if (topicInfo.isVote ==1 && needVoteAnimation) {
            voteAniamtion((float) topicInfo.option.get(0).vote/topicInfo.totalVote);
            needVoteAnimation = false;
        }
        GlideApp.with(itemView.getContext())
                .asBitmap()
                .load(topicInfo.backImg)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        BlurFactor factor = new BlurFactor();
                        factor.width = resource.getWidth();
                        factor.height = resource.getHeight();
                        factor.radius = 5;
                        resource = factor.of(itemView.getContext(), resource, factor);
                        //resource = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.back_icon);
                        imageTop.setImageBitmap(resource);
                        return false;
                    }
                })
                .into(ViewUtil.getScreenWidth(itemView.getContext()),(int) ViewUtil.dp2px(150));
        textTopicTitle.setText("#" + topicInfo.title);
    }


}
