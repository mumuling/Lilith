package com.youloft.lilith.cons.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.socialize.media.Base;

/**
 * Created by zchao on 2017/7/4.
 * desc:
 * version:
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
    public Context mContext;
    public BaseHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
    }

    public abstract void bindData(T t);

}
