package com.youloft.lilith.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.fastjson.JSON;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.common.net.AbsResponse;
import com.youloft.lilith.common.net.OkHttpUtils;
import com.youloft.lilith.common.rx.RxFlowableUtil;
import com.youloft.lilith.common.rx.exception.ContentNotChangeException;
import com.youloft.lilith.common.utils.NetUtil;

import org.reactivestreams.Publisher;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
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
public abstract class AbstractDataRepo implements IProvider {

    @Override
    public void init(Context context) {

    }

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
    public static <T extends AbsResponse> Flowable<T> unionFlow(String url,
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
     * 为RetrofitSevice添加缓存
     *
     * @param observable
     * @param clz
     * @param cacheKey
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> unionRetrofit(Observable<T> observable, Class<T> clz, String cacheKey, long duration) {
        return Flowable
                .concat(cacheFlow(cacheKey, clz),
                        wrapCacheObservable(observable, cacheKey, duration)
                );
    }


    /**
     * 包
     *
     * @param observable
     * @param cacheKey
     * @param cacheDuration
     * @param <T>
     * @return
     */
    private static <T> Publisher<? extends T> wrapCacheObservable(final Observable<T> observable, final String cacheKey, final long cacheDuration) {
        Flowable<T> compose = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                if (TextUtils.isEmpty(cacheKey)) {
                    e.onNext(true);
                    return;
                }
                if (!NetUtil.isNetOK()) {
                    e.onNext(null);
                    return;
                }

                if (LLApplication.getApiCache().isExpired(cacheKey, cacheDuration)) {
                    e.onNext(true);
                    return;
                }
                e.onNext(false);
            }
        }).compose(new ObservableTransformer<Boolean, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<Boolean> upstream) {
                Boolean aBoolean = upstream.blockingFirst();
                if (aBoolean == null || !aBoolean) {
                    return Observable.empty();
                }
                return observable;
            }
        }).toFlowable(BackpressureStrategy.DROP)
                .compose(RxFlowableUtil.<T>applyNetIoSchedulers())//线程切换
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
        return LLApplication.getApiCache().readCache(key, clz);

    }

    /**
     * post请求
     *
     * @param url
     * @param header
     * @param params
     * @param usePublic
     * @param clz
     * @param cacheKey
     * @param cacheDuration
     * @param files         上传的文件
     * @param <T>
     * @return
     */
    public static <T extends AbsResponse> Flowable<T> post(final String url,
                                                           final Map<String, String> header,
                                                           final Map<String, String> params,
                                                           final boolean usePublic,
                                                           final Class<T> clz,
                                                           final String cacheKey,
                                                           final long cacheDuration,
                                                           final File... files) {
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
                })
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(@NonNull String s) throws Exception {
                        return OkHttpUtils.getInstance().post(url, header, params, usePublic, files);
                    }
                })
                .compose(RxFlowableUtil.<Response>applyNetIoSchedulers())//线程切换
                .map(new Function<Response, T>() {
                    @Override
                    public T apply(@NonNull Response response) throws Exception {
                        if (response.code() == 200) {
                            String string = response.body().string();
                            T t = JSON.parseObject(string, clz);
                            if (t.status == 200 && t.data != null) {
                                return t;
                            }
                            return null;
                        } else {
                            throw new RuntimeException("No Content");
                        }
                    }
                })
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
    public static <T extends AbsResponse> Flowable<T> httpFlow(final String url,
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
                })
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(@NonNull String s) throws Exception {
                        return OkHttpUtils.getInstance().requestExecute(url, header, params, usePublic);
                    }
                })
                .compose(RxFlowableUtil.<Response>applyNetIoSchedulers())//线程切换
                .map(new Function<Response, T>() {
                    @Override
                    public T apply(@NonNull Response response) throws Exception {
                        if (response.code() == 200) {
                            String string = response.body().string();
                            T t = JSON.parseObject(string, clz);
                            if (t.status == 200 && t.data != null) {
                                return t;
                            }
                            return null;
                        } else {
                            throw new RuntimeException("No Content");
                        }
                    }
                })
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
