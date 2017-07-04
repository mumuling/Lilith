package com.youloft.lilith.cons.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.youloft.lilith.cons.bean.ConsPredictsBean;

/**
 * Created by zchao on 2017/7/4.
 * desc:
 * version:
 */

public class ConsFragmentCardAdapter extends RecyclerView.Adapter<BaseHolder> {
    private Context mContext;
    private ConsPredictsBean mData;
    public ConsFragmentCardAdapter(Context context) {
        mContext = context;
    }

    public void setData(ConsPredictsBean data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return HolderFractory.obtainHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (holder == null)return;
        if (holder instanceof ConsBaseHolder && mData != null) {
            ((ConsBaseHolder)holder).bindData(mData);
        }
        holder.bindData("");
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    @Override
    public int getItemViewType(int position) {
        return HolderFractory.obtainHolderType(position);
    }
}
