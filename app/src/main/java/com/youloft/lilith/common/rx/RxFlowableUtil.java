package com.youloft.lilith.common.rx;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by coder on 2017/6/29.
 */

public class RxFlowableUtil {
    /**
     * 应用IO线程调用
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applyIOSchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };


    }

    /**
     * 应用新线程调用
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applyThreadSchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 应用新线程调用
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applyNetIoSchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io());
            }
        };
    }


}
