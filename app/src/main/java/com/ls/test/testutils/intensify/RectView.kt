package com.ls.test.testutils.intensify

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

/**
 * @ClassName: RectView
 * @Description:
 * @Author: ls
 * @Date: 2020/9/22 18:32
 */
class RectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mDrawingRect =  Rect()
    private val mGlobalRect = Rect(0,0,10000,10000)
    private val mGlobalRectF = RectF()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
        color = Color.RED
    }
    private val mPaint2 = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
        color = Color.GREEN
    }
    private val mGestureDetector: GestureDetector
    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener(){
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            mScaleMatrix.postTranslate(-distanceX,-distanceY)
            invalidate()
            return true
        }
    }
    private val mScaleGestureDetector: ScaleGestureDetector
    private val mScaleGestureListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener(){
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleMatrix.postScale(detector.scaleFactor,detector.scaleFactor)
            invalidate()
            return true
        }
    }
    private val mScaleMatrix = Matrix()

    init {
        mGestureDetector = GestureDetector(context,mGestureListener)
        mScaleGestureDetector = ScaleGestureDetector(context,mScaleGestureListener)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        mScaleGestureDetector.onTouchEvent(event)
        return true
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        getDrawingRect(mDrawingRect)
        mGlobalRectF.set(mGlobalRect)
        mScaleMatrix.mapRect(mGlobalRectF)
        Log.d("123123123","mDrawingRect $mGlobalRect mDrawingRectF ${mGlobalRectF}")
        canvas.drawRect(mGlobalRect,mPaint)
//        canvas.drawRect(mDrawingRect,mPaint2)

    }

    // 获取当前图片的缩放值
    private fun getScale(): Float {
        val values = FloatArray(9)
        mScaleMatrix.getValues(values)
        return values[Matrix.MSCALE_X]
    }
}