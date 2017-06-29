package com.youloft.lilith.common;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.common.net.OkHttpUtils;
import com.youloft.lilith.common.rx.RxFlowableUtil;
import com.youloft.lilith.common.rx.exception.ContentNotChangeException;
import com.youloft.lilith.common.utils.NetUtil;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.Response;


/**
 * 数据仓库
 * 仅做为一个规范
 * Created by coder on 2017/6/29.
 */
public abstract class AbstractDataRepo {


    /**
     * 联合请求
     *
     * @param url
     * @param header
     * @param params
     * @param usePublic
     * @param clz
     * @param cacheKey
     * @param cacheDuration
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> unionFlow(String url,
                                            HashMap<String, String> header,
                                            HashMap<String, String> params,
                                            boolean usePublic,
                                            Class<T> clz,
                                            String cacheKey,
                                            long cacheDuration) {
        return Flowable
                .concat(cacheFlow(cacheKey, clz),
                        httpFlow(url, header, params, usePublic, clz, cacheKey, cacheDuration));
    }


    /**
     * 缓存流
     *
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> cacheFlow(String key, Class<T> clz) {
        if (TextUtils.isEmpty(key)) {
            return Flowable.empty();
        }
        return LLApplication.getApiCache()
                .<T>readCache(key, clz);

    }

    /**
     * 处理Http请求
     *
     * @param <T>
     * @param url
     * @param header
     * @param params
     * @param usePublic
     * @param cacheKey
     * @param cacheDuration
     * @return
     */
    public static <T> Flowable<T> httpFlow(final String url,
                                           final Map<String, String> header,
                                           final Map<String, String> params,
                                           final boolean usePublic,
                                           final Class<T> clz,
                                           final String cacheKey,
                                           final long cacheDuration) {
        Flowable<T> compose = Flowable.just(url)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        if (TextUtils.isEmpty(cacheKey)) {
                            return true;
                        }
                        if (!NetUtil.isNetOK()) {
                            throw new RuntimeException("Not Network");
                        }
                        return LLApplication.getApiCache().isExpired(cacheKey, cacheDuration);
                    }
                }).map(new Function<String, Response>() {
                    @Override
                    public Response apply(@NonNull String s) throws Exception {
                        return OkHttpUtils.getInstance().requestExecute(url, header, params, usePublic);
                    }
                }).filter(new Predicate<Response>() {
                    @Override
                    public boolean test(@NonNull Response response) throws Exception {
                        return response != null;
                    }
                }).map(new Function<Response, T>() {
                    @Override
                    public T apply(@NonNull Response response) throws Exception {
                        if (response.code() == 200) {
                            String string = response.body().string();
                            return JSON.parseObject(string, clz);
                        } else {
                            throw new RuntimeException("No Content");
                        }
                    }
                }).compose(RxFlowableUtil.<T>applyNetIoSchedulers())//切换线程
                .compose(LLApplication.getApiCache().<T>cacheTransform(cacheKey));

        if (TextUtils.isEmpty(cacheKey)
                || !LLApplication.getApiCache().hasCache(cacheKey)) {
            return compose;
        } else {
            return compose.compose(new FlowableTransformer<T, T>() {
                @Override
                public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                    return upstream.onErrorResumeNext(new Function<Throwable, Publisher<? extends T>>() {
                        @Override
                        public Publisher<? extends T> apply(@NonNull Throwable throwable) throws Exception {
                            Log.e(TAG, "rrr", throwable);
                            return Flowable.empty();
                        }
                    });
                }
            });
        }
    }

    private static final String TAG = "AbstractDataRepo";

}
