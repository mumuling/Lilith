package com.youloft.lilith.setting;


import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.common.provider.SettingProvider;
import com.youloft.lilith.login.bean.UserBean;

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
     *
     * @param userInfo
     */
    public static void saveUserInfo(UserBean userInfo) {
        String userInfoJson = JSON.toJSONString(userInfo);
        SettingProvider.save(sContext, "user_login_info", userInfoJson);
    }

    /**
     * 清除用户登录信息
     */
    public static void clearUserInfo() {
        SettingProvider.save(sContext, "user_login_info", "");
    }

    /**
     * 检查user登录状态
     *
     * @return 返回为null是表示没有登录
     */
    public static UserBean getUserInfo() {
        String userInfo = SettingProvider.getString(sContext, "user_login_info", null);
        JSONObject parse = (JSONObject) JSON.parse(userInfo);
        UserBean userBean = JSONObject.toJavaObject(parse, UserBean.class);
        if (userBean == null ||
                userBean.data == null ||
                userBean.data.userInfo == null ||
                userBean.data.userInfo.id == 0) {
            return null;
        }
        return userBean;
    }

    /**
     * 检查user信息是否完整，服务器就是要这样玩
     *
     * @return 返回为NULL时，表示用户信息不完整
     */
    public static UserBean userDataIsComplete() {
        UserBean userInfo = getUserInfo();
        if (userInfo == null ||
                TextUtils.isEmpty(userInfo.data.userInfo.birthLongi) ||
                TextUtils.isEmpty(userInfo.data.userInfo.birthLati)) {
            return null;
        }
        return userInfo;
    }

    /**
     * 城市数据库版本
     *
     * @param version
     */
    public static void saveCityDBVersion(int version) {
        SettingProvider.save(sContext, "city_info_db_version", version);
    }
    /**
     * 城市数据库版本
     *
     */
    public static int getCityDBVersion() {
        return SettingProvider.getInt(sContext, "city_info_db_version", 0);
    }
}
