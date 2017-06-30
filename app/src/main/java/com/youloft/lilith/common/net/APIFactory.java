package com.youloft.lilith.common.net;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.FastJsonConvertFactory;

/**
 * API工厂类用于产生相应接口的实例
 * <p>
 * Created by coder on 2017/6/30.
 */
public class APIFactory {

    private static HashMap<Class<?>, Object> sApiInterfaceCache = new HashMap<>();

    /**
     * 创建Api实例
     *
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T createApiInstance(String baseUrl,
                                          Class<T> clz) {
        if (!sApiInterfaceCache.containsKey(clz)) {
            synchronized (APIFactory.class) {
                if (!sApiInterfaceCache.containsKey(clz)) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(FastJsonConvertFactory.create())
                            .client(OkHttpUtils.getInstance().getClient())
                            .build();
                    T apiObj = retrofit.create(clz);
                    if (apiObj != null) {
                        sApiInterfaceCache.put(clz, apiObj);
                    }
                }
            }
        }
        return (T) sApiInterfaceCache.get(clz);

    }
}
