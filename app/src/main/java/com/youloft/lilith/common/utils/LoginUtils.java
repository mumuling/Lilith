package com.youloft.lilith.common.utils;

/**
 * Created by gyh on 2017/7/12.
 */

public class LoginUtils {
    /**
     * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700
     **/
    private static final String CHINA_TELECOM_PATTERN = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";

    /**
     * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1709
     **/
    private static final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";

    /**
     * 中国移动号码格式验证
     * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184
     * ,187,188,147,178,1705
     **/
    private static final String CHINA_MOBILE_PATTERN = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";

    public static boolean isPhoneNumber(String phone){
        if(phone.matches(CHINA_TELECOM_PATTERN) || phone.matches(CHINA_UNICOM_PATTERN) || phone.matches(CHINA_MOBILE_PATTERN)){
            return true;
        }else {
            return false;
        }
    }
}
