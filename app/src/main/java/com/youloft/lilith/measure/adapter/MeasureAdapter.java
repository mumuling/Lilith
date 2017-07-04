package com.youloft.lilith.measure.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.measure.holder.BaseMeasureHolder;
import com.youloft.lilith.measure.holder.ImmediatelyMeasureHolder;
import com.youloft.lilith.measure.holder.MasterMeasureHolder;
import com.youloft.lilith.measure.holder.MeasureBannerHolder;
import com.youloft.lilith.measure.holder.MeasureCarouselHolder;

/**
 * 测测的adapter
 * <p>
 * Created by GYH on 2017/7/4.
 */

public class MeasureAdapter extends RecyclerView.Adapter<BaseMeasureHolder> {

    public static final int ITEM_CAROUSEL = 1001;//轮播图
    public static final int ITEM_MASTER_MEASURE = 1002;//大师亲算
    public static final int ITEM_BANNER = 1003;//banner
    public static final int ITEM_IMMEDIATELY_MEASURE = 1004;//立即测算



    public MeasureAdapter() {

    }


    @Override
    public BaseMeasureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        switch (viewType) {
            case ITEM_CAROUSEL:
                return new MeasureCarouselHolder(LayoutInflater.from(context).inflate(R.layout.item_measure_carousel,parent,false));
            case ITEM_MASTER_MEASURE:
                return new MasterMeasureHolder(LayoutInflater.from(context).inflate(R.layout.item_master_measure,parent,false));
            case ITEM_BANNER:
                return new MeasureBannerHolder(LayoutInflater.from(context).inflate(R.layout.item_measure_banner,parent,false));
            case ITEM_IMMEDIATELY_MEASURE:
                return new ImmediatelyMeasureHolder(LayoutInflater.from(context).inflate(R.layout.item_immediately_banner,parent,false));
            default:
                return null;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_CAROUSEL;
            case 1:
                return ITEM_MASTER_MEASURE;
            case 2:
                return ITEM_BANNER;
            default:
                return ITEM_IMMEDIATELY_MEASURE;
        }
    }

    @Override
    public void onBindViewHolder(BaseMeasureHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
