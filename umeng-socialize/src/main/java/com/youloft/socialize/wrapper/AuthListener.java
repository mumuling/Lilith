package com.youloft.socialize.wrapper;

import com.youloft.socialize.SocializePlatform;

import java.util.Map;

/**
 * 认证Listener用于Wrapper
 * Created by coder on 2017/6/28.
 */
public interface AuthListener {
    int ACTION_AUTHORIZE = 0;
    int ACTION_DELETE = 1;
    int ACTION_GET_PROFILE = 2;

    void onStart(SocializePlatform platform);

    void onComplete(SocializePlatform platform, int code, Map<String, String> data);

    void onError(SocializePlatform platform, int code, Throwable err);

    void onCancel(SocializePlatform platform, int code);
}
