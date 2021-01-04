package com.ls.glide_library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ls.comm_util_library.ObjectUtil;
import com.ls.glide_library.strategy.ILoadStrategy;

public class GlideLoader {

    private Context mContext;
    private ILoadStrategy mStrategy;
    private RequestBuilder<Drawable> mRequestBuilder;

    public GlideLoader(Context context,ILoadStrategy strategy){
        mContext = context;
        mStrategy = strategy;
    }

    public RequestBuilder<Drawable> toGlide(){
        return mRequestBuilder;
    }

    public GlideLoader from(String url){
        mRequestBuilder = from(GlideApp.with(mContext).load(url).mRequestBuilder);
        return this;
    }

    public GlideLoader fromBitmap(String url){
        mRequestBuilder = from(GlideApp.with(mContext).load(url).mRequestBuilder);
        return this;
    }

    public GlideLoader from(@DrawableRes int id){
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
        ObjectUtil.requireNonNull(mRequestBuilder,"call after from()");
        mRequestBuilder.into(view);
    }
}
