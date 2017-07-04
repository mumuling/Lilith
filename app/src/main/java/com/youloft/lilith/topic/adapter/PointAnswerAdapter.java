package com.youloft.lilith.topic.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youloft.lilith.R;

/**
 *
 */

public class PointAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static int ITEM_TYPE_NONE= 1000;//无评论
    private static int ITEM_TYPE_NORMAL = 2000;//普通item
    private static int ITEM_TYPE_AUTHOR = 3000;//作者的item
    public PointAnswerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE_AUTHOR) {
            holder = new HeadHolder(mInflater.inflate(R.layout.item_point_head,parent,false));
        } else if (viewType == ITEM_TYPE_NONE) {
            holder = new NoAnswerHolder(mInflater.inflate(R.layout.item_point_no_anwser,parent,false));
        } else {
            holder = new NormalHolder(mInflater.inflate(R.layout.item_point_answer_normal,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_AUTHOR;
        } else if (position == 1) {
            return ITEM_TYPE_NONE;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class NoAnswerHolder extends RecyclerView.ViewHolder {
        public NoAnswerHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeadHolder extends RecyclerView.ViewHolder {
        public HeadHolder(View itemView) {
            super(itemView);
        }
    }

    public class AnswerHolder extends RecyclerView.ViewHolder {

        public AnswerHolder(View itemView) {
            super(itemView);
        }
    }

    public class NormalHolder extends RecyclerView.ViewHolder {
        public NormalHolder(View itemView) {
            super(itemView);
        }
    }
}
