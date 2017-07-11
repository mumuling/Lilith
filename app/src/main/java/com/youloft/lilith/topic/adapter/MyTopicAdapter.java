package com.youloft.lilith.topic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.topic.bean.MyTopicBean;
import com.youloft.lilith.topic.holder.MyTopicHolder;

import java.util.ArrayList;

/**      我的话题的Adapter
 *version
 *@author  slj
 *@time    2017/7/10 16:42
 *@class   MyTopicAdapter
 */

public class MyTopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<MyTopicBean.DataBean> myTopicList = new ArrayList<>();

    public MyTopicAdapter(Context context) {
        this.mContext = context;
    }

    public void setMyTopicList(ArrayList<MyTopicBean.DataBean> list) {
        if (list == null || list.size() == 0)return;
        myTopicList.addAll(list);
        notifyDataSetChanged();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        holder = new MyTopicHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_topic,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyTopicHolder) {
            if (position == myTopicList.size() -1) {
                ((MyTopicHolder) holder).bind(myTopicList.get(position),true);
            } else {
                ((MyTopicHolder) holder).bind(myTopicList.get(position),true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myTopicList.size();
    }
}


