package com.youloft.lilith.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by gyh on 2017/7/12.
 */

public class LoginUtils {

    /**
     * 手机正则校验
     */
    private static final String PHONE_REGEX = "^1[3|4|5|7|8][0-9]\\d{8}$";

    //返回true 号码合法  返回false 不合法
    public static boolean isPhoneNumber(String phone) {
        phone = phone.trim();
        if (phone.length() != 11) {
            return false;
        }
        if (phone.matches(PHONE_REGEX)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 防止抖动点击
     */
    private static long clicktime = 0;
    public synchronized static boolean canClick() {
        if (Math.abs(clicktime - System.currentTimeMillis()) > 800) {
            clicktime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * 检测是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWxInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
}
