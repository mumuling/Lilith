package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.topic.bean.TopicBean;

import java.util.List;

/**
 * 话题list的view封装
 */

public class TopicUserImageLayout extends FrameLayout implements TopicUserDataBind{

    TextView userNumber;
    UserImageStackViewGroup mUserStack;
    private Context mContext;


    public TopicUserImageLayout(Context context) {
        this(context, null);
    }

    public TopicUserImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_topic_user_image_1, this);
        userNumber = (TextView) findViewById(R.id.user_number);
        mUserStack = (UserImageStackViewGroup) findViewById(R.id.cons_hot_topic_user_stack);
    }


    @Override
    public void bindData(List<TopicBean.DataBean.VoteUserBean> imageList, int number) {
        mUserStack.bindData(imageList);
        if (number <= 0) {
            userNumber.setVisibility(GONE);
        } else {
            userNumber.setVisibility(VISIBLE);
            userNumber.setText(String.valueOf(number) + "人");
        }
    }


}
