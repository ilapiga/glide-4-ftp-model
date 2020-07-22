package com.pro100soft.glideftp;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.pro100soft.glideftp.GlideFTP.FTPModel;
import com.pro100soft.glideftp.GlideFTP.FTPModelLoaderFactory;

import java.io.InputStream;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        int diskCacheSizeBytes = 1024 * 1024 * 1024; // 1 GB
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, diskCacheSizeBytes));//(new ExternalCacheDiskCacheFactory(context));

//        builder.setDefaultRequestOptions(
//                new RequestOptions()
//                        .format(DecodeFormat.PREFER_RGB_565)
//                        .disallowHardwareConfig());
        //builder.setMemoryCache(null);
        //builder.setDefaultRequestOptions(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.prepend(FTPModel.class, InputStream.class,
                new FTPModelLoaderFactory(context));
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

