package com.ls.glide_library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ls.glide_library.strategy.ILoadStrategy;

public class GlideApp {

    /**
     * 清除磁盘缓存
     * @param context
     */
    public static void clearDiskCache(Context context){
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清除内存缓存
     * @param context
     */
    public static void clearMemoryCache(Context context){
        Glide.get(context).clearMemory();
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
