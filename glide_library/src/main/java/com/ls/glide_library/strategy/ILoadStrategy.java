package com.ls.glide_library.strategy;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.RequestBuilder;

public interface ILoadStrategy {
    RequestBuilder<Drawable> process(RequestBuilder<Drawable> requestBuilder);
}
