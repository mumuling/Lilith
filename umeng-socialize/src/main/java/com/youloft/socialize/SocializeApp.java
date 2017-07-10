package com.youloft.socialize;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;
import com.youloft.socialize.wrapper.AuthListener;
import com.youloft.socialize.wrapper.AuthListenerWrapper;

/**
 * 分享
 * <p>
 * Created by coder on 2017/6/28.
 */
public class SocializeApp {

    private UMShareAPI mShareAPI;

    private SocializeApp(Context context) {
        this.mShareAPI = UMShareAPI.get(context);
    }

    //delegate from PlatformConfig
    public static void setQQZone(String id, String key) {
        PlatformConfig.setQQZone(id, key);
    }

    public static void setSinaWeibo(String key, String secret, String redirectUrl) {
        PlatformConfig.setSinaWeibo(key, secret, redirectUrl);
    }


    public static void setWeixin(String id, String secret) {
        PlatformConfig.setWeixin(id, secret);
    }

    public static PlatformConfig.Platform getPlatform(SHARE_MEDIA name) {
        return PlatformConfig.getPlatform(name);
    }

    private static SocializeApp singleton;

    public static SocializeApp get(Context context) {
        if (singleton == null) {
            synchronized (SocializeApp.class) {
                if (singleton == null) {
                    if (context instanceof Activity) {
                        context = context.getApplicationContext();
                    }
                    singleton = new SocializeApp(context);
                }
            }
        }
        return singleton;
    }


    public void doOauthVerify(Activity activity, SocializePlatform platform, AuthListener listener) {
        mShareAPI.doOauthVerify(activity, platform.toShareMedia(), new AuthListenerWrapper(listener));
    }

    public void deleteOauth(Activity context, SocializePlatform platform, AuthListener listener) {
        mShareAPI.deleteOauth(context, platform.toShareMedia(), new AuthListenerWrapper(listener));
    }

    public void getPlatformInfo(Activity context, SocializePlatform platform, AuthListener listener) {
        mShareAPI.getPlatformInfo(context, platform.toShareMedia(), new AuthListenerWrapper(listener));
    }

    public boolean isInstall(Activity context, SocializePlatform platform) {
        return mShareAPI.isInstall(context, platform.toShareMedia());
    }

    public boolean isAuthorize(Activity context, SocializePlatform platform) {
        return mShareAPI.isAuthorize(context, platform.toShareMedia());
    }

    public boolean isSupport(Activity context, SocializePlatform platform) {
        return mShareAPI.isSupport(context, platform.toShareMedia());
    }

    public String getversion(Activity context, SocializePlatform platform) {
        return mShareAPI.getversion(context, platform.toShareMedia());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    public void release() {
        mShareAPI.release();
    }

    public void onSaveInstanceState(Bundle bundle) {
        mShareAPI.onSaveInstanceState(bundle);
    }

    public void fetchAuthResultWithBundle(Activity context, Bundle bundle, AuthListener listener) {
        mShareAPI.fetchAuthResultWithBundle(context, bundle, new AuthListenerWrapper(listener));
    }

    public void setShareConfig(SocializeConfig config) {
        if (config == null) {
            return;
        }
        mShareAPI.setShareConfig(config.toShareConfig());
    }


    /**
     * 设置友盟AppKey
     *
     * @param appKey
     */
    public static void setAppKey(String appKey) {
        SocializeConstants.APPKEY = appKey;
    }
}
