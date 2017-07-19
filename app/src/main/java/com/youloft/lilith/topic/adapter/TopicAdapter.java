package com.youloft.lilith.topic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.glide.GlideBlurTransform;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.widget.TopicUserDataBind;
import com.youloft.statistics.AppAnalytics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 星座话题列表的Adapter
 * version
 *
 * @author slj
 * @time 2017/6/29 10:59
 * @class TopicAdapter
 */

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static int ITEM_TYPE_HEADER = 1000;//顶部header
    private static int ITEM_TYPE_NORMAL = 2000;//普通item
    private static int ITEM_TYPE_HOT_TOPIC = 2002;//首页热门item
    private static int ITEM_TYPE_NO_DATA = 2003;//首页热门无数据item
    private static int ITEM_TYPY_BOTTOM = 3999;
    private boolean isHotTopic = false;


    private List<TopicBean.DataBean> topicBeanList = new ArrayList<>();

    public TopicAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public TopicAdapter(Context context, boolean isHotTopic) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.isHotTopic = isHotTopic;
    }

    public void setData(List<TopicBean.DataBean> data) {
        if (data == null) return;
        topicBeanList.clear();
        topicBeanList.addAll(data);
        notifyDataSetChanged();
    }

    public void setMoreData(List<TopicBean.DataBean> data) {
        if (data == null || data.size() == 0) return;
        topicBeanList.addAll(data);
        notifyItemRangeChanged(topicBeanList.size() - data.size() + 1, data.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE_HEADER) {
            holder = new MyHeaderHolder(mInflater.inflate(R.layout.header_topic_rv, parent, false));
        } else if (viewType == ITEM_TYPE_HOT_TOPIC) {
            holder = new HotTopicViewHolder(mInflater.inflate(R.layout.list_item_hot_topic, parent, false));
        } else if (viewType == ITEM_TYPE_NORMAL) {
            holder = new NormalViewHolder(mInflater.inflate(R.layout.list_item_topic, parent, false));
        } else if (viewType == ITEM_TYPE_NO_DATA) {
            holder = new HotTopicNoDataViewHolder(mInflater.inflate(R.layout.list_item_hot_topic_no_data, parent, false));
        } else {
            holder = new MyHeaderHolder(mInflater.inflate(R.layout.bottom_topic_rv, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NormalViewHolder) {
            TopicBean.DataBean safeData = SafeUtil.getSafeData(topicBeanList, getRealPosition(position));
            if (safeData == null) {
                return;
            }
            ((NormalViewHolder) holder).bind(safeData, position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppAnalytics.onEvent("Topic", "C" + String.valueOf(position - 1));
                    ARouter.getInstance().build("/test/TopicDetailActivity")
                            .withInt("tid", topicBeanList.get(getRealPosition(position)).id)
                            .withString("bakImg", topicBeanList.get(getRealPosition(position)).backImg)
                            .navigation();
                    if (holder instanceof HotTopicViewHolder) {
                        AppAnalytics.onEvent("Hometopic", "C" + String.valueOf(position - 1));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isHotTopic) {
            if (topicBeanList == null || topicBeanList.isEmpty()) {
                return 4;
            } else {
                return topicBeanList.size();
            }
        }
        return topicBeanList.size() + 2;
    }

    public int getRealPosition(int position) {

        return position - (isHotTopic ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHotTopic) {
            if (topicBeanList == null || topicBeanList.isEmpty()) {
                return ITEM_TYPE_NO_DATA;
            } else {
                return ITEM_TYPE_HOT_TOPIC;
            }
        }
        return position == 0 ? ITEM_TYPE_HEADER : position == getItemCount() - 1 ? ITEM_TYPY_BOTTOM : ITEM_TYPE_NORMAL;
    }

    /**
     * 话题item的holder
     */
    public class NormalViewHolder extends RecyclerView.ViewHolder {
        public TextView mTopicContent;
        public TopicUserDataBind mUserImageStackViewGroup;
        public ImageView mTopicImage;
        public ImageView imageTest;
        private boolean needReport = true;
        private TopicBean.DataBean topic;
        private boolean isFirst = true;
        private HashSet<String> hashPosition = new HashSet<>();

        public NormalViewHolder(View itemView) {
            super(itemView);
            mTopicContent = (TextView) itemView.findViewById(R.id.topic_content);
            mUserImageStackViewGroup = (TopicUserDataBind) itemView.findViewById(R.id.layout_user_image);
            mTopicImage = (ImageView) itemView.findViewById(R.id.image_topic_bg);
        }

        public void bind(TopicBean.DataBean topic, int position) {
            if (topic == null) return;
            if (hashPosition != null && !hashPosition.contains(String.valueOf(position))) {
                // 展示埋点
                AppAnalytics.onEvent("Topic", "IM" + String.valueOf(position - 1));
                hashPosition.add(String.valueOf(position));
            }
            this.topic = topic;
            mTopicContent.setText(topic.title);
            GlideApp.with(itemView)
                    .asBitmap()
                    .load(topic.backImg)
                    .dontAnimate()
                    .signature(new ObjectKey("list:" + topic.backImg))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .transform(new MultiTransformation<Bitmap>(new CenterCrop(),new GlideBlurTransform(itemView.getContext())))
                    .transform(GlideBlurTransform.getInstance(LLApplication.getInstance()))
                    .override(188, 75)
                    .into(mTopicImage);


            mUserImageStackViewGroup.bindData(topic.voteUser, topic.totalVote);
        }
    }

    /**
     * 热门话题item的holder
     */
    public class HotTopicViewHolder extends NormalViewHolder {
        private HashSet<String> homePosition = new HashSet<>();

        public HotTopicViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(TopicBean.DataBean topic, int position) {
            if (homePosition != null && !homePosition.contains(String.valueOf(position))) {
                // 展示埋点
                AppAnalytics.onEvent("Hometopic", "IM" + String.valueOf(position - 1));
                homePosition.add(String.valueOf(position));
            }
            mTopicContent.setText(topic.title);

            GlideApp.with(itemView)
                    .asBitmap()
                    .load(topic.backImg)
                    .dontAnimate()
                    .signature(new ObjectKey("list:" + topic.backImg))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(GlideBlurTransform.getInstance(LLApplication.getInstance()))
                    .override(188, 75)
                    .into(mTopicImage);
            mUserImageStackViewGroup.bindData(topic.voteUser, topic.totalVote);
        }
    }

    /**
     * 热门话题item的holder
     */
    public class HotTopicNoDataViewHolder extends RecyclerView.ViewHolder {
        public HotTopicNoDataViewHolder(View itemView) {
            super(itemView);
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
