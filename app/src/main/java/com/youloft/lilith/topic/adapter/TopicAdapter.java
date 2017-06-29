package com.youloft.lilith.topic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.topic.TopicDetailActivity;

/**      星座话题列表的Adapter
 *version
 *@author  slj
 *@time    2017/6/29 10:59
 *@class   TopicAdapter
 */

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static int ITEM_TYPE_HEADER = 1000;//顶部header
    private static int ITEM_TYPE_NORMAL = 2000;//普通item

    public TopicAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE_HEADER) {
            holder = new MyHeaderHolder(mInflater.inflate(R.layout.header_topic_rv,parent,false));
        } else {
            holder = new NormalViewHolder(mInflater.inflate(R.layout.list_item_topic,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).mTopicContent.setText(position + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,TopicDetailActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_HEADER:ITEM_TYPE_NORMAL;
    }

    /**
     *   话题item的holder
     */
    public class NormalViewHolder extends RecyclerView.ViewHolder {
        public TextView mTopicContent;
        public NormalViewHolder(View itemView) {
            super(itemView);
            mTopicContent = (TextView) itemView.findViewById(R.id.topic_content);
        }
    }

    /**
     * 顶部header的holder
     */
    public class MyHeaderHolder extends RecyclerView.ViewHolder {

        public MyHeaderHolder(View itemView) {
            super(itemView);
        }
    }
}
