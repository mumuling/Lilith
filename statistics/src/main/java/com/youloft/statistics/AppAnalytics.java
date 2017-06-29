package com.youloft.statistics;

import android.app.Application;
import android.content.Context;

import com.tendcloud.tenddata.TCAgent;

import java.util.Map;

/**
 * 应用内统计SDK
 * <p>
 * Created by coder on 2017/6/29.
 */
public class AppAnalytics {

    private static Context sCtx = null;

    /**
     * 初始化应用统计
     *
     * @param application
     * @param appId
     * @param channel
     */
    public static void init(Application application, String appId, String channel) {
        sCtx = application;
        TCAgent.init(application, appId, channel);
        TCAgent.setReportUncaughtExceptions(true);
    }

    /**
     * 设置为Debug模式
     */
    public static void setDebug() {
        TCAgent.LOG_ON = true;
    }

    /**
     * 设置开启反作弊功能
     *
     * @param enabled
     */
    public static void setAntiCheatingEnabled(boolean enabled) {
        TCAgent.setAntiCheatingEnabled(sCtx, enabled);
    }

    /**
     * 页面统计--进入
     *
     * @param pageName
     */
    public static void onPageStart(String pageName) {
        TCAgent.onPageStart(sCtx, pageName);
    }

    /**
     * 页面统计--离开
     *
     * @param pageName
     */
    public static void onPageEnd(String pageName) {
        TCAgent.onPageEnd(sCtx, pageName);
    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceId() {
        return TCAgent.getDeviceId(sCtx);
    }

    /**
     * 如果所有事件都需要传输相同的参数，可以设置全局的Key-Value，这些Key-Value会自动添加到所有自定义事件：
     *
     * @param key
     * @param value 支持字符串（String）和数字（Number）类型
     */
    public static void setGlobalKV(String key, Object value) {
        TCAgent.setGlobalKV(key, value);
    }

    /**
     * 移除全局参数
     *
     * @param key
     */
    public static void removeGlobalKV(String key) {
        TCAgent.removeGlobalKV(key);
    }

    /**
     * 上报自定义事件
     *
     * @param eventId EVENT_ID
     */
    public static void onEvent(String eventId) {
        TCAgent.onEvent(sCtx, eventId);
    }

    /**
     * 上报带有Label的自定义事件
     *
     * @param eventId
     * @param eventLabel
     */
    public static void onEvent(String eventId, String eventLabel) {
        TCAgent.onEvent(sCtx, eventId, eventLabel);
    }

    /**
     * 上报带有参数&&Label的自定义事件
     *
     * @param eventId
     * @param eventLabel
     * @param map
     */
    public static void onEvent(String eventId, String eventLabel, Map map) {
        TCAgent.onEvent(sCtx, eventId, eventLabel, map);
    }

    /**
     * 上报错误
     *
     * @param throwable
     */
    public static void onError(Throwable throwable) {
        TCAgent.onError(sCtx, throwable);
    }


}
