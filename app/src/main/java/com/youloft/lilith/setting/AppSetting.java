package com.youloft.lilith.setting;


import android.content.Context;
import android.text.TextUtils;

import com.youloft.lilith.AppConfig;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.common.provider.SettingProvider;

/**
 * Desc:
 * Change:
 * Setting从SettingProvider获取
 *
 * @author zchao created at 2017/6/26 13:28
 * @see
 */

public class AppSetting {

    private static Context sContext = LLApplication.getInstance();

    public static int getVersionCode() {
        return SettingProvider.getInt(sContext, "app_versioncode", 0);
    }

    /**
     * 安装应用保存当前版本的版本号等信息
     */
    public static void install() {
        SettingProvider.save(sContext, "app_versioncode", AppConfig.VERSION_CODE);
    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceId() {
        String did = SettingProvider.getString(sContext, "app_device_did", null);
        if (TextUtils.isEmpty(did)) {
            did = com.ut.device.UTDevice.getUtdid(sContext);
            SettingProvider.save(sContext, "app_device_did", did);
        }
        return did;
    }

    /**
     * 登录成功之后 存入返回的user 信息
     * @param userInfo
     */
    public static void saveUserInfo(String userInfo){
        SettingProvider.save(sContext,"user_login_info",userInfo);
    }

    /**
     * 获取登录成功后的信息
     * @return
     */
    public static String getUserInfo(){
        return SettingProvider.getString(sContext,"user_login_info",null);
    }

}
