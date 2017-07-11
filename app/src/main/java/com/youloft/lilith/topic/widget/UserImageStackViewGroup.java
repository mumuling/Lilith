package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.glide.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class UserImageStackViewGroup extends RelativeLayout {

    ImageView userImage6;

    ImageView userImage5;

    ImageView userImage4;

    ImageView userImage3;

    ImageView userImage2;

    ImageView userImage1;

    private Context mContext;
    private List<ImageView> mUserImageList = new ArrayList<>();


    public UserImageStackViewGroup(Context context) {
        this(context, null);
        //这儿掉了this就不用在执行下边的了，否者是执行两边
//
//        this.mContext = context;
//        LayoutInflater.from(context).inflate(R.layout.layout_topic_user_image, this);
    }

    public UserImageStackViewGroup(Context context, AttributeSet attrs) {
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
        mUserImageList.add(userImage1);
        mUserImageList.add(userImage2);
        mUserImageList.add(userImage3);
        mUserImageList.add(userImage4);
        mUserImageList.add(userImage5);
        mUserImageList.add(userImage6);
    }

    /**
     * 绑定数据
     * @param imageList
     */
    public void bindData(List<TopicBean.DataBean.VoteUserBean> imageList) {
        for (int i = 0; i < Math.min(imageList.size(), mUserImageList.size()); i++) {
            mUserImageList.get(i).setVisibility(VISIBLE);
            GlideApp.with(mContext).asBitmap()
                    .error(R.drawable.topic_user_img_er)
                    .transform(new GlideCircleTransform())
                    .load(imageList.get(i).headImg)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(false)
                    .into(mUserImageList.get(i));
        }
    }


}
