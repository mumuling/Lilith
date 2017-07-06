package com.youloft.lilith.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.launcher.ARouter;
import com.umeng.socialize.media.UMImage;
import com.youloft.lilith.R;

/**
 * Created by zchao on 2017/7/6.
 * desc:
 * version:
 */

public class ShareBuilder {
    private String mShareTitle;
    private String mShareUrl;
    private UMImage mShareBitmap;
    private Context mContext;
    private String mShareContent;

    public ShareBuilder(Context context) {
        mContext = context;
    }

    public ShareBuilder withTitle(String title){
        mShareTitle = title;
        return this;
    }

    public ShareBuilder withContent(String content){
        mShareContent = content;
        return this;
    }

    public ShareBuilder withImg(Bitmap img){
        if (img != null) {
            mShareBitmap = new UMImage(mContext, img);
        }
        return this;
    }

    /**
     * 分享使用丽丽思icon
     * @return
     */
    public ShareBuilder withIcon(){
        mShareBitmap = new UMImage(mContext, R.mipmap.icon_launcher);
        return this;
    }

    public ShareBuilder withUrl(String url){
        mShareUrl = url;
        return this;
    }

    public void share(){
        CustomShareActivity.mShareBitmap = mShareBitmap;
        ARouter.getInstance().build("/share/CustomShareActivity")
                .withString("mShareTitle", mShareTitle)
                .withString("mShareUrl", mShareUrl)
                .withString("mShareContent", mShareContent)
                .navigation();
    }
}
