package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youloft.lilith.cons.bean.ConsPredictsBean;

/**
 * Created by zchao on 2017/7/4.
 * desc:
 * version:
 */

public class ConsBaseHolder extends BaseHolder<ConsPredictsBean> {

    public ConsBaseHolder(View itemView, Context context) {
        super(itemView, context);
    }

    public ConsBaseHolder(Context context, ViewGroup parent, int resId) {
        this(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false), context);
    }

    @Override
    public void bindData(ConsPredictsBean data) {

    }

}
