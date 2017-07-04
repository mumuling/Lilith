package com.youloft.lilith.measure.holder;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Toaster;

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

    public MeasureCarouselHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        vpCarousel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toaster.showShort("haha");
            }
        });
    }
}
