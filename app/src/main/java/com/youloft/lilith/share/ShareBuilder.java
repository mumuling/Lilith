package com.youloft.lilith.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.launcher.ARouter;
import com.umeng.socialize.media.UMImage;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.ViewUtil;

/**
 * Created by zchao on 2017/7/6.
 * desc: 分享builder。使用：     new ShareBuilder(this)                                      //创建一个builder，
 *                                                  .withTitle("title")                     //主题
 *                                                  .withUrl("https://www.baidu.com")       //链接
 *                                                  .withImg(null)
 *                                                  .withIcon()                             //图片跟icon只能有一个，后边设置的会覆盖前面的
 *                                                  .withContent("内容")
 *                                                  .share();                               //发起分享，拉起分享界面
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

    /**
     * 分享title
     *
     * @param title
     * @return
     */
    public ShareBuilder withTitle(String title) {
        mShareTitle = title;
        return this;
    }

    /**
     * 分享内容
     *
     * @param content
     * @return
     */
    public ShareBuilder withContent(String content) {
        mShareContent = content;
        return this;
    }

    /**
     * 分享带图片
     *
     * @param img
     * @return
     */
    public ShareBuilder withImg(Bitmap img) {
        if (img != null) {
            mShareBitmap = new UMImage(mContext, img);
        }
        return this;
    }

    /**
     * 分享使用丽丽思icon
     *
     * @return
     */
    public ShareBuilder withIcon() {
        mShareBitmap = new UMImage(mContext, R.mipmap.icon_launcher);
        return this;
    }

    /**
     * 分享带链接
     *
     * @param url
     * @return
     */
    public ShareBuilder withUrl(String url) {
        mShareUrl = url;
        return this;
    }

    /**
     * 开始分享
     */
    public void share() {
        CustomShareActivity.mShareBitmap = mShareBitmap;
        if (mContext instanceof BaseActivity) {
            CustomShareActivity.mBGBitmap = ViewUtil.blurBitmap(((BaseActivity) mContext).takeScreenShot(false), mContext);
        }
        ARouter.getInstance().build("/share/CustomShareActivity")
                .withString("mShareTitle", mShareTitle)
                .withString("mShareUrl", mShareUrl)
                .withString("mShareContent", mShareContent)
                .navigation();
    }
}
