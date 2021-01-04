package com.ls.glide_library.strategy

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.bumptech.glide.RequestBuilder
import com.ls.glide_library.GlideApp

open class ImageSelectorStrategy() : ILoadStrategy {

    private var mPlaceholder: Int = -1
    private var mError: Int = -1
    private var mRoundingRadius = -1
    private var mCenterCrop = false

    constructor(@DrawableRes placeholder: Int, @DrawableRes error: Int) : this() {
        mPlaceholder = placeholder
        mError = error
    }

    constructor(@DrawableRes placeholder: Int, @DrawableRes error: Int, roundingRadius: Int) : this(
        placeholder,
        error
    ) {
        mRoundingRadius = roundingRadius
    }

    constructor(
        @DrawableRes placeholder: Int,
        @DrawableRes error: Int,
        roundingRadius: Int,
        centerCrop: Boolean
    ) : this(placeholder, error, roundingRadius) {
        mCenterCrop = centerCrop
    }

    override fun process(requestBuilder: RequestBuilder<Drawable>?): RequestBuilder<Drawable> {
        var extension = GlideApp.with(requestBuilder).noDisk()
        if (mCenterCrop && mRoundingRadius != -1) {
            extension = extension.centerCropRoundCorners(mRoundingRadius)
        } else if (mCenterCrop && mRoundingRadius == -1) {
            extension = extension.centerCrop()
        } else if (!mCenterCrop && mRoundingRadius != -1) {
            extension = extension.roundCorners(mRoundingRadius)
        }
        var origin = extension.toGlide()
        if (mPlaceholder != -1) {
            origin = origin.placeholder(mPlaceholder)
        }
        if (mError != -1) {
            origin = origin.error(mError)
        }
        return origin.thumbnail(0.5F)
    }
}