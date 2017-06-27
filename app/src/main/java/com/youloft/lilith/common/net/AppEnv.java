package com.youloft.lilith.common.net;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.meituan.android.walle.WalleChannelReader;
import com.youloft.lilith.BuildConfig;
import com.youloft.lilith.common.utils.MD5;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * 環境變量
 * <p/>
 * Created by coder on 16-5-7.
 */
public class AppEnv {
    public static float DENSITY = 1;
    //語言
    public static final String LANG = Locale.getDefault().getLanguage();
    //国家
    public static final String CC = Locale.getDefault().getCountry();
    //CID用於接口區分哪個版本請求
    public static final String CLIENT_ID = "Youloft_AndroidLite";
    //卡片接口版本号
    public static final String CARD_VERSION = "6.0";
    //预置数据库版本
    public static final String DB_VER = "10";
    //当前广告版本
    public static final String AD_VER = "1";

    //包名
    public static String BUNDLE = BuildConfig.APPLICATION_ID;
    //版本名称
    public static String VERSION_NAME = BuildConfig.VERSION_NAME;
    //版本號
    public static int VERSION_CODE = BuildConfig.VERSION_CODE;
    //屏幕大小
    public static Point SCREEN_SIZE = new Point();
    //全局的AppContext
    private static Context mAppContext = null;
    /**
     * 以下都爲緩存
     **/
    private static String IMEI = null;
    private static String IMSI = null;
    private static String ICCID = null;

    private static String ANDROID_ID = null;
    private static String MAC = null;
    private static String DEVICE_MAC = null;
    private static String CHANNEL = null;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context, String AppId, String versionName, int versionCode) {
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        mAppContext = context;
        BUNDLE = AppId;
        VERSION_NAME = versionName;
        VERSION_CODE = versionCode;
        //獲取屏幕大小
        WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(SCREEN_SIZE);
        } else {
            SCREEN_SIZE.set(
                    wm.getDefaultDisplay().getWidth(),
                    wm.getDefaultDisplay().getHeight());
        }
        DENSITY = mAppContext.getResources().getDisplayMetrics().density;
    }

    public static void setAppContext(Context context) {
        mAppContext = context;
    }

    public static String getDeviceId() {
        if (TextUtils.isEmpty(DEVICE_MAC)) {
            DEVICE_MAC = obtainDeviceId();
        }
        return DEVICE_MAC;
    }


    /**
     * 获取渠道号
     * <p>
     * 渠道相关的操作见美团Walle签名方案
     *
     * @return
     * @see {https://github.com/Meituan-Dianping/walle}
     */
    public static String getChannel(Context appContext) {
        if (CHANNEL == null) {
            try {
                CHANNEL = WalleChannelReader.getChannel(appContext.getApplicationContext());
            } catch (NullPointerException e) {
                return "unknow";
            } catch (Exception e) {
                CHANNEL = "unknow";
            }
        }
        return CHANNEL;
    }
    /**
     * 获取新的TOken
     *
     * @return
     */
    private static String obtainDeviceId() {
        return MD5.encode(
                String.valueOf(Build.DEVICE
                        + Build.VERSION.RELEASE
                        + getAndroidId()
                        + getIMEI()
                        + getMacAddress()
                        + System.currentTimeMillis()));
    }

    /**
     * 获取Mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        if (null != MAC)
            return MAC;
        WifiInfo info = ((WifiManager) mAppContext.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        MAC = info.getMacAddress();
        if (info == null || TextUtils.isEmpty(MAC) || "::::".equals(MAC))
            MAC = "00000000";
        return MAC;
    }

    /**
     * 获取IMEI
     *
     * @return
     */
    public static String getIMEI() {
        if (null != IMEI)
            return IMEI;
        try {
            IMEI = ((TelephonyManager) AppEnv.mAppContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (TextUtils.isEmpty(IMEI)) {
                IMEI = "unknow";
            }
        } catch (Throwable e) {
            IMEI = "unknow";
        }
        return IMEI;
    }

    /**
     * 获取AndroidID
     * 类似于IOS中在IDFA
     *
     * @return
     */
    public static String getAndroidId() {
        if (null != ANDROID_ID)
            return ANDROID_ID;
        try {
            ContentResolver cr = AppEnv.mAppContext.getContentResolver();
            ANDROID_ID = Settings.System.getString(cr, Settings.System.ANDROID_ID);
            if (TextUtils.isEmpty(ANDROID_ID)) {
                ANDROID_ID = "";
            }
        } catch (Exception e) {
            ANDROID_ID = "";
        }
        return ANDROID_ID;
    }


    /**
     * 获取缓存文件路径
     *
     * @param key
     * @return
     */
    public static File getDataFile(String type, String key) {
        if (type == null) {
            type = "";
        }
        File externalCacheDir = mAppContext.getExternalCacheDir();
        if (externalCacheDir == null) {
            externalCacheDir = mAppContext.getDir(type, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        } else {
            externalCacheDir = new File(externalCacheDir, type);
        }

        File baseDir = externalCacheDir;
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            baseDir.mkdirs();
        }

        if (TextUtils.isEmpty(key)) {
            return baseDir;
        }
        return new File(baseDir, key);
    }

    /**
     * 获取缓存目录
     *
     * @param type
     * @return
     */
    public static File getCacheDir(String type) {
        return getDataFile(type, null);
    }

    /**
     * 提供一個方法
     *
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }


    /**
     * IMSI
     *
     * @return
     */
    public static String getIMSI() {
        if (null != IMSI)
            return IMSI;
        try {
            IMSI = ((TelephonyManager) AppEnv.mAppContext.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
            if (TextUtils.isEmpty(IMSI)) {
                IMSI = "unknow";
            }
        } catch (Throwable e) {
            IMSI = "unknow";
        }
        return IMSI;
    }

    /**
     * 获取MCC
     *
     * @return
     */
    public static String getMCC() {
        String imsi = getIMSI();
        if (TextUtils.isEmpty(imsi) || "unknow".equalsIgnoreCase(imsi))
            return "";
        return imsi.substring(0, 3);
    }

    /**
     * 获取MNC
     *
     * @return
     */
    public static String getMNC() {
        String imsi = getIMSI();
        if (TextUtils.isEmpty(imsi) || "unknow".equalsIgnoreCase(imsi))
            return "";
        return imsi.substring(3, 5);
    }


    /**
     * ICCID
     *
     * @return
     */
    public static String getICCID() {
        if (null != ICCID)
            return ICCID;
        try {
            ICCID = ((TelephonyManager) AppEnv.mAppContext.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
            if (TextUtils.isEmpty(IMSI)) {
                ICCID = "unknow";
            }
        } catch (Throwable e) {
            ICCID = "unknow";
        }
        return ICCID;
    }

    private static PackageInfo sPackageInfo;

    public static PackageInfo getPackageInfo() {
        if (sPackageInfo == null) {
            try {
                sPackageInfo = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sPackageInfo;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName() {
        if (!TextUtils.isEmpty(VERSION_NAME)) {
            return VERSION_NAME;
        }
        if (mAppContext == null) {
            return "";
        }
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return "";
        }
        VERSION_NAME = info.versionName;
        return VERSION_NAME;
    }

    /**
     * 获取版本versioncode
     *
     * @return
     */
    public static int getVersionCode() {
        if (mAppContext == null) {
            return -1;
        }
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return -1;
        }
        return info.versionCode;
    }

    public static <T> T getSystemService(String locationService) {
        return (T) mAppContext.getSystemService(locationService);
    }


    public static boolean a() {
        return a("ro.miui.ui.version.name", "UNKNOWN").equalsIgnoreCase("V5");
    }

    public static boolean b() {
        return a("ro.miui.ui.version.name", "UNKNOWN").equalsIgnoreCase("V6");
    }

    public static boolean c() {
        return a("ro.miui.ui.version.name", "UNKNOWN").equalsIgnoreCase("V7");
    }

    public static boolean d() {
        return a("ro.miui.ui.version.name", "UNKNOWN").equalsIgnoreCase("V8");
    }

    private static String a(String var0, String var1) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, var0, var1);
        } catch (Exception var3) {
            return var1;
        }
    }

    public static boolean isMIUI() {
        return a() || b() || c() || d();
    }

    public static int getMsgVersion() {
        return 800 + VERSION_CODE;
    }

    private static HashMap<String, Typeface> sFontMap = new HashMap<>();


    /**
     * 清除字体缓存
     */
    public static void cleanFontCache() {
        sFontMap.clear();
    }

    public static Typeface getFont(Context context, String font_lunar) {
        if ("font_lunar".equalsIgnoreCase(font_lunar)) {
            return null;
        }
        Typeface mFontResult = null;
        try {
            if (!sFontMap.containsKey(font_lunar)) {
                sFontMap.put(font_lunar, Typeface.createFromAsset(context.getAssets(), "fonts/" + font_lunar));
            }
            mFontResult = sFontMap.get(font_lunar);
        } catch (Exception e) {
        }
        if (mFontResult == null) {
            mFontResult = Typeface.DEFAULT;
        }
        return mFontResult;
    }

    public static String getDefaultShareTitle() {
        return "万年历分享";
    }

}
