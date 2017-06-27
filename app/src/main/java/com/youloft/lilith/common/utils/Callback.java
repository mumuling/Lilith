package com.youloft.lilith.common.utils;

/**
 * Created by coder on 16/4/21.
 */
public interface Callback<T, E> {
    T call(E t);
}
