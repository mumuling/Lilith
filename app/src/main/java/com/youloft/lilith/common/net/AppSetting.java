package com.youloft.lilith.common.net;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Desc: 
 * Change: 
 * 
 * @version 
 * @author zchao created at 2017/6/26 13:28
 * @see 
*/

public class AppSetting {
    public static AppSetting mInstance = new AppSetting();
    private SharedPreferences mSharedPreference;

    private AppSetting() {
        mSharedPreference = AppEnv.getAppContext().getSharedPreferences("app_setting", Context.MODE_PRIVATE);
    }

    public static AppSetting getInstance() {
        return mInstance;
    }

    public String getString(String key) {
        return mSharedPreference.getString(key, null);
    }

    public void setString(String key, String value) {
        mSharedPreference.edit().putString(key, value).apply();
    }
}
