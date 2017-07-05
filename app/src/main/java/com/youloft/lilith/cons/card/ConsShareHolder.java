package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.bean.ConsPredictsBean;

/**
 * Created by zchao on 2017/7/5.
 * desc:
 * version:
 */

public class ConsShareHolder extends ConsBaseHolder {

    private final View mRoot;

    public ConsShareHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_share_holder);
        mRoot = itemView.findViewById(R.id.cons_share_root);
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
    }
}
