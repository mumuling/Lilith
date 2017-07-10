package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.measure.bean.MeasureBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测测最顶部的轮播holder
 * <p>
 * Created by GYH on 2017/7/4.
 */

public class MeasureCarouselHolder extends BaseMeasureHolder {
    @BindView(R.id.vp_carousel)
    ViewPager vpCarousel;
    private MeasureBean.DataBean mMeasureData;
    private Handler mhandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int index = vpCarousel.getCurrentItem() + 1;
            if(index > mMeasureData.ads.size()-1){
                vpCarousel.setCurrentItem(0,true);
            }else {
                vpCarousel.setCurrentItem(index);
            }
            mhandler.postDelayed(mRunnable,2000);
        }
    };
    public MeasureCarouselHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_measure_carousel, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {
        this.mMeasureData = mMeasureData;
        vpCarousel.setAdapter(new MyAdapter(mMeasureData, mContext));
        mhandler.postDelayed(mRunnable,2000);
        vpCarousel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

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
            return mMeasureData.ads.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View mView = View.inflate(mContext, R.layout.item_banner, null);
            ImageView ivItemBanner = (ImageView) mView.findViewById(R.id.iv_item_banner);
            GlideApp.with(mContext).load(mMeasureData.ads.get(position).image).into(ivItemBanner);
            container.addView(mView);
            return mView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
