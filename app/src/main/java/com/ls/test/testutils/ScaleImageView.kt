package com.ls.test.testutils

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView

@RequiresApi(Build.VERSION_CODES.KITKAT)
class ScaleImageView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    private var mGestureDetector: GestureDetector
    private var mScaleGestureDetector: ScaleGestureDetector

    private val mOnGestureListener = object: GestureDetector.SimpleOnGestureListener(){
        override fun onDoubleTap(e: MotionEvent): Boolean {
            return super.onDoubleTap(e)
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    private val mOnScaleGestureListener = ScaleGestureDetector.SimpleOnScaleGestureListener()

    init {
        mGestureDetector = GestureDetector(context, mOnGestureListener)
        mScaleGestureDetector = ScaleGestureDetector(context, mOnScaleGestureListener)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        return mScaleGestureDetector.onTouchEvent(event)
    }

    private var mOnce = true
    override fun onDraw(canvas: Canvas) {
        if(mOnce){

        }
        super.onDraw(canvas)
    }
}