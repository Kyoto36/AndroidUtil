package com.ls.custom_view_library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.contains
import com.ls.comm_util_library.LogUtils


@RequiresApi(Build.VERSION_CODES.KITKAT)
class ScaleImageView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), ViewTreeObserver.OnGlobalLayoutListener {

    private var mScaleMatrix = Matrix()
    private var mFocusPoint = PointF()
    private var mGestureDetector: GestureDetector
    private var mScaleGestureDetector: ScaleGestureDetector
    private var mBaseScale = 0F
    private var mMaxScale = 0F
    private var mMinScale = 0F
    private var mIsAutoScale = false

    private val mOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (mIsAutoScale || !getMatrixRectF().contains(e.x,e.y)) {
                return false
            }

            mFocusPoint.x = e.x
            mFocusPoint.y = e.y

            if (getScale() < mMaxScale) {
                postDelayed(AutoScale(mMaxScale), 16)
                mIsAutoScale = true
            } else {
                postDelayed(AutoScale(mBaseScale), 16)
                mIsAutoScale = true
            }
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val rect = getMatrixRectF()
            var deltaX = 0f
            var deltaY = 0f
            val width = width
            val height = height
            // 检查是否可以滑动
            if (rect.left < 0 && distanceX < 0) {
                if (Math.abs(distanceX) < Math.abs(rect.left)) {
                    deltaX = -distanceX
                } else {
                    deltaX = -rect.left
                }
            }
            if (rect.right > width && distanceX > 0) {
                val rightDis = rect.right - width
                if (Math.abs(distanceX) < Math.abs(rightDis)) {
                    deltaX = -distanceX
                } else {
                    deltaX = -rightDis
                }
            }
            if (rect.top < 0 && distanceY < 0) {
                if (Math.abs(distanceY) < Math.abs(rect.top)) {
                    deltaY = -distanceY
                } else {
                    deltaY = -rect.top
                }
            }
            if (rect.bottom > height && distanceY > 0) {
                val bottomDis = rect.bottom - height
                if (Math.abs(distanceY) < Math.abs(bottomDis)) {
                    deltaY = -distanceY
                } else {
                    deltaY = -bottomDis
                }
            }
            if(deltaX != 0F || deltaY != 0F) {
                mScaleMatrix.postTranslate(deltaX, deltaY)
                imageMatrix = mScaleMatrix
                return true
            }
            return false
        }
    }

    private val mOnScaleGestureListener =
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            private var mScaleFactorOld = 1F

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                if(!getMatrixRectF().contains(mFocusPoint.x, mFocusPoint.y)) return false
                scale(detector.scaleFactor * mScaleFactorOld)
                return false
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                mFocusPoint.set(detector.focusX, detector.focusY)
                mScaleFactorOld = getScale()
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                super.onScaleEnd(detector)
                if(getScale() > mMaxScale){
                    postDelayed(AutoScale(mMaxScale), 16)
                }
                else if(getScale() < mMinScale){
                    postDelayed(AutoScale(mMinScale), 16)
                }
            }
        }

    init {
        mGestureDetector = GestureDetector(context, mOnGestureListener)
        mScaleGestureDetector = ScaleGestureDetector(context, mOnScaleGestureListener)
        scaleType = ScaleType.MATRIX
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        mScaleGestureDetector.onTouchEvent(event)
        return true
    }

    private fun scale(scale: Float) {
        val oldScale = getScale()
        mScaleMatrix.postScale(scale / oldScale, scale / oldScale, mFocusPoint.x, mFocusPoint.y)
        borderAndCenterCheck()
        imageMatrix = mScaleMatrix
    }

    // 获取当前图片的缩放值
    private fun getScale(): Float {
        val values = FloatArray(9)
        mScaleMatrix.getValues(values)
        return values[Matrix.MSCALE_X]
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    private var mOnce = true
    override fun onGlobalLayout() {
//        if (mOnce) {
//            mOnce = false
            // 获取控件的宽度和高度
            val width = width
            val height = height
            // 获取到ImageView对应图片的宽度和高度
            val d = drawable ?: return
            val dw = d.intrinsicWidth // 图片固有宽度
            val dh = d.intrinsicHeight // 图片固有高度

            var scale = 0f
            mScaleMatrix = Matrix()
            // 图片宽度大于控件宽度但高度小于控件高度
            if(dw > width || dh > height){
                val scaleW = width * 1F / dw
                val scaleH = height * 1F / dh
                scale = if(scaleW < scaleH) scaleW else scaleH
                Log.d("aaaaa","scale = $scale scaleW $scaleW scaleH $scaleH")
            }

            mBaseScale = scale
            mMaxScale = mBaseScale * 3F
            mMinScale = mBaseScale * 0.7F
            // 将图片移动到手机屏幕的中间位置
            val dx = width / 2 - dw / 2.toFloat()
            val dy = height / 2 - dh / 2.toFloat()
            mScaleMatrix.postTranslate(dx, dy)
            mScaleMatrix.postScale(
                mBaseScale, mBaseScale, (width / 2).toFloat(),
                (height / 2).toFloat()
            )
            imageMatrix = mScaleMatrix
//        }
    }

    /**
     * 图片在缩放时进行边界控制
     */
    private fun borderAndCenterCheck() {
        val rect: RectF = getMatrixRectF()
        var deltaX = 0f
        var deltaY = 0f
        val width = width
        val height = height
        // 缩放时进行边界检测，防止出现白边
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left
            }
            if (rect.right < width) {
                deltaX = width - rect.right
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom
            }
        }
        // 如果宽度或者高度小于控件的宽或者高；则让其居中
        if (rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2f
        }
        if (rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2f
        }
        mScaleMatrix.postTranslate(deltaX, deltaY)
    }

    /**
     * 获得图片放大缩小以后的宽和高
     *
     * @return
     */
    private fun getMatrixRectF(): RectF {
        val matrix: Matrix = mScaleMatrix
        val rectF = RectF()
        val d: Drawable? = drawable
        if (d != null) {
            rectF.set(0F, 0F, d.intrinsicWidth.toFloat(), d.intrinsicHeight.toFloat())
            matrix.mapRect(rectF)
        }
        return rectF
    }

    inner class AutoScale(targetScale: Float) : Runnable{

        private val BIGGER = 1.07f
        private val SMALL = 0.93f
        private val mTargetScale = targetScale
        private var mTmpScale = 0f

        init {
            if (getScale() < mTargetScale) {
                mTmpScale = BIGGER
            }
            if (getScale() > mTargetScale) {
                mTmpScale = SMALL
            }
        }

        override fun run() {
            if(mTmpScale == 0F) return
            //进行缩放
            mScaleMatrix.postScale(mTmpScale, mTmpScale, mFocusPoint.x, mFocusPoint.y)
            borderAndCenterCheck()
            imageMatrix = mScaleMatrix

            val currentScale = getScale()
            if (mTmpScale > 1.0f && currentScale < mTargetScale || mTmpScale < 1.0f && currentScale > mTargetScale) {
                //这个方法是重新调用run()方法
                postDelayed(this, 16)
            } else {
                //设置为我们的目标值
                val scale = mTargetScale / currentScale
                mScaleMatrix.postScale(scale, scale, mFocusPoint.x, mFocusPoint.y)
                borderAndCenterCheck()
                imageMatrix = mScaleMatrix
                mIsAutoScale = false
            }
        }

    }


}