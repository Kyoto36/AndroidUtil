package com.ls.glide_library.progress;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * @ClassName: ProgressAppGlideModule
 * @Description: 如果要使用进度加载需要声明一个类继承这个类，并加上注解@GlideModule
 * @Author: ls
 * @Date: 2020/12/29 19:17
 */
public abstract class ProgressAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class,new OkHttpUrlLoader.Factory(ProgressManager.get().getOkHttp()));
    }
}
