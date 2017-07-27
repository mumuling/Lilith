package com.youloft.lilith.topic.holder;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.glide.GlideBlurTransform;
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
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.topic.widget.VoteView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * version   投票的Holder
 *
 * @author slj
 * @time 2017/7/11 14:11
 * @class VoteHolder
 */


public class VoteHolder extends RecyclerView.ViewHolder {

    private ImageView imageTop;

    private TextView textTopicTitle;

    private VoteView voteView;

    private TopicDetailBean.DataBean topicInfo;
    private VoteDialog voteDialog;
    private ValueAnimator firstAnimation;
    private ValueAnimator secondAnimation;
    private ValueAnimator thirdAnimation;
    private boolean needVoteAnimation = true;
    private int isVote = 0;
    private TopicDetailAdapter adapter;
    private UserBean.DataBean.UserInfoBean userInfo;
    private String leftTitle;
    private String rightTitle;
    private boolean needImage = true;

    public VoteHolder(View itemView, TopicDetailAdapter adapter) {
        super(itemView);
        initView();
        this.adapter = adapter;
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

    /**
     * 投票完成的动画
     *
     * @param scale
     */
    private void voteAniamtion(final float scale) {
        firstAnimation.cancel();
        secondAnimation.cancel();
        thirdAnimation.cancel();
        ValueAnimator rectAnimater = new ValueAnimator();
        rectAnimater.setDuration(2000);
        rectAnimater.setFloatValues(0.0f, 1.0f);
        rectAnimater.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                voteView.setRectProportion(value, scale);


            }
        });
        rectAnimater.start();
    }

    private void initView() {
        voteDialog = new VoteDialog(itemView.getContext(), R.style.VoteDialog);
        voteDialog.setListener(new VoteDialog.OnClickConfirmListener() {
            @Override
            public void clickConfirm(final String msg, final int id, final String voteTitle) {
                if (topicInfo != null && topicInfo.isVote == 1) return;
                if (topicInfo == null) return;
                if (isVote == 1) return;
                UserBean userBean = AppSetting.getUserInfo();
                if (userBean == null) {
                    return;
                }
                userInfo = userBean.data.userInfo;

                TopicRepo.postVote(String.valueOf(topicInfo.id), String.valueOf(id), String.valueOf(userInfo.id), msg)
                        .subscribeOn(Schedulers.newThread())
                        .toObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxObserver<VoteBean>() {
                            @Override
                            public void onDataSuccess(VoteBean s) {
                                int poitnID = s.data;
                                if (poitnID != 0) {
                                    topicInfo.totalVote++;
                                    int votes = addOptionVote(id);
                                    if (id % 2 == 1) {
                                        voteAniamtion((float) votes / topicInfo.totalVote);
                                    } else {
                                        voteAniamtion(1 - ((float) votes / topicInfo.totalVote));
                                    }
                                    needVoteAnimation = false;
                                    String time = CalendarHelper.getNowTimeString();
                                    updatePointDb(id, topicInfo.id, poitnID, msg, time,s.t, topicInfo.title, voteTitle);
                                    isVote = 1;
                                    addToDb(topicInfo, id);
                                    Toaster.showShort("投票成功！");
                                }
                            }

                            @Override
                            protected void onFailed(Throwable e) {
                                Toaster.showShort("投票失败！");
                                super.onFailed(e);
                            }
                        });

            }
        });
        imageTop = (ImageView) itemView.findViewById(R.id.image_top);
        textTopicTitle = (TextView) itemView.findViewById(R.id.text_topic_title);
        voteView = (VoteView) itemView.findViewById(R.id.vote_view);
    }

    /**
     * 投票完成后更新本地观点的数据库
     *
     * @param oid        选择的ID
     * @param tid        话题的id
     * @param poitnID    生成的观点的id
     * @param msg        观点
     * @param time       时间
     * @param topicTitle 话题title
     * @param voteTitle  选择的title
     */
    private void updatePointDb(int oid, int tid, int poitnID, String msg, String buildDate,long time, String topicTitle, String voteTitle) {
        PointTable pointTable = new PointTable(oid, tid, poitnID, msg, buildDate,time, topicTitle, voteTitle);
        PointCache.getIns(itemView.getContext()).insertData(pointTable);
        UserBean.DataBean.UserInfoBean userInfo = AppSetting.getUserInfo().data.userInfo;
        PointBean.DataBean dataBean = new PointBean.DataBean();
        dataBean.userId = userInfo.id;
        dataBean.isclick = 0;
        dataBean.zan = 0;
        dataBean.buildDate = buildDate;
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

    /**
     * 更新数据库
     *
     * @param info 观点信息
     * @param id   观点id
     */
    private void addToDb(TopicDetailBean.DataBean info, int id) {
        TopicTable table = new TopicTable(info, id);
        TopicInfoCache.getIns(itemView.getContext()).insertData(table);

    }

    /**
     * 增加投票数
     *
     * @param id
     */
    private int addOptionVote(int id) {
        int vote = 0;
        if (topicInfo.option == null || topicInfo.option.size() == 0) return 0;
        for (int i = 0; i < topicInfo.option.size(); i++) {
            if (id == topicInfo.option.get(i).id) {
                topicInfo.option.get(i).vote++;
                return topicInfo.option.get(i).vote;
            }
        }
        return 0;
    }

    /**
     * 绑定holder
     *
     * @param topicInfo
     * @param backImage
     */
    public void bindView(final TopicDetailBean.DataBean topicInfo, String backImage) {
        if (TextUtils.isEmpty(backImage) && topicInfo != null) {
            backImage = topicInfo.backImg;
        }
        if (!TextUtils.isEmpty(backImage) && needImage) {
            GlideApp.with(itemView.getContext())

                    .asBitmap()
                    .priority(Priority.HIGH)
                    .load(backImage)
                    .signature(new ObjectKey("list:"+backImage))
                    .dontAnimate()
                    .transform(GlideBlurTransform.getInstance(LLApplication.getInstance()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(188,75)

            .into(imageTop);
            needImage =false;

        }
        if (topicInfo == null || topicInfo.option == null || topicInfo.option.size() == 0) return;
        this.topicInfo = topicInfo;
        for (int j = 0; j < topicInfo.option.size(); j++) {
            if (topicInfo.option.get(j).id % 2 == 1) {
                leftTitle = topicInfo.option.get(j).shortTitle;
            } else {
                rightTitle = topicInfo.option.get(j).shortTitle;
            }
        }
        voteView.setTitle(leftTitle, rightTitle);
        voteView.setInterface(new VoteView.OnItemClickListener() {
            @Override
            public void clickLeft() {
                if (topicInfo.isVote == 1 || isVote == 1) return;
                if (AppSetting.getUserInfo() == null) {
                    new LogInOrCompleteDialog(itemView.getContext()).setStatus(LogInOrCompleteDialog.TOPIC_IN).show();
                } else {
                    for (int i = 0; i < topicInfo.option.size(); i++) {
                        if (topicInfo.option.get(i).id % 2 == 1) {
                            voteDialog.show();
                            voteDialog.setTitle(topicInfo.option.get(i).shortTitle, topicInfo.option.get(i).title, topicInfo.option.get(i).id);
                            return;
                        }
                    }

                }
            }

            @Override
            public void clickRight() {
                if (topicInfo.isVote == 1 || isVote == 1) return;
                if (AppSetting.getUserInfo() == null) {
                    new LogInOrCompleteDialog(itemView.getContext()).setStatus(LogInOrCompleteDialog.TOPIC_IN).show();
                } else {
                    for (int i = 0; i < topicInfo.option.size(); i++) {
                        if (topicInfo.option.get(i).id % 2 == 0) {
                            voteDialog.show();
                            voteDialog.setTitle(topicInfo.option.get(i).shortTitle, topicInfo.option.get(i).title, topicInfo.option.get(i).id);
                            return;
                        }
                    }
                }
            }
        });
        if (topicInfo.isVote == 1 && needVoteAnimation) {
            for (int j = 0; j < topicInfo.option.size(); j++) {
                if (topicInfo.option.get(j).id % 2 == 1) {
                    voteAniamtion((float) topicInfo.option.get(j).vote / topicInfo.totalVote);
                } else {
                    voteAniamtion(1 - ((float) topicInfo.option.get(j).vote / topicInfo.totalVote));
                }
            }
            needVoteAnimation = false;
        }

        textTopicTitle.setText(topicInfo.title);
    }
}
