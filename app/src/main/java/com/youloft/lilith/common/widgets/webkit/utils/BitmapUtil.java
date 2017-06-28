package com.youloft.lilith.common.widgets.webkit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 用于WebKit中处理图片转Base64
 * Created by coder on 2017/6/28.
 */
public class BitmapUtil {
    public static Bitmap readZoomImage(String path, int maxSize) {
        if (maxSize > 1024) {
            maxSize = 1024;
        }
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;

        Bitmap localBitmap = BitmapFactory.decodeFile(path, localOptions);
        int i = localOptions.outHeight > localOptions.outWidth ? localOptions.outHeight : localOptions.outWidth;
        int j = Math.round(i / maxSize);
        if (j < 1) {
            j = 1;
        }
        localOptions.inSampleSize = j;

        localOptions.inJustDecodeBounds = false;
        if (localBitmap != null) {
            localBitmap.recycle();
        }
        return BitmapFactory.decodeFile(path, localOptions);
    }

    public static byte[] bitmapToBytes(Bitmap paramBitmap, Bitmap.CompressFormat paramCompressFormat) {
        if (paramBitmap != null) {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            paramBitmap.compress(paramCompressFormat, 70, localByteArrayOutputStream);
            return localByteArrayOutputStream.toByteArray();
        }
        return null;
    }

    public static String bitmapToString(String path, int maxSize) {
        Bitmap bitmap = readZoomImage(path, maxSize);
        byte[] arrayOfByte = bitmapToBytes(bitmap, Bitmap.CompressFormat.JPEG);
        return new String(Base64.encode(arrayOfByte, 0)).replaceAll("[\r|\n]", "");
    }
}
