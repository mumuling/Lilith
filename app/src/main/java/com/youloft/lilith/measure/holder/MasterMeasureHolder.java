package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.measure.bean.MeasureBean;

/**
 * 大师亲算的holder
 *
 * Created by GYH on 2017/7/4.
 */

public class MasterMeasureHolder extends BaseMeasureHolder{

    public MasterMeasureHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_master_measure, parent, false));
    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {

    }
}
