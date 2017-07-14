package com.youloft.lilith.share;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.socialize.media.ShareImage;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * Created by zchao on 2017/7/6.
 * desc: 分享builder。使用：     new ShareBuilder(this)                                      //创建一个builder，
 * .withTitle("title")                     //主题
 * .withUrl("https://www.baidu.com")       //链接
 * .withImg(null)
 * .withIcon()                             //图片跟icon只能有一个，后边设置的会覆盖前面的
 * .withContent("内容")
 * .share();                               //发起分享，拉起分享界面
 * version:
 */

public class ShareBuilder {
    private String mShareTitle;
    private String mShareUrl;
    private ShareImage mShareBitmap;
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
            mShareBitmap = new ShareImage(mContext, img);
        }
        return this;
    }

    /**
     * 分享使用丽丽思icon
     *
     * @return
     */
    public ShareBuilder withIcon() {
        mShareBitmap = new ShareImage(mContext, R.mipmap.ic_luncher);
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
        ShareActivity.mShareBitmap = mShareBitmap;
        if (mContext instanceof BaseActivity) {
            Bitmap source = ((BaseActivity) mContext).takeScreenShot(false, 0);
            BlurFactor bf = new BlurFactor();
            bf.sampling = 10;
            bf.radius = 10;
            bf.width = source.getWidth();
            bf.height = source.getHeight();
            ShareActivity.mBGBitmap = Blur.of(mContext, source, bf);
        }
        ARouter.getInstance().build("/ui/share")
                .withString("title", mShareTitle)
                .withString("url", mShareUrl)
                .withString("content", mShareContent)
                .navigation();
    }
}
