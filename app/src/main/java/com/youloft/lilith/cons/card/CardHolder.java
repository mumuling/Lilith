package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zchao on 2017/7/6.
 * desc:
 * version:
 */

public class CardHolder extends BaseHolder{

    public CardHolder(View itemView, Context context) {
        super(itemView, context);
    }
    public CardHolder(Context context, ViewGroup parent, int resId) {
        this(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false), context);
    }


    @Override
    public void bindData(Object o) {

    }
}
