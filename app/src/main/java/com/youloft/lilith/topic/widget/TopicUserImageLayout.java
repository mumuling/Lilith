package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.youloft.lilith.R;

/**
 *
 */

public class TopicUserImageLayout extends RelativeLayout {
    private Context mContext;


    public TopicUserImageLayout(Context context) {
        this(context,null);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_topic_user_image,this);
    }

    public TopicUserImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_topic_user_image,this);
    }
}
