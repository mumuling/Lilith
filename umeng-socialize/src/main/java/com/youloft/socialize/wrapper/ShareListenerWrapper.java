package com.youloft.socialize.wrapper;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youloft.socialize.SocializePlatform;

/**
 * Created by coder on 2017/6/28.
 */

public class ShareListenerWrapper implements UMShareListener {

    private ShareListener mShareListener;

    public ShareListenerWrapper(ShareListener listener) {
        this.mShareListener = listener;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        if (mShareListener != null) {
            mShareListener.onStart(SocializePlatform.parseMedia(share_media));
        }
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        if (mShareListener != null) {
            mShareListener.onResult(SocializePlatform.parseMedia(share_media));
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        if (mShareListener != null) {
            mShareListener.onError(SocializePlatform.parseMedia(share_media), throwable);
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        if (mShareListener != null) {
            mShareListener.onCancel(SocializePlatform.parseMedia(share_media));
        }
    }
}
