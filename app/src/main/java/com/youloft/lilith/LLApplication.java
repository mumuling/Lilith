package com.youloft.lilith;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.common.cache.CacheStore;
import com.youloft.lilith.common.net.AppEnv;
import com.youloft.lilith.common.net.NetUtil;
import com.youloft.lilith.common.utils.Util;
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
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        AppEnv.setAppContext(this);
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
     * 设置渠道标识
     */
    private void setChannelBundle() {
        String channel = AppEnv.getChannel(getApplicationContext());
        //友盟统计参数
//        MobclickAgent.UMAnalyticsConfig umAnalyticsConfig = new MobclickAgent.UMAnalyticsConfig(getInstance(), "528d799b56240b16d814d97f", channel, MobclickAgent.EScenarioType.E_UM_NORMAL, true);
//        MobclickAgent.startWithConfigure(umAnalyticsConfig);
//        //TC初始化
//        TCAgent.LOG_ON = true;
//        TCAgent.init(this, "1A3BC081BB9A442E8AFF29ACB08069E3", channel);
//        TCAgent.setReportUncaughtExceptions(true);
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
