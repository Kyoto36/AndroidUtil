package com.ls.glide_library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ls.comm_util_library.ObjectCheck;
import com.ls.glide_library.strategy.ILoadStrategy;

public class GlideLoader {

    private Context mContext;
    private ILoadStrategy mStrategy;
    private RequestBuilder<Drawable> mRequestBuilder;

    public GlideLoader(Context context,ILoadStrategy strategy){
        mContext = context;
        mStrategy = strategy;
    }

    public GlideLoader from(String url){
        mRequestBuilder = from(Glide.with(mContext).load(url));
        return this;
    }

    public GlideLoader from(@RawRes @DrawableRes int id){
        mRequestBuilder = from(Glide.with(mContext).load(id));
        return this;
    }

    public GlideLoader from(Uri uri){
        mRequestBuilder = from(Glide.with(mContext).load(uri));
        return this;
    }

    private RequestBuilder<Drawable> from(RequestBuilder<Drawable> requestBuilder){
        if(mStrategy == null){
            return requestBuilder;
        }
        return mStrategy.process(requestBuilder);
    }

    public void to(ImageView view){
        ObjectCheck.requireNonNull(mRequestBuilder,"call after from()");
        mRequestBuilder.into(view);
    }
}
