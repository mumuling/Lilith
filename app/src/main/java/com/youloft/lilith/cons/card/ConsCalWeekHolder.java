package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.ConsCalDetailActivity;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.view.ConsCalendar;

/**
 * Created by zchao on 2017/7/5.
 * desc:
 * version:
 */

public class ConsCalWeekHolder extends ConsBaseHolder implements ConsCalendar.OnClickListener{


    private final ConsCalendar mWeekView;

    public ConsCalWeekHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_week_holder);
        mWeekView = (ConsCalendar) itemView.findViewById(R.id.cons_week_view);
        mWeekView.setCalType(ConsCalendar.CONS_CAL_TYPE_WEEK);
        mWeekView.setListener(this);
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
        mWeekView.setData(data);
    }

    @Override
    public void onClick() {
        int[] local = new int[2];
        mWeekView.getLocationOnScreen(local);


        ConsCalDetailActivity.startConsCalDetailActivity(mContext, local, null);
    }
}
