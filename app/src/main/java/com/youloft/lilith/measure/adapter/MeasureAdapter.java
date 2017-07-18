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

    public static final int ITEM_CAROUSEL = 1;//轮播图
    public static final int ITEM_MASTER_MEASURE = 2;//大师亲算
    public static final int ITEM_BANNER = 3;//banner
    public static final int ITEM_IMMEDIATELY_MEASURE = 4;//立即测算


    private int mImmedStart = 0; //统计立即测算起始位置
    private List<MeasureBean.DataBean> mMeasureData = new ArrayList<>();
    private Activity mContext;

    public MeasureAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MeasureBean.DataBean> measureData) {
        if (measureData == null) {
            return;
        }
        mMeasureData.clear();
        for (int i = 0; i < measureData.size(); i++) {
            MeasureBean.DataBean safeData = SafeUtil.getSafeData(measureData, i);
            if (safeData == null) {
                continue;
            }
            if (safeData.location == ITEM_IMMEDIATELY_MEASURE) {
                mImmedStart = i;
                List<MeasureBean.DataBean.AdsBean> ads = safeData.ads;
                //接口写的傻逼，于是乎自己来高,
                for (int j = 0; j < ads.size(); j++) {
                    MeasureBean.DataBean dataBean = new MeasureBean.DataBean();
                    dataBean.location = safeData.location;
                    ArrayList<MeasureBean.DataBean.AdsBean> ad = new ArrayList<>();
                    ad.add(ads.get(j));
                    dataBean.ads = ad;
                    mMeasureData.add(dataBean);
                }
            } else {
                mMeasureData.add(safeData);
            }
        }
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
        return mMeasureData.get(position).location;
    }

    @Override
    public void onBindViewHolder(BaseMeasureHolder holder, int position) {
        MeasureBean.DataBean safeData = SafeUtil.getSafeData(mMeasureData, position);
        if (safeData != null) {
            if (holder instanceof ImmediatelyMeasureHolder) {
                holder.bindData(safeData, position - mImmedStart);
            } else {
                holder.bindData(safeData, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMeasureData.size();
    }


}
