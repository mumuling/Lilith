package com.youloft.socialize.wrapper;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youloft.socialize.SocializePlatform;

import java.util.Map;

/**
 * Created by coder on 2017/6/28.
 */

public class AuthListenerWrapper implements UMAuthListener {

    private AuthListener mListener;

    public AuthListenerWrapper(AuthListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        if (mListener != null) {
            mListener.onStart(SocializePlatform.parseMedia(share_media));
        }
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        if (mListener != null) {
            mListener.onComplete(SocializePlatform.parseMedia(share_media), i, map);
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        if (mListener != null) {
            mListener.onError(SocializePlatform.parseMedia(share_media), i, throwable);
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        if (mListener != null) {
            mListener.onCancel(SocializePlatform.parseMedia(share_media), i);
        }
    }
}
