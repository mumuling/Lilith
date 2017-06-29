package com.youloft.lilith.common.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设备相关的方法
 * <p>
 * Created by coder on 2017/6/29.
 */

public class DeviceUtil {
    private static final String TAG = DeviceUtil.class.getSimpleName();


    /**
     * 获取 Wifi MAC 地址
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getWifiMacAddress() {
        //wifi mac地址
        String mac = "";
        try {
            WifiManager wifi = (WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            mac = info.getMacAddress();
        } catch (Exception e) {
        }
        Log.i(TAG, "WIFI MAC：" + mac);
        return mac;
    }

    /**
     * IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
     * IMSI共有15位，其结构如下：
     * MCC+MNC+MIN
     * MCC：Mobile Country Code，移动国家码，共3位，中国为460;
     * MNC:Mobile NetworkCode，移动网络码，共2位
     * 在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
     * 合起来就是（也是Android手机中APN配置文件中的代码）：
     * 中国移动：46000 46002
     * 中国联通：46001
     * 中国电信：46003
     * 举例，一个典型的IMSI号码为460030912121001
     */
    public static String getIMSI() {
        String IMSI = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            IMSI = telephonyManager.getSubscriberId();
            Log.i(TAG, " IMSI：" + IMSI);
        } catch (Exception e) {
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
     * IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称
     * IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的
     * 其组成为：
     * 1. 前6位数(TAC)是”型号核准号码”，一般代表机型
     * 2. 接着的2位数(FAC)是”最后装配号”，一般代表产地
     * 3. 之后的6位数(SNR)是”串号”，一般代表生产顺序号
     * 4. 最后1位数(SP)通常是”0″，为检验码，目前暂备用
     */
    public static String getIMEI() {
        String IMEI = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = telephonyManager.getDeviceId();
        } catch (Exception e) {
        }
        Log.i(TAG, " IMEI：" + IMEI);
        return IMEI;
    }

    /**
     * ICCID
     * <p>
     * SIM卡号
     *
     * @return
     */
    public static String getICCID() {
        try {
            return ((TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
        } catch (Throwable e) {
        }
        return "";
    }

    /**
     * 获取 ANDROID_ID
     */
    public static String getAndroidId() {
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(Utils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.i(TAG, "ANDROID_ID ：" + androidId);
        } catch (Exception e) {
        }
        return androidId;
    }


    /**
     * 获取 开机时间
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        Log.i(TAG, h + ":" + m);
        return h + ":" + m;
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

    /**
     * 是否为Miui
     *
     * @return
     */
    public static boolean isMIUI() {
        return a() || b() || c() || d();
    }


    /**
     * 打印系统信息
     *
     * @return
     */
    public static String printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  系统信息  ").append(time).append(" ______________");
        sb.append("\nID                 :").append(Build.ID);
        sb.append("\nBRAND              :").append(Build.BRAND);
        sb.append("\nMODEL              :").append(Build.MODEL);
        sb.append("\nRELEASE            :").append(Build.VERSION.RELEASE);
        sb.append("\nSDK                :").append(Build.VERSION.SDK);

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              :").append(Build.BOARD);
        sb.append("\nPRODUCT            :").append(Build.PRODUCT);
        sb.append("\nDEVICE             :").append(Build.DEVICE);
        sb.append("\nFINGERPRINT        :").append(Build.FINGERPRINT);
        sb.append("\nHOST               :").append(Build.HOST);
        sb.append("\nTAGS               :").append(Build.TAGS);
        sb.append("\nTYPE               :").append(Build.TYPE);
        sb.append("\nTIME               :").append(Build.TIME);
        sb.append("\nINCREMENTAL        :").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            :").append(Build.DISPLAY);
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            :").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       :").append(Build.MANUFACTURER);
            sb.append("\nBOOTLOADER         :").append(Build.BOOTLOADER);
            sb.append("\nCPU_ABI            :").append(Build.CPU_ABI);
            sb.append("\nCPU_ABI2           :").append(Build.CPU_ABI2);
            sb.append("\nHARDWARE           :").append(Build.HARDWARE);
            sb.append("\nUNKNOWN            :").append(Build.UNKNOWN);
            sb.append("\nCODENAME           :").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ GINGERBREAD-9 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sb.append("\nSERIAL             :").append(Build.SERIAL);
        }
        Log.i(TAG, sb.toString());
        return sb.toString();
    }
}
