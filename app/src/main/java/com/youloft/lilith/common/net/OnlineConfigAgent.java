package com.youloft.lilith.common.net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youloft.lilith.common.utils.Callback;
import com.youloft.lilith.common.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okio.BufferedSource;
import okio.Okio;

/**
 * 在线参数
 * Created by coder on 16/4/21.
 */
public class OnlineConfigAgent {
    private static final String CFG_APP_KEY = "android-wnl-tv";
    public static final String CFG_FL_KEY = "Festival_BEF_FL";
    private static String CFG_APP_VER = "2";

    private static final String TAG = "OnlineConfigAgent";
    //最后更新的版本号
    private static final String KEY_LASTUPDATE = "online_lastupdate";
    //最后一次更新
    private static final String KEY_LASTREQUEST = "online_lastrequest";
    private static final String KEY_LASTREQUEST_VER = "online_lastrequest_ver";

    //请求间隔时间
    private static final long EXPIRED_REQUEST = 600000;
    /**
     * 用于缓存
     * <p/>
     * 主要优化目的
     * 用于优化多次重复获取相同KEY的缓存
     */
    private static HashMap<String, Object> sConfigCache = new HashMap<String, Object>();
    private static OnlineConfigAgent sAgentInstance = null;
    boolean NetworkRecievered = false;

    private OnlineConfigAgent() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static OnlineConfigAgent getInstance() {
        if (sAgentInstance == null) {
            synchronized (OnlineConfigAgent.class) {
                if (sAgentInstance == null) {
                    sAgentInstance = new OnlineConfigAgent();
                }
            }
        }
        return sAgentInstance;
    }



    /**
     * 加载预置配置
     *
     * @param context
     * @param callback
     */
    public void loadPreloadConfig(Context context, final Callback<Void, Integer> callback) {
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        final Context finalContext = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedSource buffer = null;
                try {
                    SharedPreferences sp = ConfigStore.getInstance(finalContext).getSharedPreferences();
                    String lastUpdateVer = sp.getString(KEY_LASTUPDATE, null);
                    if (lastUpdateVer != null) {
                        return;
                    }
                    InputStream inputStream = finalContext.getAssets().open("preload.json");
                    buffer = Okio.buffer(Okio.source(inputStream));
                    String str = buffer.readUtf8();
                    JSONObject object = JSON.parseObject(str);
                    SharedPreferences.Editor editor = sp.edit();
                    for (String key : object.keySet()) {
                        if (sp.contains(key)) {
                            continue;
                        }
                        editor.putString(key, Utils.base64Decode(object.getString(key)));
                    }
                    editor.putString(KEY_LASTUPDATE, "0");
                    if (!NetworkRecievered) {
                        editor.commit();
                        if (callback != null) {
                            callback.call(1);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (buffer != null)
                            buffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    boolean isUpdating = false;

    /**
     * 更新在线参数
     *
     * @param context
     */
    public void updateOnlineConfig(Context context) {
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        if (isUpdating)
            return;
        isUpdating = true;
        final Context finalContext = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    updateOnlineConfigFromNetwork(finalContext);
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    isUpdating = false;
                }
            }
        }).start();
    }

    /**
     * 从网络上更新配置项
     * 待修改
     *
     * @param context
     */
    private void updateOnlineConfigFromNetwork(Context context) throws Throwable {
        SharedPreferences sharedPreferences = ConfigStore.getInstance(context).getSharedPreferences();
        String lastUpdateVer = sharedPreferences.getString(KEY_LASTUPDATE, null);
        long lastRequestTime = sharedPreferences.getLong(KEY_LASTREQUEST, 0);
        String lastVer = sharedPreferences.getString(KEY_LASTREQUEST_VER, "0");
        if (lastVer.equalsIgnoreCase(CFG_APP_VER)) {
            if (Math.abs(lastRequestTime - System.currentTimeMillis()) < EXPIRED_REQUEST) {
                return;
            }
        } else {
            lastUpdateVer = "";
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("appid", CFG_APP_KEY);
        params.put("appver", CFG_APP_VER);
        params.put("lastupdate", lastUpdateVer);
//        String url = URLFormatter.parseUrl(Urls.CONFIG, NetUtil.initParams(params));
        String data = WebUtils.getString(Urls.CONFIG, NetUtil.initParams(params));
        if (!TextUtils.isEmpty(data)) {
            ConfigResp configResp = JSON.parseObject(data, ConfigResp.class);
            if (configResp != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (configResp.status == 1 && configResp.data != null && !configResp.data.isEmpty()) {
                    NetworkRecievered = true;
                    JSONObject configData = configResp.data;
                    for (String jsonKey : configData.keySet()) {
                        editor.putString(jsonKey, Utils.base64Decode(configData.getString(jsonKey)));
                    }
                    sConfigCache.clear();
                    editor.putString(KEY_LASTUPDATE, configResp.r);
                    editor.putString(KEY_LASTREQUEST_VER, CFG_APP_VER);
                }
                editor.putLong(KEY_LASTREQUEST, System.currentTimeMillis());
                editor.apply();
            }
//            EventBus.getDefault().post(new ConfigEvent());
        }
    }

    /**
     * 获取ConfigParams
     *
     * @param context
     * @param key
     * @return
     */
    public String getConfigParams(Context context, String key, String defaultValue) {
        return ConfigStore.getInstance(context).getSharedPreferences().getString(key, defaultValue);
    }

    /**
     * 获取配置的JSONObject
     *
     * @param context
     * @param key
     * @return
     */
    public JSONObject getConfigParamsAsJSONObject(Context context, String key, String defaultValue) {
        JSONObject retObj = null;
        String cacheKey = "json_" + key;
        retObj = (JSONObject) sConfigCache.get(cacheKey);
        try {
            retObj = JSON.parseObject(getConfigParams(context, key, defaultValue));
        } catch (Throwable unused) {
            if (defaultValue != null) {
                try {
                    retObj = JSON.parseObject(defaultValue);
                } catch (Throwable un) {

                }
            }
        }
        if (retObj != null)
            sConfigCache.put(cacheKey, retObj);
        return retObj;
    }

    /**
     * 以JSONArray获取配置
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public JSONArray getConfigParamsAsJSONArray(Context context, String key, String defaultValue) {
        String cacheKey = "jsonarr_" + key;
        if (sConfigCache.containsKey(cacheKey)) {
            return (JSONArray) sConfigCache.get(cacheKey);
        } else {
            JSONArray object = JSON.parseArray(getConfigParams(context, key, defaultValue));
            if (object != null)
                sConfigCache.put(cacheKey, object);

            return object;
        }
    }


    /**
     * 以对象形式获取配置项
     *
     * @param context
     * @param key
     * @param defaultValue
     * @param clz
     * @return
     */
    public <T> T getConfigParamsAsObject(Context context, String key, String defaultValue, Class<T> clz) {
        String cacheKey = "obj_" + key;
        if (sConfigCache.containsKey(cacheKey)) {
            return (T) sConfigCache.get(cacheKey);
        } else {
            T object = JSON.parseObject(getConfigParams(context, key, defaultValue), clz);
            if (object != null)
                sConfigCache.put(cacheKey, object);
            return object;
        }
    }

    /**
     * 获取ConfigParams
     *
     * @param context
     * @param key
     * @return
     */
    public String getConfigParams(Context context, String key) {
        return getConfigParams(context, key, null);
    }

    /**
     * 获取配置的JSONObject
     *
     * @param context
     * @param key
     * @return
     */
    public JSONObject getConfigParamsAsJSONObject(Context context, String key) {
        return getConfigParamsAsJSONObject(context, key, null);
    }

    /**
     * 以JSONArray获取配置
     *
     * @param context
     * @param key
     * @return
     */
    public JSONArray getConfigParamsAsJSONArray(Context context, String key) {
        return getConfigParamsAsJSONArray(context, key, null);
    }


    /**
     * 以对象形式获取配置项
     *
     * @param context
     * @param key
     * @param clz
     * @return
     */
    public <T> T getConfigParamsAsObject(Context context, String key, Class<T> clz) {
        return getConfigParamsAsObject(context, key, null, clz);
    }

    /**
     * 配置存储工具类
     */
    static class ConfigStore {
        private static ConfigStore sStoreIntance = null;
        private Context mContext;
        private String KEY_PREF_NAME = "online_config_wnl";

        private ConfigStore(Context context) {
            if (context instanceof Activity) {
                context = context.getApplicationContext();
            }
            this.mContext = context;
        }

        /**
         * 获取实例
         *
         * @param context
         * @return
         */
        public static ConfigStore getInstance(Context context) {
            if (sStoreIntance == null) {
                synchronized (ConfigStore.class) {
                    if (sStoreIntance == null) {
                        sStoreIntance = new ConfigStore(context);
                    }
                }
            }
            return sStoreIntance;
        }


        /**
         * 获取SharePreferences
         *
         * @return
         */
        public SharedPreferences getSharedPreferences() {
            return mContext.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        }

    }

}
