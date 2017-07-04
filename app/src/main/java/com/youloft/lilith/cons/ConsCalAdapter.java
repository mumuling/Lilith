package com.youloft.lilith.cons;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.view.ConsCalendar;

/**
 * Created by zchao on 2017/7/3.
 * desc:
 * version:
 */

public class ConsCalAdapter extends RecyclerView.Adapter<ConsCalAdapter.ConsCalItemHolder> {
    private Context mContext;
    private int mCalType = ConsCalendar.CONS_CAL_TYPE_MONTH;

    public ConsCalAdapter(Context context) {
        mContext = context;
    }

    public void setCalType(int type){
        if (mCalType == type) {
            return;
        }
        mCalType = type;
        notifyDataSetChanged();
    }

    @Override
    public ConsCalItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cons_cal_item, parent, false);
        return new ConsCalItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsCalItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mCalType == ConsCalendar.CONS_CAL_TYPE_WEEK ? 7 : 28;
    }


    class ConsCalItemHolder extends RecyclerView.ViewHolder{

        public ConsCalItemHolder(View itemView) {
            super(itemView);
        }
    }

}
