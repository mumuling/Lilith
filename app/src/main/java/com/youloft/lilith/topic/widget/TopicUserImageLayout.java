package com.youloft.lilith.topic.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class TopicUserImageLayout extends RelativeLayout {

    ImageView userImage6;

    ImageView userImage5;

    ImageView userImage4;

    ImageView userImage3;

    ImageView userImage2;

    ImageView userImage1;

    TextView userNumber;
    private Context mContext;
    private List<ImageView> mUserImageList = new ArrayList<>();


    public TopicUserImageLayout(Context context) {
        this(context, null);

        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_topic_user_image, this);
    }

    public TopicUserImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_topic_user_image, this);
        init();
    }
    private void init() {
        userImage1 = (ImageView) findViewById(R.id.user_image1);
        userImage2 = (ImageView) findViewById(R.id.user_image2);
        userImage3 = (ImageView) findViewById(R.id.user_image3);
        userImage4 = (ImageView) findViewById(R.id.user_image4);
        userImage5 = (ImageView) findViewById(R.id.user_image5);
        userImage6 = (ImageView) findViewById(R.id.user_image6);
        userNumber = (TextView) findViewById(R.id.user_number);
        mUserImageList.add(userImage1);
        mUserImageList.add(userImage2);
        mUserImageList.add(userImage3);
        mUserImageList.add(userImage4);
        mUserImageList.add(userImage5);
        mUserImageList.add(userImage6);
        for (int i = 0; i < mUserImageList.size(); i ++) {
            GlideApp.with(mContext).asBitmap()
                    .transform(new GlideCircleTransform(mContext))
                    .load(R.mipmap.er)
                    .into(mUserImageList.get(i));
        }
    }
}
