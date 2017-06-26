package com.youloft.lilith;

import android.app.Application;

import com.youloft.lilith.common.cache.CacheStore;
import com.youloft.lilith.common.utils.Utils;

import java.io.IOException;

/**
 * Application
 * <p>
 * Created by coder on 2017/6/26.
 */
public class AppEnv extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

    public static AppEnv getContext() {
        return null;
    }

    /**
     * 获取APi类别缓存
     *
     * @return
     */
    public static CacheStore getApiCache() {
        return CacheStore.getStore("api");
    }
}
