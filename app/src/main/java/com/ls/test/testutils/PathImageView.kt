package com.ls.test.testutils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @ClassName: PathImageView
 * @Description:
 * @Author: ls
 * @Date: 2020/8/31 12:21
 */
class PathImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mRadius = 0F
    private val mPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
    }
    private val mBorderPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.GREEN
    }

    private var mCurrpath: Path? = null

    private val mBorderWidth = 10F

    init {
        mBorderPaint.strokeWidth = mBorderWidth
    }

    fun setPath(path: Path){
        mCurrpath = path
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mRadius = if(measuredWidth > measuredHeight) (measuredHeight / 2).toFloat() else (measuredWidth / 2).toFloat()
    }
    override fun onDraw(canvas: Canvas) {
        if(drawable == null) return
//        drawCircle(canvas)
        drawSquare(6,canvas)
        mBorderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
        mBorderPaint.color = Color.parseColor("#F5EEEA")
        mBorderPaint.alpha = 38
        canvas.drawPath(mCurrpath!!,mBorderPaint)
        mBorderPaint.xfermode = null
    }


    private fun drawSquare(sideNum: Int,canvas: Canvas){
        mCurrpath = getPath(sideNum,mRadius, (mBorderWidth / 2).toInt())
        canvas.rotate(30F, (width/2).toFloat(), (height/2).toFloat())
        mPaint.color = Color.RED
        canvas.drawPath(mCurrpath!!,mPaint)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        if(mBitmap == null){
            getBitmap()
        }
        canvas.rotate(-30F, (width/2).toFloat(), (height/2).toFloat())
        canvas.drawBitmap(mBitmap!!,0F,0F,mPaint)
        mPaint.xfermode = null
        canvas.rotate(30F, (width/2).toFloat(), (height/2).toFloat())
        mBorderPaint.color = Color.parseColor("#F5EEEA")
        canvas.drawPath(mCurrpath!!,mBorderPaint)
        canvas.rotate(-30F, (width/2).toFloat(), (height/2).toFloat())
    }

    fun sin(num: Int): Float {
        return Math.sin(num * Math.PI / 180).toFloat()
    }

    fun cos(num: Int): Float {
        return Math.cos(num * Math.PI / 180).toFloat()
    }

    private fun getCircle(radius: Float,padding: Int): Path{
        val path = Path()
        path.addCircle(radius,radius,radius - padding,Path.Direction.CW)
        return path
    }

    private fun getPath(num: Int, radius: Float,padding: Int): Path {
        val path = Path()
        var x = 0F
        var y = 0F
        for (i in 0 until num) {
            x = radius + radius * cos(360 / num * i)
            y = radius + radius * sin(360 / num * i)
            if(x < radius) x += padding else x -= padding
            if(y < radius) y += padding else y -= padding
            if (i == 0) {
                path.moveTo(x,y) //绘制起点
            } else {
                path.lineTo(x,y)
            }
        }
        path.close()
        return path
    }

    private fun drawCircle(canvas: Canvas){
        mCurrpath = getCircle(mRadius, (mBorderWidth / 2).toInt())
        canvas.drawPath(mCurrpath!!,mPaint)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        if(mBitmap == null){
            getBitmap()
        }
        canvas.drawBitmap(mBitmap!!,0F,0F,mPaint)
        mPaint.xfermode = null
        canvas.drawPath(mCurrpath!!,mBorderPaint)
    }

    private var mBitmap: Bitmap? = null

    private fun getBitmap(){
        mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mBitmap!!)
        canvas.concat(imageMatrix)
        drawable.draw(canvas)
    }
}