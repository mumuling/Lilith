package com.youloft.lilith.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import jp.wasabeef.blurry.Blurry;

/**
 * 用于登录后头像显示
 * <p>
 * Created by coder on 2017/7/11.
 */
public class GlideBlurTwoViewTarget extends BitmapImageViewTarget {

    private ImageView mBlurView = null;

    public GlideBlurTwoViewTarget(ImageView view, ImageView blurView) {
        super(view);
        this.mBlurView = blurView;
    }

    private int radius = 10;

    /**
     * 模糊半径
     *
     * @param radius
     * @return
     */
    public GlideBlurTwoViewTarget setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    private int sampling = 10;

    /**
     * 取样系数
     *
     * @param smapling 1/sampling
     * @return
     */
    public GlideBlurTwoViewTarget setSampling(int smapling) {
        this.sampling = smapling;
        return this;
    }

    @Override
    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        super.onResourceReady(resource, transition);
        if (mBlurView != null) {
            Blurry.with(getView().getContext()).sampling(sampling).async().radius(radius).from(resource).into(mBlurView);
        }
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        super.onLoadFailed(errorDrawable);
        if (mBlurView != null) {
//            Blurry.with(getView().getContext()).sampling(sampling).radius(radius).capture(getView()).into(mBlurView);
            mBlurView.setImageBitmap(null);
        }

    }
}
