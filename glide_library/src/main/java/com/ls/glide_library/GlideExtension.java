package com.ls.glide_library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ls.comm_util_library.NumberUtils;
import com.ls.comm_util_library.ObjectUtil;

public class GlideExtension {
    private Context mContext;
    protected RequestBuilder<Drawable> mRequestBuilder;

    protected GlideExtension(Context context){
        mContext = context;
    }

    protected GlideExtension(RequestBuilder<Drawable> requestBuilder){
        mRequestBuilder = requestBuilder;
    }

    public GlideExtension load(String str){
        if(NumberUtils.isInteger(str)){
            load(Integer.parseInt(str));
        }
        else {
            mRequestBuilder = Glide.with(mContext).load(str);
        }
        return this;
    }

    public GlideExtension load(@DrawableRes int id){
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
        RequestOptions requestOptions = RequestOptions.noTransformation()
                .transform(new CenterCrop(),new RoundedCorners(roundingRadius));
        mRequestBuilder = mRequestBuilder.apply(requestOptions);
        return this;
    }

    public GlideExtension topCropRoundCorners(int roundingRadius){
        RequestOptions requestOptions = RequestOptions.noTransformation()
                .transform(new TopCrop());
        mRequestBuilder = mRequestBuilder.apply(requestOptions);
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
        ObjectUtil.requireNonNull(mRequestBuilder,"call after load()");
        mRequestBuilder.into(view);
    }

    public <Y extends Target<Drawable>> void to(Y target){
        ObjectUtil.requireNonNull(mRequestBuilder,"call after load()");
        mRequestBuilder.into(target);
    }
}
