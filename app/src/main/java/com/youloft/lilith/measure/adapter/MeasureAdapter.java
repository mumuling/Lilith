package com.youloft.lilith.measure.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.measure.bean.MeasureBean;
import com.youloft.lilith.measure.holder.BaseMeasureHolder;
import com.youloft.lilith.measure.holder.ImmediatelyMeasureHolder;
import com.youloft.lilith.measure.holder.MasterMeasureHolder;
import com.youloft.lilith.measure.holder.MeasureBannerHolder;
import com.youloft.lilith.measure.holder.MeasureCarouselHolder;

import java.util.ArrayList;
import java.util.List;

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


    private List<MeasureBean.DataBean> mMeasureData = new ArrayList<>();
    private Activity mContext;
    public MeasureAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MeasureBean.DataBean> mMeasureData) {
        this.mMeasureData = mMeasureData;
        notifyDataSetChanged();
    }

    @Override
    public BaseMeasureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_CAROUSEL:
                return new MeasureCarouselHolder(mContext, parent);
            case ITEM_MASTER_MEASURE:
                return new MasterMeasureHolder(mContext, parent);
            case ITEM_BANNER:
                return new MeasureBannerHolder(mContext, parent);
            case ITEM_IMMEDIATELY_MEASURE:
                return new ImmediatelyMeasureHolder(mContext, parent);
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

        if (holder instanceof MeasureCarouselHolder) {  //轮播holder
            holder.bindData(mMeasureData.get(0),position);
        } else if (holder instanceof MasterMeasureHolder) { //大师清算
            holder.bindData(mMeasureData.get(1),position);
        } else if (holder instanceof MeasureBannerHolder) { //bannerholder
            holder.bindData(mMeasureData.get(2),position);
        } else if (holder instanceof ImmediatelyMeasureHolder) {//立即测算holder
            holder.bindData(mMeasureData.get(3),position);
        }
    }

    @Override
    public int getItemCount() {
        //这里的长度需要计算一下  当最后一个的location为四的时候 需要加上一个长度
        if(mMeasureData == null || mMeasureData.size() == 0){
            return 0;
        }
        MeasureBean.DataBean dataBean = mMeasureData.get(mMeasureData.size() - 1);
        if (dataBean.location == 4) {
            return mMeasureData.size() + dataBean.ads.size() - 1;
        } else {
            return mMeasureData.size();
        }
    }
}
