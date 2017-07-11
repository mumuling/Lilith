package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.measure.bean.MeasureBean;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测测最顶部的轮播holder
 * <p>
 * Created by GYH on 2017/7/4.
 */

public class MeasureCarouselHolder extends BaseMeasureHolder {
    @BindView(R.id.vp_carousel)
    ViewPager vpCarousel; //viewpager
    @BindView(R.id.iv_dot01)
    ImageView ivDot01;  //第一个原点
    @BindView(R.id.iv_dot02)
    ImageView ivDot02;  //第二原点
    @BindView(R.id.iv_dot03)
    ImageView ivDot03;  //第三个原点
    private MeasureBean.DataBean mMeasureData;
    private Handler mhandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int index = vpCarousel.getCurrentItem() + 1;
            vpCarousel.setCurrentItem(index);
            mhandler.postDelayed(mRunnable, 2500);
        }
    };

    public MeasureCarouselHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_measure_carousel, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {
        this.mMeasureData = mMeasureData;
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(vpCarousel.getContext(),
                    new AccelerateInterpolator());
            field.set(vpCarousel, scroller);
            scroller.setmDuration(500);
        } catch (Exception e) {
        }


        vpCarousel.setAdapter(new MyAdapter(mMeasureData, mContext));
        vpCarousel.setCurrentItem(300000,false);
        mhandler.postDelayed(mRunnable, 2500);
        vpCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = position % 3;
                switch (index) {
                    case 0:
                        ivDot01.setImageResource(R.drawable.pager_select_icon);
                        ivDot02.setImageResource(R.drawable.pager_icon);
                        ivDot03.setImageResource(R.drawable.pager_icon);
                        break;
                    case 1:
                        ivDot01.setImageResource(R.drawable.pager_icon);
                        ivDot02.setImageResource(R.drawable.pager_select_icon);
                        ivDot03.setImageResource(R.drawable.pager_icon);
                        break;
                    case 2:
                        ivDot01.setImageResource(R.drawable.pager_icon);
                        ivDot02.setImageResource(R.drawable.pager_icon);
                        ivDot03.setImageResource(R.drawable.pager_select_icon);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpCarousel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mhandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mhandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        mhandler.postDelayed(mRunnable, 2500);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mhandler.postDelayed(mRunnable, 2500);
                        break;

                }
                return false;
            }
        });
    }


    class MyAdapter extends PagerAdapter {

        private MeasureBean.DataBean mMeasureData;
        private Context mContext;

        public MyAdapter(MeasureBean.DataBean mMeasureData, Context mContext) {
            this.mMeasureData = mMeasureData;
            this.mContext = mContext;

        }

        @Override
        public int getCount() {
            if(mMeasureData == null || mMeasureData.ads == null){
                return 0;
            }else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position % 3;
            View mView = View.inflate(mContext, R.layout.item_banner, null);
            ImageView ivItemBanner = (ImageView) mView.findViewById(R.id.iv_item_banner);
            GlideApp.with(mContext).load(mMeasureData.ads.get(index).image).into(ivItemBanner);
            container.addView(mView);
            if (mView.getParent() != null) {
                return mView;
            }
            return mView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
}
