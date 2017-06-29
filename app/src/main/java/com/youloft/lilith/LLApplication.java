package com.youloft.lilith;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.common.cache.CacheStore;
import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.common.utils.Utils;
import com.youloft.lilith.push.PushMessageHandler;
import com.youloft.lilith.push.PushNotificationClickHandler;
import com.youloft.lilith.router.AppRouter;
import com.youloft.push.PushApp;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.youloft.lilith.AppConfig.CONFIG_APP_KEY;

/**
 * Application
 * <p>
 * Created by coder on 2017/6/26.
 */
public class LLApplication extends Application {


    private static LLApplication sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Utils.init(this);
        AppConfig.init(this);
        //初始化页面路由
        AppRouter.init(this, AppConfig.DebugMode);
        //初始化在线参数
        OnlineConfigAgent
                .initConfig(CONFIG_APP_KEY, String.valueOf(AppConfig.VERSION_CODE))
                .loadPreloadConfig(this)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }


    public static LLApplication getInstance() {
        return sInstance;
    }

    /**
     * 获取APi类别缓存
     *
     * @return
     */
    public static CacheStore getApiCache() {
        return CacheStore.getStore("api");
    }

    /**
     * 初始化Push
     *
     * @param context
     * @param appKey
     * @param secret
     * @param channel
     */
    private static void initPush(Context context, String appKey, String secret, String channel) {
        PushApp.getInstance(context)
                .setAppkeyAndSecret(appKey, secret)
                .setMessageChannel(channel)
                .registePushDevice()
                .setMessageHandler(new PushMessageHandler())
                .setNotificationClickHandler(new PushNotificationClickHandler());
    }
}
