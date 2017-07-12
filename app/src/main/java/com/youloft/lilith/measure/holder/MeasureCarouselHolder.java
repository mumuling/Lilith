package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.measure.bean.MeasureBean;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * 测测最顶部的轮播holder
 * <p>
 * Created by GYH on 2017/7/4.
 */

public class MeasureCarouselHolder extends BaseMeasureHolder {
    @BindView(R.id.vp_carousel)
    ViewPager vpCarousel; //viewpager
    @BindViews({R.id.iv_dot01, R.id.iv_dot02, R.id.iv_dot03, R.id.iv_dot04, R.id.iv_dot05})
    ImageView[] ivDots;  //原点

    //下面的handler是玩轮播的
    private Handler mhandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int index = vpCarousel.getCurrentItem() + 1;
            vpCarousel.setCurrentItem(index);
            if(mData.size()!=1){
                mhandler.postDelayed(mRunnable, 2500);
            }
        }
    };
    //数据集合
    private List<MeasureBean.DataBean.AdsBean> mData;

    public MeasureCarouselHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_measure_carousel, parent, false));
        ButterKnife.bind(this, itemView);
        for (int i = 0; i < 5; i++) {//小圆点先隐藏起来
            ivDots[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void bindData(final MeasureBean.DataBean mMeasureData, int position) {
        //这里也要写,防止下拉刷新的时候数据产生了变化
        for (int i = 0; i < 5; i++) {//小圆点先隐藏起来
            ivDots[i].setVisibility(View.INVISIBLE);
        }

        if (isHavaData(mMeasureData)) {//无数据 不走了
            return;
        }

        if (mMeasureData.ads.size() > 5) {
            for (int i = 0; i < 5; i++) { //大于5 只取前五个
                mData.add(mMeasureData.ads.get(i));
            }
        } else {
            mData = mMeasureData.ads;
        }

        //根据数据集合的长度,决定显示几个原点
        disPlayDot();
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(vpCarousel.getContext());
//            FixedSpeedScroller scroller = new FixedSpeedScroller(vpCarousel.getContext(),new AccelerateInterpolator());
            field.set(vpCarousel, scroller);
            scroller.setmDuration(800);
        } catch (Exception e) {
        }

        vpCarousel.setAdapter(new MyAdapter(mData, mContext));
        if(mData.size() == 1){
            mhandler.removeCallbacksAndMessages(null);
        }else {
            mhandler.postDelayed(mRunnable, 2500);
            vpCarousel.setCurrentItem(mData.size() * 100000, false);
        }
        vpCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = position % mData.size();
                setDotStatus(index);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpEventHandle();//viewpager触摸事件处理
    }

    /**
     * 根据原点
     */
    private void disPlayDot() {
        if (mData.size() == 1) { //如果数据集合长度 等于1  那么不显示小圆点
            return;
        } else { //如果不是,那就显示对应的个数
            for (int i = 0; i < 5; i++) {
                if (i < mData.size()) {
                    ivDots[i].setVisibility(View.VISIBLE);
                } else {
                    ivDots[i].setVisibility(View.GONE);
                }
            }
        }

    }

    /**
     * viewpager触摸事件的处理
     */
    private void vpEventHandle() {
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

    /**
     * 判断是否有数据
     *
     * @param mMeasureData
     * @return
     */
    private boolean isHavaData(MeasureBean.DataBean mMeasureData) {
        return mMeasureData == null || mMeasureData.ads == null || mMeasureData.ads.size() == 0;
    }

    /**
     * 小圆点的设置
     *
     * @param index
     */
    private void setDotStatus(int index) {
        for (int i = 0; i < mData.size(); i++) {
            if (i == index) {
                ivDots[i].setImageResource(R.drawable.pager_select_icon);
            } else {
                ivDots[i].setImageResource(R.drawable.pager_icon);
            }
        }
    }


    class MyAdapter extends PagerAdapter {


        private Context mContext;
        List<MeasureBean.DataBean.AdsBean> mData;

        public MyAdapter(List<MeasureBean.DataBean.AdsBean> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;

        }

        @Override
        public int getCount() {
            if(mData.size() == 1){
                return mData.size();
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
            final int index = position % mData.size();
            View mView = View.inflate(mContext, R.layout.item_banner, null);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build("/ui/web")
                            .withString("url", mData.get(index).url)
                            .navigation();
                }
            });
            ImageView ivItemBanner = (ImageView) mView.findViewById(R.id.iv_item_banner);
            GlideApp.with(mContext).load(mData.get(index).image).into(ivItemBanner);
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
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
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
