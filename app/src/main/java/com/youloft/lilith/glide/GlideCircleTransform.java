package com.youloft.lilith.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * Glide处理圆形图片
 * version
 *
 * @author slj
 * @time 2017/6/29 15:32
 * @class GlideCircleTransform
 */

public class GlideCircleTransform implements Transformation<Bitmap> {
    private static final String ID = "com.youloft.glide.load.resource.bitmap.circle";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private float radius;
    private Context mContext;
    private static GlideCircleTransform sInstance = null;
    public GlideCircleTransform() {

    }
    public GlideCircleTransform(Context context) {
        super();
        mContext = context;
    }
    public static Transformation<Bitmap> getInstance(Context context){
            if(sInstance == null){
                sInstance =  new GlideCircleTransform(context.getApplicationContext());
            }
            return sInstance;
        }






    public static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap toTransform = TransformationUtils.centerCrop(Glide.get(mContext).getBitmapPool(),resource.get(),outWidth,outHeight);
        Bitmap ret = circleCrop(Glide.get(mContext).getBitmapPool(), toTransform);
        float r1 = Math.min(outWidth, outHeight);
        radius = r1/2f;
        return BitmapResource.obtain(ret, Glide.get(mContext).getBitmapPool());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlideCircleTransform that = (GlideCircleTransform) o;

        return radius == that.radius;

    }

    @Override
    public int hashCode() {
        return (int) radius;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
