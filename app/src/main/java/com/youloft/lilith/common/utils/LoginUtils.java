package com.youloft.lilith.common.utils;

/**
 * Created by gyh on 2017/7/12.
 */

public class LoginUtils {

    private static final String PHONE_REGEX = "^1[3|4|5|7|8][0-9]\\d{8}$";

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
}
