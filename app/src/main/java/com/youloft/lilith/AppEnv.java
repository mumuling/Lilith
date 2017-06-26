package com.youloft.lilith;

import android.app.Application;

import com.youloft.lilith.common.utils.Utils;

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
}
