package com.youloft.socialize.media;

import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.media.UMImage;

import java.io.File;

/**
 * 分享图片Hook
 * <p>
 * Created by coder on 2017/6/28.
 */

public class ShareImage extends UMImage {
    public ShareImage(Context context, File file) {
        super(context, file);
    }

    public ShareImage(Context context, String s) {
        super(context, s);
    }

    public ShareImage(Context context, int i) {
        super(context, i);
    }

    public ShareImage(Context context, byte[] bytes) {
        super(context, bytes);
    }

    public ShareImage(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }
}
