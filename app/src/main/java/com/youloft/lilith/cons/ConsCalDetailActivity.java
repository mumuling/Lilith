package com.youloft.lilith.cons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.picker.CityInfo;
import com.youloft.lilith.common.widgets.picker.CityPicker;
import com.youloft.lilith.common.widgets.picker.DatePicker;
import com.youloft.lilith.common.widgets.picker.DatePickerPop;
import com.youloft.lilith.common.widgets.picker.GenderPickerPop;
import com.youloft.lilith.common.widgets.picker.OnPickerSelectListener;
import com.youloft.lilith.common.widgets.picker.TimePickerPop;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.cons.view.ConsCalendar;
import com.youloft.lilith.share.ShareBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.LogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    @BindView(R.id.root)
    FrameLayout mRoot;
    @BindView(R.id.share_content)
    FrameLayout mShareContent;
    private int[] week_locals;
    private int distance;
    private ConsPredictsBean mData;
    private ConsPredictsBean data;

    /**
     * 关闭
     */
    @OnClick(R.id.cons_detail_close)
    public void close() {
        openAnim(false, distance);
    }

    /**
     * 关闭
     */
    @OnClick(R.id.cons_detail_share_root)
    public void shareCons() {
        share();
    }

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
        } else {
            ConsRepo.getConsPredicts("1989-11-11", "", "29.35", "106.33")
                    .subscribeOn(Schedulers.newThread())
                    .toObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxObserver<ConsPredictsBean>() {
                        @Override
                        public void onDataSuccess(ConsPredictsBean bean) {
                            if (bean != null) {
                                mData = bean;
                                bindData(mData);
                            }
                            Log.d(TAG, "onDataSuccess() called with: bean = [" + bean + "]");
                        }
                    });
        }


    }

    /**
     * 绑定数据
     */
    private void bindData(ConsPredictsBean data) {
        if (data == null || data.data == null || data.data.predicts == null) {
            return;
        }

        this.data = data;

        List<ConsPredictsBean.DataBean.PredictsBean> predicts = data.data.predicts;
        if (!predicts.isEmpty()) {
            ConsPredictsBean.DataBean.PredictsBean today = SafeUtil.getSafeData(predicts, 1);
            ConsPredictsBean.DataBean.PredictsBean dataEnd = SafeUtil.getSafeData(predicts, predicts.size() - 1);
            int todayMonth = -1;
            int nextMonth = -1;
            if (today != null) {
                todayMonth = getMonth(today.date);
            }
            if (dataEnd != null) {
                nextMonth = getMonth(dataEnd.date);
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

    private void share() {
        CityPicker.getDefCityPicker(this).setOnCityItemClickListener(new OnPickerSelectListener<CityInfo>() {
            @Override
            public void onSelected(CityInfo data) {
                Log.d(TAG, "onSelected: " + data.pProvice + data.pCity + data.pDistrict);
            }

            @Override
            public void onCancel() {

            }
        }).show();
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View shareview = inflater.inflate(R.layout.cons_detail_share_view, null);
//        mShareContent.addView(shareview);
//
//        TextView mShareTitle = (TextView) shareview.findViewById(R.id.cons_detail_title_share);
//        ConsCalendar mShareCal = (ConsCalendar) shareview.findViewById(R.id.cons_detail_cal_view_share);
//        mShareTitle.setText(mConsDetailTitle.getText().toString());
//        mShareCal.setData(data);
//
//        mShareContent.setDrawingCacheEnabled(true);
//        Bitmap drawingCache = mShareContent.getDrawingCache();
////        mShareContent.setDrawingCacheEnabled(false);
//        if (drawingCache != null && !drawingCache.isRecycled()) {
//            new ShareBuilder(this).withImg(drawingCache).share();
//        }
//        Bitmap b = Bitmap.createBitmap((int)ViewUtil.dp2px(355),(int)ViewUtil.dp2px(432),Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(b);
//        shareview.draw(canvas);

//        share1();
    }

    private void share1() {
        mConsDetailCalView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = mConsDetailCalView.getDrawingCache();
        if (drawingCache != null && !drawingCache.isRecycled()) {
            int height = drawingCache.getHeight();
            int width = drawingCache.getWidth();
            Paint paint = new Paint();
            Bitmap shareBit = Bitmap.createBitmap(width, height + (int) ViewUtil.dp2px(167), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(shareBit);
            canvas.drawColor(Color.YELLOW);
            ViewUtil.renderTextByCenter(canvas, mConsDetailTitle.getText().toString(), width / 2, ViewUtil.dp2px(23.5f), mConsDetailTitle.getPaint());
            canvas.drawBitmap(drawingCache, 0, ViewUtil.dp2px(47), paint);

            Drawable[] loveDrawables = mConsDetailConsLoveTendency.getCompoundDrawables();
            Drawable[] moneyDrawables = mConsDetailConsMoneyTendency.getCompoundDrawables();
            Drawable[] workDrawables = mConsDetailConsWorkTendency.getCompoundDrawables();
            int v = (int) ViewUtil.dp2px(15);
            int wordY = (int) ViewUtil.dp2px(10);
            int v1 = (int) ViewUtil.dp2px(327);
            int v2 = (int) ViewUtil.dp2px(20);
            int space = (int) ViewUtil.dp2px(35);
            int word = (int) ViewUtil.dp2px(88);
            loveDrawables[0].setBounds(v, v1, v + v2, v1 + v2);
            moneyDrawables[0].setBounds(v, v1 + space, v + v2, v1 + +space + v2);
            workDrawables[0].setBounds(v, v1 + space + space, v + v2, v1 + space + space + v2);

            loveDrawables[0].draw(canvas);
            moneyDrawables[0].draw(canvas);
            workDrawables[0].draw(canvas);


            ViewUtil.renderTextByCenter(canvas, mConsDetailConsLoveTendency.getText().toString(), word, v1 + wordY, mConsDetailConsLoveTendency.getPaint());
            ViewUtil.renderTextByCenter(canvas, mConsDetailConsMoneyTendency.getText().toString(), word, v1 + wordY + space, mConsDetailConsMoneyTendency.getPaint());
            ViewUtil.renderTextByCenter(canvas, mConsDetailConsWorkTendency.getText().toString(), word, v1 + wordY + space + space, mConsDetailConsWorkTendency.getPaint());

            new ShareBuilder(this).withImg(shareBit).share();
        }
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
