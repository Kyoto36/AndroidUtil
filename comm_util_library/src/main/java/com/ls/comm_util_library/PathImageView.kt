package com.ls.comm_util_library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @ClassName: PathImageView
 * @Description:
 * @Author: ls
 * @Date: 2020/8/31 15:47
 */
open class PathImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    class Config{
        private var mPath: Path? = null
        private var mBorderWidth = 0F
        private var mBorderColor = Color.WHITE
        private var mSquareRotate = 0F

        fun setPath(path: Path): Config{
            mPath = path
            return this
        }

        fun getPath(): Path?{
            return mPath
        }

        fun setRotate(rotate: Float): Config{
            mSquareRotate = rotate
            return this
        }

        fun getRotate(): Float{
            return mSquareRotate
        }

        fun setBorderWidth(width: Float): Config{
            mBorderWidth = width
            return this
        }

        fun getBorderWidth(): Float{
            return mBorderWidth
        }

        fun setBorderColor(color: Int):Config{
            mBorderColor = color
            return this
        }

        fun getBorderColor(): Int{
            return mBorderColor
        }
    }

    protected var mConfig = Config()
    protected var mConfigListener: ISingleResultListener<Size,Config>? = null

    init {
//        setLayerType(LAYER_TYPE_SOFTWARE,null)
    }

    override fun onDraw(canvas: Canvas) {
        if(mConfig.getPath() == null || measuredWidth <= 0 || measuredHeight <= 0) {
            super.onDraw(canvas)
            return
        }
        canvas.drawBitmap(drawPath(mConfig.getPath()!!), 0F, 0F,null)
    }

    fun setConfig(config: Config){
        mConfig = config
        invalidate()
    }

    fun setConfig(configListener: ISingleResultListener<Size,Config>){
        mConfigListener = configListener
        post {
            mConfig = mConfigListener!!.onResult(Size(measuredWidth,measuredHeight))
            invalidate()
        }
    }

    protected open fun drawPath(path: Path): Bitmap{
        val bitmap = recyclerBitmap(mBitmap)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            color = mConfig.getBorderColor()
            style = Paint.Style.FILL
        }
        canvas.rotate(mConfig.getRotate(), (measuredWidth/2).toFloat(), (measuredHeight/2).toFloat())
        canvas.drawPath(path,paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.rotate(-mConfig.getRotate(), (measuredWidth/2).toFloat(), (measuredHeight/2).toFloat())
        canvas.drawBitmap(getImageBitmap(), 0F, 0F, paint)
        paint.xfermode = null
        paint.apply {
            style = Paint.Style.STROKE
            color = mConfig.getBorderColor()
            strokeWidth = mConfig.getBorderWidth()
        }
        canvas.rotate(mConfig.getRotate(), (measuredWidth/2).toFloat(), (measuredHeight/2).toFloat())
        canvas.drawPath(path,paint)
        canvas.rotate(-mConfig.getRotate(), (measuredWidth/2).toFloat(), (measuredHeight/2).toFloat())
        return bitmap
    }

    protected var mBitmap: Bitmap? = null
    protected var mImageBitmap: Bitmap? = null

    private fun getImageBitmap(): Bitmap{
        val bitmap = recyclerBitmap(mImageBitmap)
        val canvas = Canvas(bitmap)
        canvas.drawColor(mConfig.getBorderColor(), PorterDuff.Mode.CLEAR);
        canvas.concat(imageMatrix)
        drawable?.draw(canvas)
        return bitmap
    }

    protected fun recyclerBitmap(bitmap: Bitmap?): Bitmap{
        var resultBitmap = bitmap
        if(resultBitmap == null) {
            resultBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        }
        else if(resultBitmap.width != measuredWidth || resultBitmap.height != measuredHeight) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                resultBitmap.reconfigure(measuredWidth,measuredHeight,resultBitmap.config)
            }
            else{
                resultBitmap.recycle()
                resultBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            }
        }
        return resultBitmap!!
    }
}