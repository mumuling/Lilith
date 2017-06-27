package com.youloft.lilith.common.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.youloft.lilith.LLApplication;

import java.util.HashMap;
import java.util.Locale;

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
    private static HashMap<String, String> mParams;
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
        mParams.put("CID", "Youloft_Android");
        mParams.put("CC", Locale.getDefault().getCountry());
        mParams.put("AV", AppEnv.getVersionName());
        mParams.put("MAC", AppEnv.getMacAddress());
        mParams.put("DID", AppEnv.getDeviceId());
        mParams.put("CHN", AppEnv.getChannel(AppEnv.getAppContext()));
        mParams.put("LANG", Locale.getDefault().getLanguage());
        mParams.put("BD", AppEnv.BUNDLE);
        mParams.put("T", String.valueOf(System.currentTimeMillis() / 1000));

    }


    private final Object tokenLock = new Object();

    public void updateToken() {
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
                            initPublicParam();
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

    public static HashMap<String, String> initParams(HashMap<String, String> params) {

        if (mParams == null || mParams.isEmpty()) {
            NetUtil.getInstance().initPublicParam();
        }
        if (!mParams.containsKey("TKN")) {
            if (mTokenStore != null && mTokenStore.getToken() != null) {
                mParams.put("TKN", mTokenStore.getToken());
            }
        }
        if (params == null) {
            return mParams;
        }
        params.putAll(mParams);
        return params;
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
