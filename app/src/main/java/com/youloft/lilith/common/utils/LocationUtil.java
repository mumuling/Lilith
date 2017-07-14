package com.youloft.lilith.common.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.youloft.lilith.LLApplication;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zchao on 2017/7/4.
 * desc: 定位
 * version:
 */

public class LocationUtil {
    private static final long REFRESH_TIME = 5000L;
    private static final float METER_POSITION = 0.0f;
    private static ILocationListener mLocationListener;
    private static LocationListener listener = new MyLocationListener();
    private static final String location_cache_key = "location_cache";

    private static class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (mLocationListener != null) {
                mLocationListener.onSuccessLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * 更新定位缓存
     */
    public static void updateLocation() {
        if (LLApplication.getApiCache().isExpired(location_cache_key, 24 * 60 * 60 * 1000)) {
            Observable.create(new ObservableOnSubscribe<String[]>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String[]> e) throws Exception {
                    Location bestLocation = getBestLocation(Utils.getContext());
                    String[] la = new String[2];
                    if (bestLocation != null) {
                        la[0] = String.valueOf(bestLocation.getLongitude());
                        la[1] = String.valueOf(bestLocation.getLatitude());
                        e.onNext(la);
                    } else {
                        e.onError(new Throwable("没有定位"));
                    }
                }
            }).subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io()).subscribe(new Observer<String[]>() {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                }

                @Override
                public void onNext(@io.reactivex.annotations.NonNull String[] strings) {
                    LLApplication.getApiCache().putCacheObj(location_cache_key, strings);
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
        }

    }

    public static String[] getLocation(){
        try {
            return LLApplication.getApiCache().getCacheObjSync(location_cache_key, String[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GPS获取定位方式
     */
    public static Location getGPSLocation(@NonNull Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);
        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//是否支持GPS定位
            //获取最后的GPS定位信息，如果是第一次打开，一般会拿不到定位信息，一般可以请求监听，在有效的时间范围可以获取定位信息
            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }

    /**
     * network获取定位方式
     */
    public static Location getNetWorkLocation(Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);
        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//是否支持Network定位
            //获取最后的network定位信息
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    /**
     * 获取最好的定位方式
     */
    public static Location getBestLocation(Context context) {
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);               //设置低耗电
        criteria.setAltitudeRequired(true);                             //设置需要海拔
        criteria.setBearingAccuracy(Criteria.ACCURACY_COARSE);          //设置COARSE精度标准
        criteria.setAccuracy(Criteria.ACCURACY_LOW);                    //设置低精度
        Location location;
        LocationManager manager = getLocationManager(context);
        if (criteria == null) {
            criteria = new Criteria();
        }
        String provider = manager.getBestProvider(criteria, true);
        if (TextUtils.isEmpty(provider)) {
            location = getNetWorkLocation(context);
        } else {
            //高版本的权限检查
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            //获取最适合的定位方式的最后的定位权限
            location = manager.getLastKnownLocation(provider);
        }
        return location;
    }

    /**
     * 定位监听
     */
    public static void addLocationListener(Context context, String provider, ILocationListener locationListener) {

        addLocationListener(context, provider, REFRESH_TIME, METER_POSITION, locationListener);
    }

    /**
     * 定位监听
     */
    public static void addLocationListener(Context context, String provider, long time, float meter, ILocationListener locationListener) {
        if (locationListener != null) {
            mLocationListener = locationListener;
        }
        if (listener == null) {
            listener = new MyLocationListener();
        }
        LocationManager manager = getLocationManager(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(provider, time, meter, listener);
    }

    /**
     * 取消定位监听
     */
    public static void unRegisterListener(Context context) {
        if (listener != null) {
            LocationManager manager = getLocationManager(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //移除定位监听
            manager.removeUpdates(listener);
        }
    }

    private static LocationManager getLocationManager(@NonNull Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 自定义接口
     */
    public interface ILocationListener {
        void onSuccessLocation(Location location);
    }
}
