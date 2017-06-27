package com.youloft.lilith;

import android.app.Application;
import android.content.Context;

import com.youloft.lilith.common.cache.CacheStore;
import com.youloft.lilith.common.net.AppEnv;
import com.youloft.lilith.common.net.NetUtil;
import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.common.utils.Callback;
import com.youloft.lilith.common.utils.Utils;

/**
 * Application
 * <p>
 * Created by coder on 2017/6/26.
 */
public class LLApplication extends Application {
    private static LLApplication mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        AppEnv.setAppContext(this);

        setChannelBundle();
        //加载预置数据
        OnlineConfigAgent.getInstance().loadPreloadConfig(getApplicationContext(), new Callback<Void, Integer>() {
            @Override
            public Void call(Integer t) {
                if (t != null) {
                }
                return null;
            }
        });

        NetUtil.getInstance().initPublicParam();

        mContext = this;

        Utils.init(this);
    }

    public static Context getContext(){
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
