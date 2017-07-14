package com.youloft.lilith.glide;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

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
    private String ID = "com.youloft.glide.load.resource.bitmap.blur";
    private byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final Context mContext;

    public GlideBlurTransform(Context context) {
        super();
        mContext = context;
    }

    private int sampling = 1;
    private int radius = 10;

    public GlideBlurTransform(Context context, int sampling, int radius) {
        super();
        mContext = context;
        this.sampling = sampling;
        this.radius = radius;
        //更新
        ID += "_" + sampling + "_" + radius;
        ID_BYTES = ID.getBytes(CHARSET);
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap toTransform, int outWidth, int outHeight) {
        BlurFactor bf = new BlurFactor();
        bf.width = toTransform.getWidth();
        bf.height = toTransform.getHeight();

        float xScale = (float)toTransform.getWidth()/outWidth;
        float yScale = (float)toTransform.getHeight()/outHeight;
        float scale = Math.max(xScale,yScale);
        if(scale>sampling){
            sampling = (int) scale;
        }
        bf.sampling = sampling;
        bf.radius = (int) (radius/scale)/ 2;
        return Blur.of(mContext, toTransform, bf);
     //   return toTransform;
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
