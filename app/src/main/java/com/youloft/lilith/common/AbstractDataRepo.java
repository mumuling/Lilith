package com.youloft.lilith.common;

import com.alibaba.fastjson.JSON;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.common.net.OkHttpUtils;
import com.youloft.lilith.common.rx.RxFlowableUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
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
                .concat(cacheFlow(cacheKey, clz, cacheDuration),
                        httpFlow(url, header, params, usePublic, clz, cacheKey));
    }


    /**
     * 缓存流
     *
     * @param key
     * @param clz
     * @param duration
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> cacheFlow(String key, Class<T> clz, long duration) {
        return LLApplication.getApiCache()
                .<T>readCache(key, clz, LLApplication.getApiCache().isExpiredPredicate(duration));

    }

    /**
     * 处理Http请求
     *
     * @param url
     * @param header
     * @param params
     * @param usePublic
     * @param cacheKey
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> httpFlow(final String url,
                                           final Map<String, String> header,
                                           final Map<String, String> params,
                                           final boolean usePublic,
                                           final Class<T> clz,
                                           final String cacheKey) {
        return Flowable.create(new FlowableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Response> e) throws Exception {
                try {
                    Response response = OkHttpUtils.getInstance().requestExecute(url, header, params, usePublic);
                    e.onNext(response);
                    e.onComplete();
                } catch (Throwable throwable) {
                    e.onError(throwable);
                }
            }
        }, BackpressureStrategy.DROP).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                return response != null;
            }
        }).map(new Function<Response, T>() {
            @Override
            public T apply(@NonNull Response response) throws Exception {
                String string = response.body().string();
                return JSON.parseObject(string, clz);
            }
        }).onErrorReturn(new Function<Throwable, T>() {
            @Override
            public T apply(@NonNull Throwable throwable) throws Exception {
                return LLApplication.getApiCache().getCacheObjSync(cacheKey, clz);
            }
        }).compose(RxFlowableUtil.<T>applyNetIoSchedulers())//切换线程
                .compose(LLApplication.getApiCache().<T>cacheTransform(cacheKey));//写入缓存;

//        return Flowable.create(new ObservableOnSubscribe<Response>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
//                try {
//                    Response response = OkHttpUtils.getInstance().requestExecute(url, header, params, usePublic);
//                    e.onNext(response);
//                    e.onComplete();
//                } catch (Throwable throwable) {
//                    e.onError(throwable);
//                }
//            }
//        }).map(new Function<Response, T>() {
//            @Override
//            public T apply(@NonNull Response response) throws Exception {
//                String string = response.body().string();
//                return JSON.parseObject(string, clz);
//            }
//        }).toFlowable(BackpressureStrategy.BUFFER)
//                .compose(RxFlowableUtil.<T>applyNetIoSchedulers())//切换线程
//                .compose(LLApplication.getApiCache().<T>cacheTransform(cacheKey));//写入缓存
    }


}
