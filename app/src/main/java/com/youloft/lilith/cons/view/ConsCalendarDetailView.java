package com.youloft.lilith.cons.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.ConsCalAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/1.
 * desc:
 * version:
 */

public class ConsCalendarDetailView extends FrameLayout {
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

    @OnClick(R.id.cons_detail_close)
    public void close(){
        setVisibility(GONE);
    }

    public ConsCalendarDetailView(Context context) {
        this(context, null);
    }

    public ConsCalendarDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.cons_cal_detail_view, this, true);
        ButterKnife.bind(this, view);
    }

    public void show(){
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
                openAnim(true);
            }
        });
    }
    private void openAnim(boolean in) {
        ObjectAnimator weekTran = ObjectAnimator.ofFloat(mConsDetailContentRoot, View.TRANSLATION_Y, 1000, 0);

        final LinearLayout.LayoutParams topLayoutParams = (LinearLayout.LayoutParams) mConsDetailContentTop.getLayoutParams();
        int topAnimEnd = mConsDetailContentTop.getHeight();
        ValueAnimator valueAnimator1 = ObjectAnimator.ofInt(0, topAnimEnd);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                topLayoutParams.height = (int)animation.getAnimatedValue();
                mConsDetailContentTop.setLayoutParams(topLayoutParams);
            }
        });

        final LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mConsDetailContentBottom.getLayoutParams();
        final int bomAnimStart = (mConsDetailCalView.getChildAt(0).getHeight());
        final int bomAnimEnd = mConsDetailContentBottom.getHeight();

        ValueAnimator valueAnimator = ObjectAnimator.ofInt(bomAnimStart, bomAnimEnd);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams1.height = (int) animation.getAnimatedValue();
                mConsDetailContentBottom.setLayoutParams(layoutParams1);
            }
        });

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(valueAnimator, valueAnimator1, weekTran);
        set.start();
    }


//    private void iniCal() {
//        ConsCalAdapter adapter = new ConsCalAdapter(getContext());
//        mConsDetailCalView.setLayoutManager(new GridLayoutManager(getContext(), 7));
//        mConsDetailCalView.addItemDecoration(new ConsCalItemDecoration(getContext()));
//        mConsDetailCalView.setAdapter(adapter);
//        adapter.setCalType(ConsCalAdapter.CONS_CAL_TYPE_MONTH);
//    }
}
