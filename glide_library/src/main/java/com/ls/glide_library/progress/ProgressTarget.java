package com.ls.glide_library.progress;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ls.comm_util_library.ThreadUtils;

public abstract class ProgressTarget<Z> extends WrappingTarget<Z> implements OnProgressListener {

    private String mUrl;

    public ProgressTarget(String url, Target<Z> target) {
        super(target);
        mUrl = url;
    }

    private void start() {
        ProgressManager.get().registerListener(mUrl,this);
        onStart();
    }

    private void cleanup() {
        ProgressManager.get().unRegisterListener(mUrl,this);
        onEnd();
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        super.onLoadStarted(placeholder);
        start();
    }

    @Override
    public void onResourceReady(@NonNull Z resource, @Nullable Transition<? super Z> transition) {
        cleanup();
        super.onResourceReady(resource, transition);
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        cleanup();
        super.onLoadFailed(errorDrawable);
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        cleanup();
        super.onLoadCleared(placeholder);
    }

    @Override
    public void onProgress(String url, long bytesRead, long totalBytes, boolean isDone, GlideException exception) {
        if(TextUtils.equals(mUrl,url)){
            final float ppm = (float) (bytesRead / (totalBytes / 10000.0));
            ThreadUtils.Companion.execMain(new Runnable() {
                @Override
                public void run() {
                    onProgress(ppm);
                }
            });
        }
    }

    public abstract void onProgress(float ppm);
    public abstract void onEnd();
}