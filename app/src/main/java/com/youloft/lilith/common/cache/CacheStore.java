package com.youloft.lilith.common.cache;

import android.os.Environment;
import android.text.TextUtils;
import android.util.LruCache;

import com.alibaba.fastjson.JSON;
import com.youloft.lilith.common.cache.disk.DiskLruCache;
import com.youloft.lilith.common.utils.Utils;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

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
            mDiskCache = DiskLruCache.open(cacheDir, 1, 1, Integer.MAX_VALUE);
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
     * 缓存Transform
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> FlowableTransformer<T, T> cacheTransform(final String key) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(@NonNull T t) throws Exception {
                        CacheObj<T> cacheObj = new CacheObj<T>(key, t);
                        mMemoryCache.put(key, cacheObj);
                        if (mDiskCache != null) {
                            //放入缓存
                            DiskLruCache.Editor editor = mDiskCache.edit(key);
                            File cacheFile = editor.getFile(0);
                            editor.set(0, JSON.toJSONString(t));
                            editor.commit();
                            cacheFile.setLastModified(cacheObj.cacheStamp);
                        }
                    }
                });
            }
        };
    }


    /**
     * 读取缓存
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> Flowable<T> readCache(final String key, final Class<T> type) {
        return Flowable.just(key).compose(new FlowableTransformer<String, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<String> upstream) {
                return upstream.filter(new Predicate<String>() {//过滤不存在的缓存
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        if (mMemoryCache.get(s) == null && mDiskCache.get(s) == null) {
                            return false;
                        }
                        return true;
                    }
                }).map(new Function<String, CacheObj<T>>() {
                    @Override
                    public CacheObj<T> apply(@NonNull String s) throws Exception {//加载缓存
                        CacheObj<T> retCache = (CacheObj<T>) mMemoryCache.get(key);
                        if (retCache != null) {
                            return retCache;
                        }
                        if (mDiskCache != null) {
                            String diskStr = mDiskCache.get(key).getString(0);
                            long cacheTime = mDiskCache.get(key).getFile(0).lastModified();
                            retCache = new CacheObj<T>(key, null);
                            retCache.data = JSON.parseObject(diskStr, type);
                            retCache.cacheStamp = cacheTime;
                            mMemoryCache.put(key, retCache);
                        }
                        return retCache;
                    }
                }).map(new Function<CacheObj<T>, T>() {
                    @Override
                    public T apply(@NonNull CacheObj<T> tCacheObj) throws Exception {
                        return tCacheObj.data;
                    }
                });
            }
        });
    }

    /**
     * 获取缓存对象
     *
     * @param <T>
     * @param key
     * @param type
     * @return
     */
    public <T> Observable<CacheObj<T>> getCacheObj(final String key, final Class<T> type) {
//        return Observable.just(key).map(new Function<String, CacheObj<T>>() {
//            @Override
//            public CacheObj<T> apply(@NonNull String key) throws Exception {
//                CacheObj<T> retCache = (CacheObj<T>) mMemoryCache.get(key);
//                if (retCache != null) {
//                    return retCache;
//                }
//                if (mDiskCache != null) {
//                    String diskStr = mDiskCache.get(key).getString(0);
//                    long cacheTime = mDiskCache.get(key).getFile(0).lastModified();
//                    if (TextUtils.isEmpty(diskStr)) {
//                        return null;
//                    }
//                    retCache.data = JSON.parseObject(diskStr, type);
//                    retCache.cacheStamp = cacheTime;
//                    mMemoryCache.put(key, retCache);
//                }
//                return retCache;
//            }
//        });

        return Observable.create(new ObservableOnSubscribe<CacheObj<T>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<CacheObj<T>> e) throws Exception {
                try {
                    CacheObj<T> retCache = (CacheObj<T>) mMemoryCache.get(key);
                    if (retCache != null) {
                        e.onNext(retCache);
                        e.onComplete();
                        return;
                    }
                    if (mDiskCache != null) {
                        String diskStr = mDiskCache.get(key).getString(0);
                        long cacheTime = mDiskCache.get(key).getFile(0).lastModified();
                        if (TextUtils.isEmpty(diskStr)) {
                            e.onNext(null);
                            e.onComplete();
                            return;
                        }
                        retCache = new CacheObj<T>(key, null);
                        retCache.data = JSON.parseObject(diskStr, type);
                        retCache.cacheStamp = cacheTime;
                        mMemoryCache.put(key, retCache);
                    }
                    e.onNext(retCache);
                    e.onComplete();
                } catch (Throwable e1) {
                    e.onError(e1);
                }
                return;
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

    /**
     * 判断是否过期
     *
     * @param duration
     * @return
     */
    public Predicate<String> isExpiredPredicate(final long duration) {
        return new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                if (isExpired(s, duration)) {
                    return true;
                }
                return false;
            }
        };
    }

    public <T> T getCacheObjSync(String key, Class<T> type) throws IOException {
        CacheObj<T> retCache = (CacheObj<T>) mMemoryCache.get(key);
        if (retCache != null) {
            return retCache.data;
        }
        if (mDiskCache != null && mDiskCache.get(key) != null) {
            String diskStr = mDiskCache.get(key).getString(0);
            long cacheTime = mDiskCache.get(key).getFile(0).lastModified();
            if (TextUtils.isEmpty(diskStr)) {
                return null;
            }
            retCache = new CacheObj<>(key, null);
            retCache.data = JSON.parseObject(diskStr, type);
            retCache.cacheStamp = cacheTime;
            mMemoryCache.put(key, retCache);
            return retCache.data;
        }
        return null;
    }
}
