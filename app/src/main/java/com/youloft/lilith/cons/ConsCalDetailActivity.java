package com.youloft.lilith.cons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jakewharton.rxbinding2.view.RxView;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.view.ConsCalendar;
import com.youloft.lilith.share.ShareBuilder;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.Subscribe;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by zchao on 2017/7/3.
 * desc:
 * version:
 */

public class ConsCalDetailActivity extends BaseActivity {
    private static final String TAG = "ConsCalDetailActivity";
    public static Bitmap mBg = null;
    @BindView(R.id.cons_detail_bg_img)
    ImageView mConsDetailBgImg;
    @BindView(R.id.cons_detail_title)
    TextView mConsDetailTitle;
    @BindView(R.id.cons_detail_cal_view)
    ConsCalendar mConsDetailCalView;
    @BindView(R.id.cons_detail_cons_love_tendency)
    TextView mConsDetailConsLoveTendency;
    @BindView(R.id.cons_detail_cons_money_tendency)
    TextView mConsDetailConsMoneyTendency;
    @BindView(R.id.cons_detail_cons_work_tendency)
    TextView mConsDetailConsWorkTendency;
    @BindView(R.id.cons_detail_share)
    TextView mConsDetailShare;
    @BindView(R.id.cons_detail_top)
    LinearLayout mConsDetailContentTop;
    @BindView(R.id.cons_detail_bottom)
    LinearLayout mConsDetailContentBottom;
    @BindView(R.id.cons_detail_close)
    ImageView mConsDetailClose;
    @BindView(R.id.cons_detail_content_root)
    LinearLayout mConsDetailContentRoot;
    @BindView(R.id.cons_detail_share_root)
    LinearLayout mConsShare;
    @BindView(R.id.root)
    FrameLayout mRoot;
    private int[] week_locals;
    private int distance;
    private ConsPredictsBean mData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cons_cal_detail_view_activity);
        ButterKnife.bind(this);
        week_locals = getIntent().getIntArrayExtra("week_local");
        mData = (ConsPredictsBean) getIntent().getSerializableExtra("bean");
        if (mBg != null) {
            mConsDetailBgImg.setImageBitmap(mBg);
        } else {
            mConsDetailBgImg.setBackgroundColor(getResources().getColor(R.color.cons_detail_bg_color));
        }

        if (mData != null) {
            bindData(mData);
        }
        AppAnalytics.onEvent("Monthfortunes", "IM");
        bindClick();
    }



    /**
     * 点击事件，使用RxBinding来做连点过滤
     */
    private void bindClick() {
        RxView.clicks(mConsShare)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        AppAnalytics.onEvent("Monthshare", "C");
                        share();
                    }
                });
        RxView.clicks(mConsDetailClose)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        AppAnalytics.onEvent("Monthcancel", "C");
                        openAnim(false, distance);
                    }
                });
    }

    /**
     * 绑定数据
     */
    private void bindData(ConsPredictsBean data) {
        if (data == null || data.data == null || data.data.predicts == null || data.data.predicts.isEmpty()) {
            doAnimator();
            return;
        }

        List<ConsPredictsBean.DataBean.PredictsBean> predicts = data.data.predicts;
        if (!predicts.isEmpty()) {
            ConsPredictsBean.DataBean.PredictsBean today = SafeUtil.getSafeData(predicts, 1);
            ConsPredictsBean.DataBean.PredictsBean dataEnd = SafeUtil.getSafeData(predicts, predicts.size() - 1);
            int todayMonth = -1;
            int nextMonth = -1;
            if (today != null) {
                todayMonth = getMonth(today.date) + 1; //index是从0开始的，所以+1
            }
            if (dataEnd != null) {
                nextMonth = getMonth(dataEnd.date) + 1;
            }
            String titleString = getResources().getString(R.string.cons_cal_title);
            if (nextMonth > 0 && todayMonth > 0) {
                if (nextMonth != todayMonth) {
                    mConsDetailTitle.setText(String.format("%s月-%s月" + titleString, String.valueOf(todayMonth), String.valueOf(nextMonth)));
                } else {
                    mConsDetailTitle.setText(String.format("%s月" + titleString, String.valueOf(todayMonth)));
                }
            } else if (todayMonth > 0) {
                mConsDetailTitle.setText(String.format("%s月" + titleString, String.valueOf(todayMonth)));
            } else {
                mConsDetailTitle.setText(titleString);
            }
            mConsDetailCalView.setData(data);

        }
        doAnimator();


    }

    /**
     * 执行动画
     */
    private void doAnimator() {
        //数据绑定完后才执行动画。防止出现闪现
        mRoot.post(new Runnable() {
            @Override
            public void run() {
                int[] contentroot = new int[2];
                mConsDetailContentRoot.getLocationOnScreen(contentroot);
                distance = week_locals[1] - contentroot[1];
                openAnim(true, distance);
            }
        });
    }

    private int getMonth(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return 0;
        }
        Date date = CalendarHelper.parseDate(dateString, ConsCalAdapter.dateFormatString);
        GregorianCalendar gcl = new GregorianCalendar();
        gcl.setTimeInMillis(date.getTime());
        return gcl.get(Calendar.MONTH);
    }

    /**
     * 进入退出动画
     *
     * @param in
     * @param dis
     */
    private void openAnim(final boolean in, int dis) {
        ObjectAnimator weekTran = ObjectAnimator.ofFloat(mConsDetailContentRoot, View.TRANSLATION_Y, in ? dis : 0, in ? 0 : dis);

        final LinearLayout.LayoutParams topLayoutParams = (LinearLayout.LayoutParams) mConsDetailContentTop.getLayoutParams();
        int topAnimEnd = mConsDetailContentTop.getHeight();
        ValueAnimator valueAnimator1 = ObjectAnimator.ofInt(in ? 0 : topAnimEnd, in ? topAnimEnd : 0);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                topLayoutParams.height = (int) animation.getAnimatedValue();
                mConsDetailContentTop.setLayoutParams(topLayoutParams);
            }
        });

        final LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mConsDetailContentBottom.getLayoutParams();
        final int bomAnimStart = (mConsDetailCalView.getChildAt(0).getHeight());
        final int bomAnimEnd = mConsDetailContentBottom.getHeight();

        ValueAnimator valueAnimator = ObjectAnimator.ofInt(in ? bomAnimStart : bomAnimEnd, in ? bomAnimEnd : bomAnimStart);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams1.height = (int) animation.getAnimatedValue();
                mConsDetailContentBottom.setLayoutParams(layoutParams1);
            }
        });

        ValueAnimator alphaAnim = ObjectAnimator.ofFloat(mConsDetailBgImg, View.ALPHA, in ? 0 : 1f, in ? 1f : 0);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(350);
        set.playTogether(valueAnimator, valueAnimator1, weekTran, alphaAnim);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!in) {
                    finish();
                }
            }
        });
        set.start();
    }

    /**
     * 分享周视图
     */
    private void share() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View shareView = inflater.inflate(R.layout.cons_detail_share_view, null); //分享出去的布局跟界面布局不一样，单独构造一个布局

        TextView mShareTitle = (TextView) shareView.findViewById(R.id.cons_detail_title_share);
        ConsCalendar mShareCal = (ConsCalendar) shareView.findViewById(R.id.cons_detail_cal_view_share);
        final ImageView mShareBg = (ImageView) shareView.findViewById(R.id.share_bg_img);
        mShareTitle.setText(mConsDetailTitle.getText().toString());

        if (mData != null
                && mData.data != null
                && mData.data.predicts != null
                && !mData.data.predicts.isEmpty()) {
            mShareCal.setData(mData);
            if (TextUtils.isEmpty(mData.data.bgImg)) {
                canShare(shareView);
                return;
            }
            //不能直接使用glide加载到imageView里边
            GlideApp.with(shareView).asBitmap().load(mData.data.bgImg).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    mShareBg.setImageBitmap(resource);
                    canShare(shareView);
                }
            });
        } else {
            //没有数据直接搞图
            canShare(shareView);
        }

    }

    private void canShare(View shareView) {
        shareView.measure(
                View.MeasureSpec.makeMeasureSpec(mConsDetailContentTop.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        shareView.layout(0, 0, shareView.getMeasuredWidth(),
                shareView.getMeasuredHeight());

        Bitmap b = Bitmap.createBitmap(shareView.getMeasuredWidth(), shareView.getMeasuredHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(b);
        shareView.draw(canvas);

        new ShareBuilder(ConsCalDetailActivity.this).withImg(b).share();
    }

    public static void startConsCalDetailActivity(Context context, int[] local, Bitmap bitmapByte, ConsPredictsBean bean) {
        Intent intent = new Intent(context, ConsCalDetailActivity.class);
        intent.putExtra("week_local", local);
        if (bean != null) {
            intent.putExtra("bean", bean);
        }
        mBg = bitmapByte;
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        mBg = null;
        overridePendingTransition(0, 0);
    }
}
