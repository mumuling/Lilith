package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.topic.bean.TopicBean;

import java.util.List;

/**
 * 首页热门话题list的view封装
 */

public class HotUserImageStackViewGroup extends FrameLayout implements TopicUserDataBind{

    TextView userNumber;
    UserImageStackViewGroup mUserStack;
    private Context mContext;


    public HotUserImageStackViewGroup(Context context) {
        this(context, null);
    }

    public HotUserImageStackViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_topic_user_image_hot, this);
        userNumber = (TextView) findViewById(R.id.user_number);
        mUserStack = (UserImageStackViewGroup) findViewById(R.id.cons_hot_topic_user_stack);
    }


    @Override
    public void bindData(List<TopicBean.DataBean.VoteUserBean> imageList, int number) {
        mUserStack.bindData(imageList);
        userNumber.setText(String.valueOf(number) + "人");
    }


}
