package com.youloft.lilith.glide;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

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

public class GlideBlurTransform implements Transformation<Bitmap> {
    private static String ID = "com.youloft.glide.load.resource.bitmap.blur";
    private static byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final Context mContext;

    private static GlideBlurTransform sInstance = null;
    public static Transformation<Bitmap> getInstance(Context context){
        return new GlideBlurTransform(context.getApplicationContext());
//
//       if(sInstance==null){
//          sInstance =   new GlideBlurTransform(context.getApplicationContext());
//
//        }
//        return sInstance;


    }

    public GlideBlurTransform(Context context) {
        super();
        mContext = context;
    }

    private int sampling = 1;
    private int radius = 5;



    /**
     * 加入磁盘缓存
     *
     * @param messageDigest
     */
    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
       Bitmap toTransform =TransformationUtils.centerCrop(Glide.get(mContext).getBitmapPool(),resource.get(),outWidth,outHeight);
        BlurFactor bf = new BlurFactor();
        bf.width = toTransform.getWidth();
        bf.height = toTransform.getHeight();

        float xScale = (float)toTransform.getWidth()/outWidth;
        float yScale = (float)toTransform.getHeight()/outHeight;
        float scale = Math.max(xScale,yScale);
        if(scale>sampling){
            sampling = (int) scale;
            radius = (radius/scale) < 1 ? 1: (int) (radius / scale);
        }
        bf.sampling = sampling;
        bf.radius = radius ;

        Bitmap ret =  Blur.of(mContext, toTransform, bf);
        return BitmapResource.obtain(ret, Glide.get(mContext).getBitmapPool());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlideBlurTransform that = (GlideBlurTransform) o;

        return radius == that.radius;

    }

    @Override
    public int hashCode() {
        return radius;
    }
}
