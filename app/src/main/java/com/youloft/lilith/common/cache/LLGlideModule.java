package com.youloft.lilith.common.cache;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by zchao on 2017/6/27.
 * desc:
 * version:
 */

public class LLGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, getDiskFileString(context,"glideCache"), Integer.MAX_VALUE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

    private String getDiskFileString(Context mContext,String str) {
        File dirFile=new File(mContext.getCacheDir().getAbsolutePath().toString()+str);
        File tempFile=new File(dirFile,"bitmaps");
        if (! tempFile.getParentFile().exists()){
            tempFile.getParentFile().mkdirs();
        }
        return tempFile.getAbsolutePath().toString();
    }

}
