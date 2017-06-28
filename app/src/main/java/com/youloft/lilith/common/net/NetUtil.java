package com.youloft.lilith.common.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.youloft.lilith.LLApplication;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Desc: 网络请求工具类，包括公共参数的封装
 * Change:
 *
 * @author zchao created at 2017/6/26 14:10
 * @see
 */

public class NetUtil {
    private static HashMap<String, String> mParams = null;
    private static TokenStore mTokenStore;
    private static NetUtil instance = null;

    /**
     * 公共参数封装
     *
     * @return
     */
    public void initPublicParam() {
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        if (mTokenStore == null) {
            mTokenStore = new TokenStore(AppEnv.getAppContext());
            updateToken();
        }
        if (mTokenStore.getToken() != null) {
            mParams.put("tkn", mTokenStore.getToken());
        }
        mParams.put("cid", "Youloft_Android");
        mParams.put("cc", Locale.getDefault().getCountry());
        mParams.put("av", AppEnv.getVersionName());
        mParams.put("mac", AppEnv.getMacAddress());
        mParams.put("did", AppEnv.getDeviceId());
        mParams.put("chn", AppEnv.getChannel(AppEnv.getAppContext()));
        mParams.put("lang", Locale.getDefault().getLanguage());
        mParams.put("bd", AppEnv.BUNDLE);
        mParams.put("t", String.valueOf(System.currentTimeMillis() / 1000));

    }

    public HashMap<String, String> getParams() {
        if (mParams == null || mParams.isEmpty()) {
            NetUtil.getInstance().initPublicParam();
        }
        if (!mParams.containsKey("tkn")) {
            if (mTokenStore != null) {
                if (mTokenStore.getToken() != null) {
                    mParams.put("tkn", mTokenStore.getToken());
                } else {
                    updateToken();
                }
            }
        }
        return mParams;
    }

    private final Object tokenLock = new Object();

    public synchronized void updateToken() {
        synchronized (tokenLock) {
            io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    boolean b = mTokenStore.loadToken();
                    e.onNext(b);
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            if (mTokenStore != null && mTokenStore.getToken() != null) {
                                mParams.put("tkn", mTokenStore.getToken());
                            }
                        }
                    });
        }
    }

    public static NetUtil getInstance() {
        if (instance == null) {
            instance = new NetUtil();
        }
        return instance;
    }

    /**
     * 获取网络类型名字
     *
     * @param context
     * @return
     */
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = "disconnect";
        if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        }

        String typeName = networkInfo.getTypeName();
        if ("WIFI".equalsIgnoreCase(typeName)) {
            type = "wifi";
        } else {
            type = "MOBILE";
        }
        return type;
    }


    /**
     * 网络是否OK
     *
     * @return
     */
    public static boolean isNetOK() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 获取活动的网络
     *
     * @return
     */
    public static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) LLApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }
}
