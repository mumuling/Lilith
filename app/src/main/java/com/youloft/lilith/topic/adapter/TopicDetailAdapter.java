package com.youloft.lilith.topic.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.topic.PointDetailActivity;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.holder.OtherTopicHolder;
import com.youloft.lilith.topic.holder.PointHolder;
import com.youloft.lilith.topic.holder.VoteHolder;
import com.youloft.lilith.topic.widget.VoteDialog;
import com.youloft.lilith.topic.widget.VoteView;

import java.util.ArrayList;
import java.util.List;

/**         话题详情的Adapter
 *version
 *@author  slj
 *@time    2017/7/4 14:08
 *@class   TopicDetailAdapter
 */

public class TopicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static int ITEM_TYPE_OTHER = 1000;//顶部header
    private static int ITEM_TYPE_VOTE_VIEW = 2000;//投票的view
    private static int ITEM_TYPE_COMMENT = 3000;//评论item
    public List<PointBean.DataBean> pointBeanList = new ArrayList<>();
    private List<TopicBean.DataBean> otherTopicList = new ArrayList<>();
    private TopicDetailBean.DataBean topicInfo = null;
    public TopicDetailAdapter (Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     *   观点列表
     * @param list
     */
    public void setPointBeanList(List<PointBean.DataBean> list) {
        if (list == null)return;
        pointBeanList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOtherTopicList(List<TopicBean.DataBean> otherTopicList) {
        if (otherTopicList == null) return;
        this.otherTopicList.addAll(otherTopicList);
        notifyDataSetChanged();
    }

    /**
     *
     * @param topicInfo  话题信息
     */
    public void setTopicInfo(TopicDetailBean.DataBean topicInfo) {
        this.topicInfo = topicInfo;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
      if (viewType == ITEM_TYPE_VOTE_VIEW){
            holder = new VoteHolder(mInflater.inflate(R.layout.item_topic_detail_vote,parent,false));
        } else if (viewType == ITEM_TYPE_COMMENT){
            holder = new PointHolder(mInflater.inflate(R.layout.item_topic_detail_comment,parent,false),this);
        }else {
          holder = new OtherTopicHolder(mInflater.inflate(R.layout.item_other_topic_layout,parent,false));

      }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoteHolder) {
                ((VoteHolder) holder).bindView(topicInfo);
        }
        if (holder instanceof PointHolder) {
            if (position == pointBeanList.size()) {
                ((PointHolder) holder).bindNormal(pointBeanList.get(position - 1), topicInfo,true);
            } else {
                ((PointHolder) holder).bindNormal(pointBeanList.get(position - 1), topicInfo,false);
            }
        }
        if (holder instanceof OtherTopicHolder) {
            if (position == pointBeanList.size() + 1) {
                ((OtherTopicHolder) holder).bind(otherTopicList.get(position - pointBeanList.size() -1),true);
            } else {
                ((OtherTopicHolder) holder).bind(otherTopicList.get(position - pointBeanList.size() -1),false);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_VOTE_VIEW;
        } else if (position >= pointBeanList.size() + 1) {
            return ITEM_TYPE_OTHER;
        }else {
            return ITEM_TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return pointBeanList.size() + otherTopicList.size() + 1;
    }




}
