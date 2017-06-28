package com.youloft.socialize.wrapper;

import com.youloft.socialize.SocializePlatform;

/**
 * Created by coder on 2017/6/28.
 */

public interface ShareListener {
    void onStart(SocializePlatform share_media);

    void onResult(SocializePlatform share_media);

    void onError(SocializePlatform share_media, Throwable throwable);

    void onCancel(SocializePlatform share_media);

}
