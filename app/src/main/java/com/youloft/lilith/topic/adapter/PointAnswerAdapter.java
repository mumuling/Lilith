package com.youloft.lilith.topic.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.holder.AuthorPointHolder;
import com.youloft.lilith.topic.holder.PointAnswerNormalHolder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class PointAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static int ITEM_TYPE_NONE= 1000;//无评论
    private static int ITEM_TYPE_NORMAL = 2000;//普通item
    private static int ITEM_TYPE_AUTHOR = 3000;//作者的item
    private PointBean.DataBean point;
    private  ArrayList<TopicDetailBean.DataBean.OptionBean> topic;

    private List<ReplyBean.DataBean> replyList = new ArrayList<>();
    public PointAnswerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setReplyList(List<ReplyBean.DataBean> replyList) {
        if (replyList != null ) {
            this.replyList.addAll(replyList);
            notifyDataSetChanged();
        }
    }

    public void setPointAndTopic(PointBean.DataBean point,ArrayList<TopicDetailBean.DataBean.OptionBean> topic)  {
        this.point = point;
        this.topic = topic;
        notifyDataSetChanged();
    }

    public void setAnswerTop(ReplyBean.DataBean data) {
        replyList.add(0,data);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE_AUTHOR) {
            holder = new AuthorPointHolder(mInflater.inflate(R.layout.item_point_head,parent,false));
        } else if (viewType == ITEM_TYPE_NONE) {
            holder = new NoAnswerHolder(mInflater.inflate(R.layout.item_point_no_anwser,parent,false));
        } else {
            holder = new PointAnswerNormalHolder(mInflater.inflate(R.layout.item_point_answer_normal,parent,false),this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PointAnswerNormalHolder) {
            if (replyList.size() == 0)return;
            if (position==1) {
                ((PointAnswerNormalHolder) holder).bindView(replyList.get(position - 1),true);
            }else {
                ((PointAnswerNormalHolder) holder).bindView(replyList.get(position - 1),false);
            }

        }
        if (holder instanceof AuthorPointHolder) {
            if (point == null || topic == null)return;
                ((AuthorPointHolder) holder).bindView(point, topic);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_AUTHOR;
        } else if ( (replyList == null || replyList.size() == 0) &&position == 1) {
            return ITEM_TYPE_NONE;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return replyList.size() == 0? 2:replyList.size()+ 1;
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

        public void bindView(ReplyBean.DataBean dataBean) {

        }
    }
}
