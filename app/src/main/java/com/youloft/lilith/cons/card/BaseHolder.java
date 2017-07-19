package com.youloft.lilith.cons.card;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.socialize.media.Base;
import com.youloft.lilith.R;

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
        itemView.setBackgroundResource(getShadowBG());
    }

    public abstract void bindData(T t);

    public int getShadowBG(){
        return 0;
    }
}
