package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.measure.bean.MeasureBean;

/**
 * Created by Administrator on 2017/7/19.
 */

public class EmptyHolder extends BaseMeasureHolder{
    public EmptyHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.item_measure_empty, viewGroup, false));
    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {

    }
}
