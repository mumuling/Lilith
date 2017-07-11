package com.youloft.lilith.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.consmanager.ConsManager;

import java.security.MessageDigest;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * Glide处理圆形图片
 * version
 *
 * @author slj
 * @time 2017/6/29 15:32
 * @class GlideCircleTransform
 */

public class GlideBlurTransform extends BitmapTransformation {
    private static final String ID = "com.youloft.glide.load.resource.bitmap.blur";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final Context mContext;
    private final RenderScript rs;

    public GlideBlurTransform(Context context) {
        super();
        mContext = context;
        rs = RenderScript.create(context);
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap toTransform, int outWidth, int outHeight) {
        BlurFactor bf = new BlurFactor();
        bf.width = outWidth;
        bf.height = outHeight;
        bf.sampling = 10;
        bf.radius = 10;
        return Blur.of(mContext, toTransform, bf);
    }

    /**
     * 加入磁盘缓存
     *
     * @param messageDigest
     */
    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
