package com.youloft.lilith.topic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.topic.PointDetailActivity;

/**
 *
 */

public class TopicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static int ITEM_TYPE_HEADER = 1000;//顶部header
    private static int ITEM_TYPE_VOTE_VIEW = 2000;//投票的view
    private static int ITEM_TYPE_COMMENT = 3000;//评论item
    public TopicDetailAdapter (Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE_HEADER) {
            holder = new HeadHolder(mInflater.inflate(R.layout.item_topic_detail_title,parent,false));
        } else if (viewType == ITEM_TYPE_VOTE_VIEW){
            holder = new VoteHolder(mInflater.inflate(R.layout.item_topic_detail_vote,parent,false));
        } else {
            holder = new CommentHolder(mInflater.inflate(R.layout.item_topic_detail_comment,parent,false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PointDetailActivity.class);
                    intent.putExtra("data","测试传数据");
                    mContext.startActivity(intent);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == 1) {
            return ITEM_TYPE_VOTE_VIEW;
        } else {
            return ITEM_TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }
    public class HeadHolder extends RecyclerView.ViewHolder {
        public TextView mTopicTitle;
        public ImageView mTopImage;
        public HeadHolder(View itemView) {
            super(itemView);
            mTopicTitle = (TextView) itemView.findViewById(R.id.title_top_text);
            mTopImage = (ImageView) itemView.findViewById(R.id.image_top_title);
        }
    }

    public class VoteHolder extends RecyclerView.ViewHolder {
        public VoteHolder(View itemView) {
            super(itemView);
        }
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        public CommentHolder(View itemView) {
            super(itemView);
        }
    }
}
