package com.youloft.lilith;

import android.app.Application;

import com.umeng.socialize.Config;
import com.youloft.lilith.api.LilithApi;
import com.youloft.lilith.common.cache.CacheStore;
import com.youloft.lilith.common.net.APIFactory;
import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.common.utils.LocationUtil;
import com.youloft.lilith.common.utils.Utils;
import com.youloft.lilith.router.AppRouter;
import com.youloft.socialize.SocializeApp;

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
        //初始化工具类
        Utils.init(this);
        //初始化基础配置
        AppConfig.init(this);
        //初始化推送
//        initPush(this, AppConfig.UMENG_APPKEY, AppConfig.UMENG_PUSH_SECRET, AppConfig.CHANNEL);
        //初始化统计
//        AppAnalytics.init(this, AppConfig.TD_APPID, AppConfig.CHANNEL);
        //初始化页面路由
        AppRouter.init(this, AppConfig.DebugMode);
        initThirdSDK();
        //初始化在线参数
        OnlineConfigAgent
                .initConfig(CONFIG_APP_KEY, String.valueOf(AppConfig.VERSION_CODE))
                .loadPreloadConfig(this)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        if (integer == 2) {
                            //loaddefault config success;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //加载参数发生异常
                    }
                });
        LocationUtil.updateLocation();//后台更新定位缓存数据
    }

    private void initThirdSDK() {
        Config.DEBUG = true;
        //社交化&登录
        SocializeApp.setWeixin("wxf21154c0fd625f83", "8ca4c335d3f77292a7649797849b3777");
        SocializeApp.get(this);
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
     * 获取Lilith相关Api
     *
     * @return
     */
    public static LilithApi getLilithApi() {
        return APIFactory.createApiInstance(AppConfig.SERVER_LILITH, LilithApi.class);
    }

    /**
     * 初始化Push
     *
     * @param context
     * @param appKey
     * @param secret
     * @param channel
     */
//    private static void initPush(Context context, String appKey, String secret, String channel) {
//        PushApp.getInstance(context)
//                .setAppkeyAndSecret(appKey, secret)
//                .setMessageChannel(channel)
//                .registePushDevice()
//                .setMessageHandler(new PushMessageHandler())
//                .setNotificationClickHandler(new PushNotificationClickHandler());
//    }
}
