package com.youloft.socialize;


import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 社交化平台包装类
 * Created by coder on 2017/6/28.
 */

public enum SocializePlatform {
    SINA(SHARE_MEDIA.SINA),
    QZONE(SHARE_MEDIA.QZONE),
    QQ(SHARE_MEDIA.QQ),
    WEIXIN(SHARE_MEDIA.WEIXIN),
    WEIXIN_CIRCLE(SHARE_MEDIA.WEIXIN_CIRCLE),
    WEIXIN_FAVORITE(SHARE_MEDIA.WEIXIN_FAVORITE);


    SHARE_MEDIA mRawValue;

    SocializePlatform(SHARE_MEDIA rawValue) {
        this.mRawValue = rawValue;
    }

    public SHARE_MEDIA toShareMedia() {
        return mRawValue;
    }

    /**
     * 转换Media
     *
     * @param share_media
     * @return
     */
    public static SocializePlatform parseMedia(SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.QZONE) {
            return SocializePlatform.QZONE;
        } else if (share_media == SHARE_MEDIA.QQ) {
            return QQ;
        } else if (share_media == SHARE_MEDIA.WEIXIN) {
            return WEIXIN;
        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            return WEIXIN_CIRCLE;
        } else if (share_media == SHARE_MEDIA.WEIXIN_FAVORITE) {
            return WEIXIN_FAVORITE;
        } else if (share_media == SHARE_MEDIA.SINA) {
            return SINA;
        }
        return null;
    }
}
