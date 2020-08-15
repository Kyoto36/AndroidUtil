package com.ls.glide_library.strategy

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.ls.glide_library.strategy.AbstractStrategy

class NoStrategy(context: Context?) : AbstractStrategy(context) {
    override fun from(requestBuilder: RequestBuilder<Drawable>): RequestBuilder<Drawable> {
        return requestBuilder;
    }
}