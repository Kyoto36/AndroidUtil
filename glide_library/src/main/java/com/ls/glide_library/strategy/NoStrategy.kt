package com.ls.glide_library.strategy

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder

class NoStrategy : ILoadStrategy {
    override fun process(requestBuilder: RequestBuilder<Drawable>): RequestBuilder<Drawable> {
        return requestBuilder
    }

}