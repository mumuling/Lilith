package com.youloft.lilith.ui;

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

import java.security.MessageDigest;

/**     Glide处理圆形图片
 *version
 *@author  slj
 *@time    2017/6/29 15:32
 *@class   GlideCircleTransform
 */

public class GlideBlurTransform extends BitmapTransformation {

    private final Context mContext;
    private final RenderScript rs;

    public GlideBlurTransform(Context context) {
        super(context);
        mContext = context;

        rs = RenderScript.create( context );
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//        return ViewUtil.blurBitmap(toTransform, mContext);
        Bitmap blurredBitmap = toTransform.copy( Bitmap.Config.ARGB_8888, true );

        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(
                rs,
                blurredBitmap,
                Allocation.MipmapControl.MIPMAP_FULL,
                Allocation.USAGE_SHARED
        );
        Allocation output = Allocation.createTyped(rs, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        // Set the blur radius
        script.setRadius(10);

        // Start the ScriptIntrinisicBlur
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);

        toTransform.recycle();

        return blurredBitmap;
    }


    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
