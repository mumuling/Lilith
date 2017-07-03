package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.ViewUtil;

/**
 *
 */

public class VoteView extends LinearLayout {


    public VoteView(Context context) {
        this(context,null);
        LayoutInflater.from(context).inflate(R.layout.item_topic_detail_vote,this);

    }

    public VoteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_topic_detail_vote,this);
    }

}
