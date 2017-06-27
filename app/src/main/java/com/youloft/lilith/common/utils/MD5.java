/*******************************************************************************
 * Copyright (C) 2009-2010 eoeMobile.
 * All rights reserved.
 * http://www.eoeMobile.com/
 * <p>
 * CHANGE LOG:
 * DATE			AUTHOR			COMMENTS
 * =============================================================================
 * 2010MAY11		Waznheng Ma		Refine for Constructor and error handler.
 *******************************************************************************/

package com.youloft.lilith.common.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5
 * @Description:处理MD5加密
 * @author: Javen
 * @date: 2013-5-24 下午1:09:47
 */
public final class MD5 {
    private static final String ALGORITHM = "MD5";

    @SuppressWarnings("unused")
    private static char sHexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    private MD5() {
    }

    final public static String encode(String source) {
        if (TextUtils.isEmpty(source))
            return "";
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(source.getBytes("utf-8"));
            return StringUtils.hexString(digest.digest());
        } catch (Throwable e) {
        }
        return "";
    }

}
