package com.youloft.lilith.common.base;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Created by zchao on 2017/6/28.
 * desc: glide4.0全局配置
 * version:
 */
@GlideModule
public class LLAppGlideModule extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        /**
         * 处理目录
         */
        File externalCacheDir = context.getExternalCacheDir();
        try {
            File tempFile = File.createTempFile("p_", "_f", externalCacheDir);
            if (tempFile != null
                    && tempFile.exists()
                    && tempFile.canWrite()) {
                builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, Integer.MAX_VALUE));
            } else {
                builder.setDiskCache(new InternalCacheDiskCacheFactory(context, Integer.MAX_VALUE));
            }
        } catch (Throwable e) {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, Integer.MAX_VALUE));
        }

        //默认glide的配置，目前只写了默认缓存到disk
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
                ;
        builder.setDefaultRequestOptions(requestOptions);
    }

    @Override
    public void registerComponents(Context context, Registry registry) {
        super.registerComponents(context, registry);
    }
}
