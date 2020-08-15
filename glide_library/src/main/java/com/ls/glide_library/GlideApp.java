package com.ls.glide_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.ls.glide_library.strategy.ILoadStrategy;

public class GlideApp {
    /**
     * 设置Glide的缓存大小与路径
     */
    @SuppressLint("VisibleForTests")
    public static void setDiskCachePath(Context context, String path) {
        GlideBuilder builder = new GlideBuilder();
        long bitmapPoolSizeBytes = 1024 * 1024 * 30L; // 30mb
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));
        long diskCacheSizeBytes = 1024 * 1024 * 10L; // 100mb
        builder.setDiskCache(new DiskLruCacheFactory(path, diskCacheSizeBytes));
        Glide.init(context, builder);
    }


    public static GlideLoader getLoader(Context context,ILoadStrategy strategy){
        return new GlideLoader(context, strategy);
    }

    private GlideApp(){}

    public static GlideExtension with(RequestBuilder<Drawable> requestBuilder){
        return new GlideExtension(requestBuilder);
    }

    public static GlideExtension with(Context context){
        return new GlideExtension(context);
    }

    public static GlideExtension with(View view){
        return new GlideExtension(view.getContext());
    }

    public static GlideExtension with(Fragment fragment){
        return new GlideExtension(fragment.getContext());
    }
}
