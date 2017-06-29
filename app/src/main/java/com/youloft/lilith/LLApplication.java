package com.youloft.lilith;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.common.cache.CacheStore;
import com.youloft.lilith.common.net.AppEnv;
//import com.youloft.lilith.common.net.NetUtil;
//import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.common.utils.Utils;
import com.youloft.lilith.push.PushMessageHandler;
import com.youloft.lilith.push.PushNotificationClickHandler;
import com.youloft.push.PushApp;

/**
 * Application
 * <p>
 * Created by coder on 2017/6/26.
 */
public class LLApplication extends Application {


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

    private static LLApplication mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppConfig.init(this);
        Utils.init(this);
        initARouter();
        setChannelBundle();
        NetUtil.getInstance().initPublicParam();
    }

    /**
     * 初始化ARouter
     */
    private void initARouter() {
        if (Util.isApkInDebug(mContext)) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }

    public static Context getContext() {
        return mContext;
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
