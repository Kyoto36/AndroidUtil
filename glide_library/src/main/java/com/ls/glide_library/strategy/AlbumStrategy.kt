package com.ls.glide_library.strategy

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.bumptech.glide.RequestBuilder
import com.ls.comm_util_library.Util
import com.ls.glide_library.GlideApp

class AlbumStrategy(context: Context): AbstractStrategy(context) {

    private var mPlaceholder: Int = -1
    private var mError: Int = -1

    fun addPlaceholder(@DrawableRes resourceId: Int){
        mPlaceholder = resourceId
    }

    fun addError(@DrawableRes resourceId: Int){
        mError = resourceId
    }

    override fun from(requestBuilder: RequestBuilder<Drawable>): RequestBuilder<Drawable> {
        var temp = GlideApp.with(requestBuilder)
            .noDisk()
            .centerCropRoundCorners(Util.dp2px(8F).toInt())
            .toGlide()
            if(mPlaceholder != -1){
                temp = temp.placeholder(mPlaceholder)
            }
            if(mError != -1){
                temp = temp.error(mError)
            }
            return temp.thumbnail(0.5F)
    }
}