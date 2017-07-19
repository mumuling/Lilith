package com.youloft.lilith.cons.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.statistics.AppAnalytics;

import java.util.HashSet;

/**
 * Created by zchao on 2017/7/4.
 * desc:
 * version:
 */

public class ConsFragmentCardAdapter extends RecyclerView.Adapter<BaseHolder> {
    private Context mContext;
    private ConsPredictsBean mData;
    private String mTitle = "";

    private HashSet<String> imreport = new HashSet<>();


    public ConsFragmentCardAdapter(Context context) {
        mContext = context;
    }

    public void setData(ConsPredictsBean data){
        mData = data;
        notifyDataSetChanged();
    }

    public void setTitle(String titleName){
        mTitle = titleName;
        notifyItemChanged(0);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return HolderFractory.obtainHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (holder == null)return;
        if (holder instanceof ConsBaseHolder && mData != null) {
            ((ConsBaseHolder) holder).bindData(mData);
        } else if (holder instanceof ConsTitleHolder) {
            ((ConsTitleHolder) holder).bindData(mTitle);
        } else if (holder instanceof ConsHotTopicHolder) {
            holder.bindData(null);
        }
        if (holder instanceof ConsYSHolder) {
            String s = ((ConsYSHolder) holder).initReportKey();
            if (!imreport.contains(s)){
                AppAnalytics.onEvent(s + ".IM");
                imreport.add(s);
            }
        }
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return HolderFractory.obtainHolderType(position);
    }
}
