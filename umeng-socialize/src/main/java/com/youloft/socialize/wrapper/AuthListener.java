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

    void onStart(SocializePlatform var1);

    void onComplete(SocializePlatform var1, int var2, Map<String, String> var3);

    void onError(SocializePlatform var1, int var2, Throwable var3);

    void onCancel(SocializePlatform var1, int var2);
}
