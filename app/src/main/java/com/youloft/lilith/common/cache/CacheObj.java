package com.youloft.lilith.common.cache;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 缓存对象
 * Created by coder on 2017/6/26.
 */
public class CacheObj<T> {
    @JSONField(name = "data")
    public T data;

    @JSONField(name = "cacheTime")
    public long cacheStamp = System.currentTimeMillis();

    @JSONField(name = "cacheKey")
    public String key;

    public CacheObj(String key, T data) {
        this.key = key;
        this.data = data;
    }


}
