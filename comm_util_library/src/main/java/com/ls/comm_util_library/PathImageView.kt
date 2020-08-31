package com.ls.comm_util_library

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @ClassName: PathImageView
 * @Description:
 * @Author: ls
 * @Date: 2020/8/31 15:47
 */
class PathImageView @JvmOverloads constructor(
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

    private var mBitmap: Bitmap? = null
    private var mConfig = Config()
    private var mConfigListener: ISingleResultListener<Size,Config>? = null

    override fun onDraw(canvas: Canvas) {
        if(drawable == null) return
        if(mConfig.getPath() == null) {
            super.onDraw(canvas)
            return
        }
        drawPath(mConfig.getPath()!!,canvas)
    }

    fun setConfig(config: Config){
        mConfig = config
        invalidate()
    }

    fun setConfig(configListener: ISingleResultListener<Size,Config>){
        mConfigListener = configListener
        post {
            mConfig = mConfigListener!!.onResult(Size(width,height))
            invalidate()
        }
    }

    private fun drawPath(path: Path,canvas: Canvas){
        val paint = Paint().apply {
            isAntiAlias = true
        }
        canvas.rotate(mConfig.getRotate(), (width/2).toFloat(), (height/2).toFloat())
        canvas.drawPath(path,paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        if(mBitmap == null){
            getBitmap()
        }
        canvas.rotate(-mConfig.getRotate(), (width/2).toFloat(), (height/2).toFloat())
        canvas.drawBitmap(mBitmap!!,0F,0F,paint)
        paint.xfermode = null
        paint.apply {
            style = Paint.Style.STROKE
            color = mConfig.getBorderColor()
            strokeWidth = mConfig.getBorderWidth()
        }
        canvas.rotate(mConfig.getRotate(), (width/2).toFloat(), (height/2).toFloat())
        canvas.drawPath(path,paint)
    }

    private fun getBitmap(): Bitmap{
        if(mBitmap == null){
            mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
            val canvas = Canvas(mBitmap!!)
            canvas.concat(imageMatrix)
            drawable.draw(canvas)
        }
        return mBitmap!!
    }
}