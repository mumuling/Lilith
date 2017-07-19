package com.youloft.lilith.topic.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.StringUtil;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.adapter.TopicDetailAdapter;
import com.youloft.lilith.topic.bean.ClickLikeBean;
import com.youloft.lilith.topic.bean.ClickLikeEvent;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.db.PointAllCache;
import com.youloft.lilith.topic.db.PointAllTable;
import com.youloft.lilith.topic.db.PointAnswerCache;
import com.youloft.lilith.topic.db.PointAnswerTable;
import com.youloft.lilith.topic.db.PointCache;
import com.youloft.lilith.topic.db.PointTable;
import com.youloft.lilith.topic.db.TopicLikeCache;
import com.youloft.lilith.topic.db.TopicLikingTable;
import com.youloft.lilith.topic.widget.Rotate3dAnimation;
import com.youloft.lilith.glide.GlideCircleTransform;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 话题观点的holder
 * version
 *
 * @author slj
 * @time 2017/7/4 16:41
 * @class PointHolder
 */

public class PointHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.image_comment_user)
    ImageView imageCommentUser;//用户头像
    @BindView(R.id.text_user_name)
    TextView textUserName;//用户名字
    @BindView(R.id.image_user_sex)
    ImageView imageUserSex;//性别
    @BindView(R.id.text_user_constellation)
    TextView textUserConstellation;//星座
    @BindView(R.id.image_zan)
    ImageView imageZan;//赞
    @BindView(R.id.text_zan_count)
    TextView textZanCount;//点赞数
    @BindView(R.id.text_vote_result)
    TextView textVoteResult;//投票结果
    @BindView(R.id.text_comment_content)
    TextView textCommentContent;//观点
    @BindView(R.id.text_comment_time)
    TextView textCommentTime;//发表时间
    @BindView(R.id.text_comment_answer_count)
    TextView textCommentAnswerCount;//回复数量
    @BindView(R.id.text_answer1)
    TextView textAnswer1;//回复1
    @BindView(R.id.text_answer2)
    TextView textAnswer2;//回复2
    @BindView(R.id.text_answer3)
    TextView textAnswer3;//回复3
    @BindView(R.id.text_comment_answer_remain)
    TextView textCommentAnswerRemain;//全部回复
    @BindView(R.id.ll_comment_answer_root)
    LinearLayout llCommentAnswerRoot;
    @BindView(R.id.comment_divider_bottom)
    View commentDividerBottom;//底部分割线
    @BindView(R.id.ll_load_more)
    FrameLayout llLoadMore;//加载更多
    @BindView(R.id.text_load_more)
    TextView textLoadMore;
    @BindView(R.id.image_loading)
    ImageView imageLoading;//加载中图片
    private TextView[] replyTextArray = new TextView[3];
    private Animation loadAnimation;//加载动画
    private int isZan;//是否为赞的状态
    private PointBean.DataBean point;//观点
    private Context mContext;
    private TopicDetailAdapter adapter;
    private TopicDetailBean.DataBean topic;//话题信息
    private int totalPoint;//总的观点数
    private int zanCount = 0;//加上本地赞的数量


    public PointHolder(View itemView, TopicDetailAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
        loadAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate_animation);
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
            case R.id.ll_load_more:
                imageLoading.setVisibility(View.VISIBLE);
                textLoadMore.setVisibility(View.GONE);
                imageLoading.startAnimation(loadAnimation);
                loadMorePiont();
                break;
        }
    }

    /**
     * 加载更多观点
     */

    public void loadMorePiont() {
        TopicRepo.getPointList(String.valueOf(topic.id), null, "10", String.valueOf(totalPoint), false)
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<PointBean>() {
                    @Override
                    public void onDataSuccess(PointBean pointBean) {
                        if (pointBean.data == null || pointBean.data.size() == 0) {
                            textLoadMore.setVisibility(View.VISIBLE);
                            textLoadMore.setText(mContext.getResources().getString(R.string.no_more_data));
                            imageLoading.setVisibility(View.GONE);
                            imageLoading.clearAnimation();
                            return;
                        }
                       // handlePointTableInfo(pointBean.data);
                       // PointAllCache.getIns(mContext).addPointListToDb(pointBean.data,pointBean.t);
                        //removeRepeatPoint(pointBean.data,pointBean.t);
                        handleAnswerTable(pointBean.data,pointBean.t);
                        adapter.setPointBeanList(pointBean.data);
                        totalPoint = adapter.pointBeanList.size();
                        textLoadMore.setVisibility(View.VISIBLE);
                        textLoadMore.setText(mContext.getResources().getString(R.string.load_more));
                        imageLoading.setVisibility(View.GONE);
                        imageLoading.clearAnimation();
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        textLoadMore.setVisibility(View.VISIBLE);
                        textLoadMore.setText(mContext.getResources().getString(R.string.no_more_data));
                        imageLoading.setVisibility(View.GONE);
                        imageLoading.clearAnimation();

                    }
                });


    }
//
//    private void removeRepeatPoint(List<PointBean.DataBean> data, long t) {
//        if (data == null || data.size() == 0)return;
//        ArrayList<PointAllTable> pointAllTableList = PointAllCache.getIns(mContext).getPointListByPid(t);
//        PointAllCache pointAllCache = PointAllCache.getIns(mContext);
//        for (PointBean.DataBean point : data ) {
//            if (pointAllCache.pointIsExperid(point.id,t)) {
//                data.remove(point);
//            }
//        }
//
//    }

    /**
     *     加上本地缓存的回复数量
     * @param pointList   获得的观点列表
     */
    private void handleAnswerTable(List<PointBean.DataBean> pointList,long time) {
        if (pointList == null || pointList.size() == 0 )return;
        if (AppSetting.getUserInfo() == null)return;
        PointAnswerCache pointAnswerCache = PointAnswerCache.getIns(mContext);
        List<PointAnswerTable> pointAnswerTableList = null;
        for (int i = 0 ; i < pointList.size(); i ++) {
            int poitnID = pointList.get(i).id;
            int replyCount = pointList.get(i).reply;
            pointAnswerTableList = pointAnswerCache.getAnswerListByCode(poitnID);
            if (pointAnswerTableList == null){
                continue;
            }
            for (int j = 0; j < pointAnswerTableList.size(); j ++) {
                if (pointAnswerTableList.get(j).time > time) {
                    replyCount = replyCount + 1;
                    PointBean.DataBean.ReplyListBean replyBean = new PointBean.DataBean.ReplyListBean();
                    replyBean.nickName = AppSetting.getUserInfo().data.userInfo.nickName;
                    replyBean.contents = pointAnswerTableList.get(j).viewPoint;
                    pointList.get(i).replyList.add(0,replyBean);
                }
            }
            pointList.get(i).reply = replyCount;
        }
    }


    /**
     *
     * @param point  观点信息
     * @param option  观点选项
     * @param position  位置
     * @param isLast  是否是最后一个
     */
    public void bindNormal(final PointBean.DataBean point, final TopicDetailBean.DataBean option, final int position, boolean isLast) {
       if (point == null)return;
        UserBean  userInfo = AppSetting.getUserInfo();
        GlideApp.with(itemView)
                .asBitmap()
                .load(point.headImg)
                .dontAnimate()
                .transform(GlideCircleTransform.getInstance(LLApplication.getInstance()))
                .placeholder(R.drawable.default_user_head_img)
                .error(R.drawable.default_user_head_img)
                .override(30)
                .into(imageCommentUser);
        if ( option == null) return;

        this.topic = option;
        this.point = point;
        this.totalPoint = adapter.pointBeanList.size();
        ArrayList<TopicDetailBean.DataBean.OptionBean> topic = option.option;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/test/PointDetailActivity")
                        .withObject("point", point)
                        .withObject("topic", option)
                        .withInt("position",position)
                        .navigation();
            }
        });
        //头像



        //用户名字
        if (userInfo!= null && userInfo.data.userInfo.id == point.userId) {
            textUserName.setText(StringUtil.toNameString(userInfo.data.userInfo.nickName));
        } else {
            textUserName.setText(StringUtil.toNameString(point.nickName));
        }
        //点赞数
        bindZan(point);
        bindTime(point);

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
                textVoteResult.setText("投票给: " + optionBean.title);//投票
            }
        }
        if (point.topicOptionId % 2 == 1) {
            textVoteResult.setTextColor(Color.parseColor("#ff8282"));
        } else {
            textVoteResult.setTextColor(Color.parseColor("#5696df"));
        }
        //星座
        textUserConstellation.setText(ConsManager.getConsName(point.signs));
        //观点
        textCommentContent.setText(point.viewpoint);
        //是否有底部的加载更多
        if (isLast) {
            llLoadMore.setVisibility(View.VISIBLE);
            commentDividerBottom.setVisibility(View.GONE);
            if (position >= 10) {
                textLoadMore.setText(mContext.getResources().getString(R.string.load_more));
            } else {
                textLoadMore.setText(mContext.getResources().getString(R.string.no_more_data));
            }
        } else {
            llLoadMore.setVisibility(View.GONE);
            commentDividerBottom.setVisibility(View.VISIBLE);
        }
        if (point.reply == 0) {
            textCommentAnswerCount.setText(mContext.getResources().getString(R.string.reply));
        } else {
            textCommentAnswerCount.setText(String.valueOf(point.reply));
        }
        //用户评论，最多显示3条
        if (point.replyList != null && point.replyList.size() > 0) {
            llCommentAnswerRoot.setVisibility(View.VISIBLE);
            for (int i = 0; i < point.replyList.size(); i++) {
                if (i >= 3) break;
                PointBean.DataBean.ReplyListBean reply = point.replyList.get(i);
                if (reply != null) {
                    if (userInfo != null && userInfo.data.userInfo.id == reply.uid) {
                        replyTextArray[i].setText(StringUtil.toNameString(userInfo.data.userInfo.nickName) + ": " + reply.contents);
                    } else {
                        replyTextArray[i].setText(StringUtil.toNameString(reply.nickName) + ": " + reply.contents);
                    }
                    replyTextArray[i].setVisibility(View.VISIBLE);
                } else {
                    replyTextArray[i].setVisibility(View.GONE);
                }
            }
            if (point.replyList.size() > 3) {
                textCommentAnswerRemain.setText("查看全部" + point.reply + "条");
                textCommentAnswerRemain.setVisibility(View.VISIBLE);
            } else {
                textCommentAnswerRemain.setVisibility(View.GONE);
                for (int j = point.replyList.size(); j < 3; j++) {
                    replyTextArray[j].setVisibility(View.GONE);
                }
            }
        } else {
            llCommentAnswerRoot.setVisibility(View.GONE);
        }

    }

    /**   绑定时间显示
     *
     * @param point
     */
    private void bindTime(PointBean.DataBean point) {
        long time = CalendarHelper.getTimeMillisByString(point.buildDate);
        textCommentTime.setText(CalendarHelper.getInterValTime(time, System.currentTimeMillis()));
    }

    /**
     *     绑定赞
     * @param dataBean
     */
    private void bindZan(PointBean.DataBean dataBean) {

        PointBean.DataBean point = new PointBean.DataBean();

        int id = dataBean.id;
        TopicLikingTable table = TopicLikeCache.getIns(mContext).getInforByCode(id, PointDetailActivity.TYPE_POINT);
        zanCount = dataBean.zan;
        if (table == null) {
            isZan = dataBean.isclick;
        } else {
            isZan = table.mIsLike;
            if (table.mIsLike == dataBean.isclick) {
                TopicLikeCache.getIns(mContext).deleteData(id, PointDetailActivity.TYPE_POINT);
            } else {
                if (table.mIsPost != 1) clickLike();
                // dataBean.isclick = table.mIsLike;
                if (table.mIsLike == 1) {
                    zanCount = dataBean.zan + 1;
                }
            }
        }
        point.zan = dataBean.zan;
        if (isZan == 1) {
            imageZan.setImageResource(R.drawable.topic_liking_icon);
        } else {
            imageZan.setImageResource(R.drawable.topic_like_icon);
        }
        textZanCount.setText(String.valueOf(zanCount));
    }

    /**
     *   点赞
     */
    public void clickLike() {
        UserBean userBean = AppSetting.getUserInfo();
        if (userBean != null) {
            TopicRepo.likePoint(String.valueOf(point.id), String.valueOf(userBean.data.userInfo.id))
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

    /**
     *  跟新赞的数据库
     * @param ispost
     */
    public void updateClickTable(int ispost) {
        TopicLikingTable topicLikingTable;
        if (isZan == 0) {
            topicLikingTable = new TopicLikingTable(point.id, 0, PointDetailActivity.TYPE_POINT, ispost);
        } else {

            topicLikingTable = new TopicLikingTable(point.id, 1, PointDetailActivity.TYPE_POINT, ispost);

        }
        TopicLikeCache.getIns(itemView.getContext()).insertData(topicLikingTable);

        EventBus.getDefault().post(new ClickLikeEvent(ClickLikeEvent.TYPE_POINT));
    }
}
