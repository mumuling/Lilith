package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.youloft.lilith.measure.bean.MeasureBean;

/**
 * 测测的基类holder
 *
 * Created by GYH on 2017/7/4.
 */

public abstract class BaseMeasureHolder extends RecyclerView.ViewHolder{

    public Context mContext;

    public BaseMeasureHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
    }
    public abstract void bindData(MeasureBean.DataBean mMeasureData, int position);
}
