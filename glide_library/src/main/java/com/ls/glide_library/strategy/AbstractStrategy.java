package com.ls.glide_library.strategy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ls.comm_util_library.ISingleListener;

public abstract class AbstractStrategy {

    protected Context mContext;
    protected RequestBuilder<Drawable> mRequestBuilder;

    public AbstractStrategy(Context context){
        mContext = context;
    }

    public AbstractStrategy from(String url){
        mRequestBuilder = from(Glide.with(mContext).load(url));
        return this;
    }

    public AbstractStrategy from(@RawRes @DrawableRes int id){
        mRequestBuilder = from(Glide.with(mContext).load(id));
        return this;
    }

    public AbstractStrategy from(Uri uri){
        mRequestBuilder = from(Glide.with(mContext).load(uri));
        return this;
    }

    protected abstract RequestBuilder<Drawable> from(RequestBuilder<Drawable> requestBuilder);

    public void into(ImageView view){
        mRequestBuilder.into(view);
    }
}
