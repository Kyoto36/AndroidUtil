package com.ls.glide_library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ls.comm_util_library.ObjectCheck;

public class GlideExtension {
    private Context mContext;
    protected RequestBuilder<Drawable> mRequestBuilder;

    protected GlideExtension(Context context){
        mContext = context;
    }

    protected GlideExtension(RequestBuilder<Drawable> requestBuilder){
        mRequestBuilder = requestBuilder;
    }

    public GlideExtension load(String url){
        mRequestBuilder = Glide.with(mContext).load(url);
        return this;
    }

    public GlideExtension load(@RawRes @DrawableRes int id){
        mRequestBuilder = Glide.with(mContext).load(id);
        return this;
    }

    public GlideExtension load(Uri uri){
        mRequestBuilder = Glide.with(mContext).load(uri);
        return this;
    }

    public GlideExtension circle(int borderWidth,@ColorInt int borderColor){
        mRequestBuilder = mRequestBuilder.apply(RequestOptions.bitmapTransform(new GlideCircleCrop(borderWidth, borderColor)));
        return this;
    }

    public GlideExtension circle(){
        return circle(0,-1);
    }

    public GlideExtension noCache(){
        mRequestBuilder = mRequestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        return this;
    }

    public GlideExtension noDisk(){
        mRequestBuilder = mRequestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE);
        return this;
    }

    public GlideExtension roundCorners(int roundingRadius){
        mRequestBuilder = mRequestBuilder.apply(RequestOptions.bitmapTransform(new RoundedCorners(roundingRadius)));
        return this;
    }

    public GlideExtension centerCropRoundCorners(int roundingRadius){
        mRequestBuilder = mRequestBuilder.apply(RequestOptions.bitmapTransform(new CenterCropRoundedCorners(roundingRadius)));
        return this;
    }

    public GlideExtension centerCrop(){
        mRequestBuilder = mRequestBuilder.centerCrop();
        return this;
    }

    public GlideExtension anim(int viewAnimationId){
        mRequestBuilder = mRequestBuilder.transition(new GenericTransitionOptions<>().transition(viewAnimationId));
        return this;
    }

    public GlideExtension crossFade(){
        mRequestBuilder = mRequestBuilder.transition(new DrawableTransitionOptions().crossFade());
        return this;
    }

    public GlideExtension crossFade(int duration){
        mRequestBuilder = mRequestBuilder.transition(new DrawableTransitionOptions().crossFade(duration));
        return this;
    }

    public RequestBuilder<Drawable> toGlide(){
        return mRequestBuilder;
    }

    public void to(ImageView view){
        ObjectCheck.requireNonNull(mRequestBuilder,"call after load()");
        mRequestBuilder.into(view);
    }

    public <Y extends Target<Drawable>> void to(Y target){
        ObjectCheck.requireNonNull(mRequestBuilder,"call after load()");
        mRequestBuilder.into(target);
    }
}
