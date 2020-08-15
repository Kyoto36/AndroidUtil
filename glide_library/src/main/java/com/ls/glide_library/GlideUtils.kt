package com.ls.glide_library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.ls.comm_util_library.ISingleListener
import com.ls.comm_util_library.IVoidListener
import java.security.MessageDigest
import com.bumptech.glide.request.RequestListener as RequestListener

object GlideUtils {

    /**
     * 设置Glide的缓存大小与路径
     */
    @SuppressLint("VisibleForTests")
    fun setDiskCachePath(context: Context, path: String) {
        val builder = GlideBuilder()
        val bitmapPoolSizeBytes = 1024 * 1024 * 30L // 30mb
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSizeBytes))
        val diskCacheSizeBytes = 1024 * 1024 * 10L // 100mb
        builder.setDiskCache(DiskLruCacheFactory(path, diskCacheSizeBytes))
        Glide.init(context, builder)
    }


    private fun <T> loadObject(obj: Any, requestBuilder: RequestBuilder<T>): RequestBuilder<T> {
        return when (obj) {
            is Int -> requestBuilder.load(obj)
            is String -> try {
                val resId = Integer.parseInt(obj.toString())
                requestBuilder.load(resId)
            } catch (e: java.lang.Exception) {
                requestBuilder.load(obj.toString())
            }
            is Bitmap -> requestBuilder.load(obj)
            else -> requestBuilder.load(obj)
        }
    }

    private fun addPlaceholder(requestBuilder: RequestBuilder<Drawable>,placeholder: Int, error: Int): RequestBuilder<Drawable>{
        return if (placeholder == -1 && error == -1) {
            requestBuilder
        } else if (placeholder == -1 && error != -1) {
            requestBuilder.error(error)
        } else if (error == -1 && placeholder != -1){
            requestBuilder.placeholder(placeholder)
        }else {
            requestBuilder.placeholder(placeholder).error(error)
        }

    }

    private fun loadRoundCorners(requestBuilder: RequestBuilder<Drawable>, view: ImageView,roundCorners: Int, placeholder: Int, error: Int) {
        resize(view,addPlaceholder(requestBuilder,placeholder, error)).apply(RequestOptions.bitmapTransform(RoundedCorners(roundCorners))).into(intoView(view))
    }

    private fun loadCircle(requestBuilder: RequestBuilder<Drawable>, view: ImageView,borderWidth: Float, borderColor: Int, placeholder: Int, error: Int) {
        resize(view,addPlaceholder(requestBuilder,placeholder, error)).apply(RequestOptions.bitmapTransform(GlideCircleCrop(borderWidth, borderColor))).into(intoView(view))
    }

    private fun resize(view: ImageView,requestBuilder: RequestBuilder<Drawable>): RequestBuilder<Drawable>{
        return requestBuilder.override(view.layoutParams.width, view.layoutParams.height).thumbnail(0.5f)
    }

    // 加载圆角图片
    fun loadRoundCorners(obj: Any, view: ImageView,roundCorners: Int, placeholder: Int, error: Int) {
        loadRoundCorners(loadObject(obj, Glide.with(view.context).asDrawable()), view,roundCorners,placeholder, error)
    }

    fun loadRoundCorners(bitmap: Bitmap, view: ImageView,roundCorners: Int) {
        loadRoundCorners(Glide.with(view.context).load(bitmap), view,roundCorners, -1, -1)
    }

    fun loadRoundCorners(url: String, view: ImageView,roundCorners: Int, placeholder: Int, error: Int) {
        loadRoundCorners(loadObject(url, Glide.with(view.context).asDrawable()), view,roundCorners, placeholder, error)
    }

    fun loadRoundCorners(resId: Int, view: ImageView,roundCorners: Int) {
        loadRoundCorners(Glide.with(view.context).load(resId), view,roundCorners, -1, -1)
    }

    fun loadRoundCorners(obj: Any, view: ImageView,roundCorners: Int, placeholder: Int, error: Int,centerCrop: Boolean) {
        var requestBuilder = Glide.with(view.context).asDrawable()
        if(centerCrop){
            requestBuilder = requestBuilder.centerCrop()
        }
        loadRoundCorners(loadObject(obj, requestBuilder), view,roundCorners,placeholder, error)
    }

    fun loadRoundCorners(bitmap: Bitmap, view: ImageView,roundCorners: Int,centerCrop: Boolean) {
        var requestBuilder = Glide.with(view.context).asDrawable()
        if(centerCrop){
            requestBuilder = requestBuilder.centerCrop()
        }
        loadRoundCorners(requestBuilder.load(bitmap), view,roundCorners, -1, -1)
    }

    fun loadRoundCorners(url: String, view: ImageView,roundCorners: Int, placeholder: Int, error: Int,centerCrop: Boolean) {
        var requestBuilder = Glide.with(view.context).asDrawable()
        if(centerCrop){
            requestBuilder = requestBuilder.centerCrop()
        }
        loadRoundCorners(loadObject(url, requestBuilder), view,roundCorners, placeholder, error)
    }

    fun loadRoundCorners(resId: Int, view: ImageView,roundCorners: Int,centerCrop: Boolean) {
        var requestBuilder = Glide.with(view.context).asDrawable()
        if(centerCrop){
            requestBuilder = requestBuilder.centerCrop()
        }
        loadRoundCorners(requestBuilder.load(resId), view,roundCorners, -1, -1)
    }

    // 加载圆形图片
    fun loadCircle(obj: Any, view: ImageView,borderWidth: Float, borderColor: Int, placeholder: Int, error: Int) {
        loadCircle(loadObject(obj, Glide.with(view.context).asDrawable()), view,borderWidth, borderColor, placeholder, error)
    }

    fun loadCircle(bitmap: Bitmap, view: ImageView,borderWidth: Float, borderColor: Int) {
        loadCircle(Glide.with(view.context).load(bitmap), view,borderWidth, borderColor, -1, -1)
    }

    fun loadCircle(url: String, view: ImageView,borderWidth: Float, borderColor: Int, placeholder: Int, error: Int) {
        loadCircle(loadObject(url, Glide.with(view.context).asDrawable()), view,borderWidth, borderColor, placeholder, error)
    }

    fun loadCircle(resId: Int, view: ImageView,borderWidth: Float, borderColor: Int) {
        loadCircle(Glide.with(view.context).load(resId), view,borderWidth, borderColor, -1, -1)
    }


    fun load(obj: Any, view: ImageView, placeholder: Int, error: Int) {
        resize(view,addPlaceholder(loadObject(obj, Glide.with(view).asDrawable()),placeholder, error)).into(intoView(view))
    }

    fun load(bitmap: Bitmap, view: ImageView) {
        resize(view,addPlaceholder(Glide.with(view.context).load(bitmap), -1, -1)).into(intoView(view))
    }

    fun load(url: String, view: ImageView, placeholder: Int, error: Int) {
        resize(view,addPlaceholder(loadObject(url, Glide.with(view.context).asDrawable()),placeholder, error)).into(intoView(view))
    }

    fun load(resId: Int, view: ImageView) {
        resize(view,addPlaceholder(Glide.with(view.context).load(resId),-1, -1)).into(intoView(view))
    }

    private fun intoView(view: ImageView): Target<Drawable>{
        return object : SimpleTarget<Drawable>(){
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.setImageDrawable(resource)
            }

        }
    }

    private fun loadGif(requestBuilder: RequestBuilder<Drawable>, view: ImageView,loopCount: Int, placeholder: Int, error: Int): ISingleListener<IVoidListener> {
        return ISingleListener {
            addPlaceholder(requestBuilder,placeholder, error)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if(resource is GifDrawable){
                                resource.setLoopCount(loopCount)
                                resource.start()
                                resource.registerAnimationCallback(object : Animatable2Compat.AnimationCallback(){
                                    override fun onAnimationEnd(drawable: Drawable?) {
                                        super.onAnimationEnd(drawable)
                                        it.invoke()
                                    }
                                })
                            }
                            return false
                        }
                    })
                    .into(view)
        }
    }

    fun loadGif(resId: Int, view: ImageView,loopCount: Int): ISingleListener<IVoidListener>{
        return loadGif(Glide.with(view.context).load(resId),view,loopCount,-1,-1)
    }

    fun getBitmap(context: Context, obj: Any, listener: ((Bitmap) -> Unit)) {
        loadObject(obj, Glide.with(context).asBitmap())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        listener.invoke(resource)
                    }
                })
    }

    fun getBitmapCircle(context: Context, obj: Any, listener: ((Bitmap) -> Unit)) {
        loadObject(obj, Glide.with(context).asBitmap())
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        listener.invoke(resource)
                    }
                })
    }

    fun getDrawable(context: Context, obj: Any, listener: ((Drawable) -> Unit)) {
        loadObject(obj, Glide.with(context).asDrawable())
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        listener.invoke(resource)
                    }
                })
    }

    fun getDrawable(context: Context, obj: Any,width: Int,height: Int, listener: ((Drawable) -> Unit)) {
        loadObject(obj, Glide.with(context).asDrawable())
                .override(width, height)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        listener.invoke(resource)
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun getVideoScreenshot(
            context: Context,
            uri: String,
            frameTimeMicros: Long,
            listener: ((Bitmap) -> Unit)
    ) {
        val requestOptions = RequestOptions.frameOf(frameTimeMicros)
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
        requestOptions.transform(object : BitmapTransformation() {
            override fun transform(
                    pool: BitmapPool,
                    toTransform: Bitmap,
                    outWidth: Int,
                    outHeight: Int
            ): Bitmap {
                return toTransform
            }

            override fun updateDiskCacheKey(messageDigest: MessageDigest) {
                try {
                    messageDigest.update(
                            (context.packageName + "RotateTransform").toByteArray(
                                    charset("utf-8")
                            )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        listener.invoke(resource)
                    }
                })
    }


}
