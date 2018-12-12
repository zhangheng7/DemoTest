package com.example.administrator.demotest.banner;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * @author zhangheng
 * @date 2018/11/8
 */

public class GlideCacheModule implements GlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义缓存目录 默认250m磁盘
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                GlideCatchConfig.GLIDE_CARCH_DIR,
                GlideCatchConfig.GLIDE_CATCH_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
