package com.youloft.lilith.common.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;

/**
 * 加密工具类
 * Created by coder on 2017/6/29.
 */

public class Encryption {
    private Encryption() {
    }

    @SuppressWarnings("unused")
    private static char sHexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * MD5编码
     *
     * @param source
     * @return
     */
    final public static String encodeMD5(String source) {
        if (TextUtils.isEmpty(source))
            return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(source.getBytes("utf-8"));
            return byte2hex(digest.digest());
        } catch (Throwable e) {
        }
        return "";
    }

    /**
     * 反编码Base64到明文
     *
     * @return
     */
    public static String decodeBase64(String decodeStr) {
        if (TextUtils.isEmpty(decodeStr)) {
            return decodeStr;
        }
        return new String(Base64.decode(decodeStr, Base64.DEFAULT));
    }

    /**
     * 编码Base64
     *
     * @param encodeStr
     * @return
     */
    public static String encodeBase64(String encodeStr) {
        if (TextUtils.isEmpty(encodeStr)) {
            return encodeStr;
        }
        return Base64.encodeToString(encodeStr.getBytes(), Base64.DEFAULT);
    }

    /**
     * byte2hex的转换
     *
     * @param source
     * @return
     */
    private static String byte2hex(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }

        final int size = source.length;
        final char str[] = new char[size * 2];
        int index = 0;
        byte b;
        for (int i = 0; i < size; i++) {
            b = source[i];
            str[index++] = sHexDigits[b >>> 4 & 0xf];
            str[index++] = sHexDigits[b & 0xf];
        }
        return new String(str);
    }

}
