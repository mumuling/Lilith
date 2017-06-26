package com.youloft.lilith.common.cache;

import android.os.Environment;
import android.text.TextUtils;
import android.util.LruCache;

import com.alibaba.fastjson.JSON;
import com.youloft.lilith.common.cache.disk.DiskLruCache;
import com.youloft.lilith.common.utils.Utils;

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

    /**
     * 获取ApiStore
     *
     * @return
     * @throws IOException
     */
    public static CacheStore getStore(String type) {
        String defaultDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Utils.getContext().getExternalCacheDir() != null)
            defaultDir = Utils.getContext().getExternalCacheDir() + File.separator + type + File.separator;
        else {
            defaultDir = Utils.getContext().getCacheDir() + File.separator + type + File.separator;
        }
        File dirFile = null;
        try {
            dirFile = new File(defaultDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        } catch (Throwable e) {
            defaultDir = Utils.getContext().getCacheDir() + File.separator + type + File.separator;
            dirFile = new File(defaultDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }
        return new CacheStore(dirFile);
    }


    private DiskLruCache mDiskCache;

    private LruCache<String, Object> mMemoryCache;

    public CacheStore(File cacheDir) {
        try {
            mDiskCache = DiskLruCache.open(cacheDir, 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
        } catch (IOException e) {
            mDiskCache = null;
        }
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

                if (mDiskCache != null) {
                    //放入缓存
                    DiskLruCache.Editor editor = mDiskCache.edit(tCacheObj.key);
                    File cacheFile = editor.getFile(0);
                    editor.set(0, JSON.toJSONString(tCacheObj.data));
                    editor.commit();
                    cacheFile.setLastModified(tCacheObj.cacheStamp);
                }
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
                CacheObj<T> retCache = (CacheObj<T>) mMemoryCache.get(key);
                if (retCache != null) {
                    return retCache;
                }
                if (mDiskCache != null) {
                    String diskStr = mDiskCache.get(key).getString(0);
                    long cacheTime = mDiskCache.get(key).getFile(0).lastModified();
                    if (TextUtils.isEmpty(diskStr)) {
                        return null;
                    }
                    T o = JSON.parseObject(diskStr, type);
                    retCache.cacheStamp = cacheTime;
                    mMemoryCache.put(key, retCache);
                }
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

            CacheObj o = (CacheObj) mMemoryCache.get(key);
            if (o != null) {
                return Math.abs(System.currentTimeMillis() - o.cacheStamp) > deltaTime;
            }
            if (mDiskCache == null) {
                return true;
            }
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
            CacheObj o = (CacheObj) mMemoryCache.get(key);
            if (o != null) {
                return o.cacheStamp > time;
            }
            if (mDiskCache == null) {
                return true;
            }
            DiskLruCache.Value value = mDiskCache.get(key);
            return value.getFile(0).lastModified() > time;
        } catch (Throwable e) {
            return true;
        }
    }

}
