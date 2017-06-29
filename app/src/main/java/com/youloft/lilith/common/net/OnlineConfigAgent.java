package com.youloft.lilith.common.net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.utils.Encryption;

import java.io.InputStream;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okio.BufferedSource;
import okio.Okio;

/**
 * 在线参数
 * Created by coder on 16/4/21.
 */
public class OnlineConfigAgent {
    private final String appKey;
    private final String appVer;
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

    private OnlineConfigAgent(String appKey, String appVer) {
        this.appKey = appKey;
        this.appVer = appVer;
    }


    /**
     * 初始化Config
     */
    public static OnlineConfigAgent initConfig(String appKey, String appVer) {
        if (sAgentInstance == null) {
            synchronized (OnlineConfigAgent.class) {
                if (sAgentInstance == null) {
                    sAgentInstance = new OnlineConfigAgent(appKey, appVer);
                }
            }
        }
        return sAgentInstance;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static OnlineConfigAgent getInstance() {
        if (sAgentInstance == null) {
            throw new RuntimeException("must be init first");
        }
        return sAgentInstance;

    }


    /**
     * 加载预置配置
     *
     * @param context
     */
    public Observable<Integer> loadPreloadConfig(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                try {
                    Context ctx = context;
                    if (context instanceof Activity) {
                        ctx = context.getApplicationContext();
                    }
                    BufferedSource buffer = null;
                    SharedPreferences sp = ConfigStore.getInstance(ctx).getSharedPreferences();
                    String lastUpdateVer = sp.getString(KEY_LASTUPDATE, null);
                    if (lastUpdateVer != null) {
                        e.onNext(2);//已经从网络更新
                        e.onComplete();
                        return;
                    }
                    InputStream inputStream = ctx.getAssets().open("preload.json");
                    buffer = Okio.buffer(Okio.source(inputStream));
                    String str = buffer.readUtf8();
                    JSONObject object = JSON.parseObject(str);
                    SharedPreferences.Editor editor = sp.edit();
                    for (String key : object.keySet()) {
                        if (sp.contains(key)) {
                            continue;
                        }
                        editor.putString(key, Encryption.decodeBase64(object.getString(key)));
                    }
                    editor.putString(KEY_LASTUPDATE, "0");
                    if (!NetworkRecievered) {
                        editor.commit();
                        e.onNext(1);//读取预置数据成功
                        e.onComplete();
                        return;
                    }
                } catch (Throwable e1) {
                    e.onError(e1);//发生异常扔回
                    e.onComplete();
                }

            }
        });
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
        if (lastVer.equalsIgnoreCase(appVer)) {
            if (Math.abs(lastRequestTime - System.currentTimeMillis()) < EXPIRED_REQUEST) {
                return;
            }
        } else {
            lastUpdateVer = "";
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("appid", appKey);
        params.put("appver", appVer);
        params.put("lastupdate", lastUpdateVer);
//        String url = URLFormatter.parseUrl(Urls.CONFIG, NetUtil.initParams(params));
        String data = OkHttpUtils.getInstance().getString(Urls.CONFIG, params, true);
        if (!TextUtils.isEmpty(data)) {
            ConfigResp configResp = JSON.parseObject(data, ConfigResp.class);
            if (configResp != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (configResp.status == 1 && configResp.data != null && !configResp.data.isEmpty()) {
                    NetworkRecievered = true;
                    JSONObject configData = configResp.data;
                    for (String jsonKey : configData.keySet()) {
                        editor.putString(jsonKey, Encryption.decodeBase64(configData.getString(jsonKey)));
                    }
                    sConfigCache.clear();
                    editor.putString(KEY_LASTUPDATE, configResp.r);
                    editor.putString(KEY_LASTREQUEST_VER, appVer);
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
        private String KEY_PREF_NAME = "online_config";

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


    /**
     * 响应实体
     */
    static class ConfigResp {
        @JSONField(name = "status")
        public int status;

        @JSONField(name = "msg")
        public JSONObject data;

        @JSONField(name = "r")
        public String r;
    }
}
