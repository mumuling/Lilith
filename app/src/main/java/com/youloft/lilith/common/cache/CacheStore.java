package com.youloft.lilith.common.cache;

import android.text.TextUtils;
import android.util.LruCache;

import com.alibaba.fastjson.JSON;
import com.youloft.lilith.common.cache.disk.DiskLruCache;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 缓存存储器
 * <p>
 * Created by coder on 2017/6/26.
 */
public class CacheStore {

    private DiskLruCache mDiskCache;

    private LruCache<String, Object> mMemoryCache;

    public CacheStore(File cacheDir) throws IOException {
        mDiskCache = DiskLruCache.open(cacheDir, 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
        mMemoryCache = new LruCache<>(500);
    }

    /**
     * 缓存一个对象
     *
     * @param key
     * @param object
     */
    public <T> Observable<CacheObj<T>> putCacheObj(String key, T object) {
        CacheObj<T> cacheObj = new CacheObj<T>(key, object);
        mMemoryCache.put(key, cacheObj);
        return Observable.just(cacheObj).map(new Function<CacheObj<T>, CacheObj<T>>() {
            @Override
            public CacheObj<T> apply(@NonNull CacheObj<T> tCacheObj) throws Exception {
                //放入缓存
                DiskLruCache.Editor editor = mDiskCache.edit(tCacheObj.key);
                File cacheFile = editor.getFile(0);
                editor.set(0, JSON.toJSONString(tCacheObj.data));
                editor.commit();
                cacheFile.setLastModified(tCacheObj.cacheStamp);
                return tCacheObj;
            }
        });
    }


    /**
     * 获取缓存对象
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> Observable<CacheObj<T>> getCacheObj(final String key, final Class<T> type) {
        return Observable.just(key).map(new Function<String, CacheObj<T>>() {
            @Override
            public CacheObj<T> apply(@NonNull String key) throws Exception {
                CacheObj<T> memCacheObj = (CacheObj<T>) mMemoryCache.get(key);
                if (memCacheObj != null) {
                    return memCacheObj;
                }
                String diskStr = mDiskCache.get(key).getString(0);
                long cacheTime = mDiskCache.get(key).getFile(0).lastModified();
                if (TextUtils.isEmpty(diskStr)) {
                    return null;
                }
                T o = JSON.parseObject(diskStr, type);
                CacheObj<T> retCache = new CacheObj<T>(key, o);
                retCache.cacheStamp = cacheTime;
                mMemoryCache.put(key, retCache);
                return retCache;
            }
        });
    }


    /**
     * 是否过期
     *
     * @param key
     * @param deltaTime
     * @return
     */
    public boolean isExpired(String key, long deltaTime) {
        try {
            DiskLruCache.Value value = mDiskCache.get(key);
            return Math.abs(System.currentTimeMillis() - value.getFile(0).lastModified()) > deltaTime;
        } catch (Throwable e) {
            return true;
        }
    }

    /**
     * 直到某时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean isExpiredUntil(String key, long time) {
        try {
            DiskLruCache.Value value = mDiskCache.get(key);
            return value.getFile(0).lastModified() > time;
        } catch (Throwable e) {
            return true;
        }
    }

}
