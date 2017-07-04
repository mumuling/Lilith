package com.youloft.lilith.cons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.view.ConsCalItemDecoration;
import com.youloft.lilith.cons.view.ConsCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/3.
 * desc:
 * version:
 */

public class ConsCalDetailActivity extends BaseActivity {
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
    @BindView(R.id.root)
    FrameLayout mRoot;
    private int[] week_locals;
    private int distance;

    /**
     * 关闭
     */
    @OnClick(R.id.cons_detail_close)
    public void close() {
        openAnim(false, distance);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cons_cal_detail_view_activity);
        ButterKnife.bind(this);
        week_locals = getIntent().getIntArrayExtra("week_local");

//        GlideApp.with(this).load(mBg).transition(new Tra)
        if (mBg != null) {
            mConsDetailBgImg.setImageBitmap(mBg);
        } else {
            mConsDetailBgImg.setBackgroundColor(Color.BLACK);
        }
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

    /**
     * 进入退出动画
     * @param in
     * @param dis
     */
    private void openAnim(final boolean in, int dis) {
        ObjectAnimator weekTran = ObjectAnimator.ofFloat(mConsDetailContentRoot, View.TRANSLATION_Y, in?dis:0, in?0:dis);

        final LinearLayout.LayoutParams topLayoutParams = (LinearLayout.LayoutParams) mConsDetailContentTop.getLayoutParams();
        int topAnimEnd = mConsDetailContentTop.getHeight();
        ValueAnimator valueAnimator1 = ObjectAnimator.ofInt(in?0:topAnimEnd, in?topAnimEnd:0);
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

        ValueAnimator valueAnimator = ObjectAnimator.ofInt(in?bomAnimStart:bomAnimEnd, in?bomAnimEnd:bomAnimStart);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams1.height = (int) animation.getAnimatedValue();
                mConsDetailContentBottom.setLayoutParams(layoutParams1);
            }
        });

        ValueAnimator alphaAnim = ObjectAnimator.ofFloat(mConsDetailBgImg, View.ALPHA, in?0:0.6f, in?0.6f:0);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
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


    public static void startConsCalDetailActivity(Context context, int[] local, Bitmap bitmap) {
        Intent intent = new Intent(context, ConsCalDetailActivity.class);
        intent.putExtra("week_local", local);
//        mBg = bitmap;
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        mBg = null;
        overridePendingTransition(0, 0);
    }
}
