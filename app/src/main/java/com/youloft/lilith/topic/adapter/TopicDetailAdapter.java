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
    private static int ITEM_TYPE_NO_POINT = 4000;//沙发
    public List<PointBean.DataBean> pointBeanList = new ArrayList<>();
    private List<TopicBean.DataBean> otherTopicList = new ArrayList<>();
    private TopicDetailBean.DataBean topicInfo = null;
    private String backImage;
    public TopicDetailAdapter (Context context,String backImg) {
        this.mContext = context;
        this.backImage = backImg;
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

    public void setPointOnFirst(PointBean.DataBean pointOnFirst) {
        pointBeanList.add(0,pointOnFirst);
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
            holder = new VoteHolder(mInflater.inflate(R.layout.item_topic_detail_vote,parent,false),this);
        } else if (viewType == ITEM_TYPE_COMMENT){
            holder = new PointHolder(mInflater.inflate(R.layout.item_topic_detail_comment,parent,false),this);
        }else if (viewType == ITEM_TYPE_OTHER){
          holder = new OtherTopicHolder(mInflater.inflate(R.layout.item_other_topic_layout,parent,false));
      } else {
          holder = new NoPointHolder(mInflater.inflate(R.layout.item_point_no_anwser,parent,false));
      }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoteHolder) {
                ((VoteHolder) holder).bindView(topicInfo,backImage);
        }
         else if (holder instanceof PointHolder) {
            if (position == pointBeanList.size()) {
                ((PointHolder) holder).bindNormal(pointBeanList.get(position - 1), topicInfo,position,true);
            } else {
                ((PointHolder) holder).bindNormal(pointBeanList.get(position - 1), topicInfo,position,false);
            }
        }
        else if (holder instanceof OtherTopicHolder) {
            if (position == pointBeanList.size() + (pointBeanList.size() == 0 ? 2 : 1)) {
                ((OtherTopicHolder) holder).bind(otherTopicList.get(position - pointBeanList.size() -(pointBeanList.size() == 0 ? 2 : 1)),getOtherTopicPosition(position),true);
            } else {
                ((OtherTopicHolder) holder).bind(otherTopicList.get(position - pointBeanList.size() -(pointBeanList.size() == 0 ? 2 : 1)),getOtherTopicPosition(position),false);
            }
        }
    }

    private int getOtherTopicPosition(int position) {
        if (pointBeanList.size() == 0) {
            return position - 2;
        } else {
            return position - pointBeanList.size() - 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_VOTE_VIEW;
        }else if (pointBeanList.size() == 0 && position == 1) {
            return ITEM_TYPE_NO_POINT;
        } else if (position >= pointBeanList.size() + (pointBeanList.size() == 0 ? 2 : 1)) {
            return ITEM_TYPE_OTHER;
        }else {
            return ITEM_TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return pointBeanList.size() + otherTopicList.size() + (pointBeanList.size() == 0 ? 2 : 1);
    }

    public class NoPointHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public NoPointHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_no_data);
            textView.setText("快来投票当意见领袖");
        }
    }


}
