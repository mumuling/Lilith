package com.youloft.lilith.common.rx;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * RxObserver
 * <p>
 * Created by coder on 2017/6/29.
 */

public abstract class RxObserver<T> implements Observer<T> {
    private Disposable d;


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(@NonNull T t) {
        onDataSuccess(t);
    }


    @Override
    public void onError(@NonNull Throwable e) {
        onFailed(e);
    }

    protected void onFailed(Throwable e) {
        Log.e("RxObserver", "onFailed", e);
    }


    @Override
    public void onComplete() {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }


    public abstract void onDataSuccess(T t);
}
