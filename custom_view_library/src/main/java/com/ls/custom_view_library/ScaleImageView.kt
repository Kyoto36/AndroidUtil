package com.ls.custom_view_library

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.text.TextUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatImageView
import com.ls.comm_util_library.*
import java.io.File
import java.io.InputStream


class ScaleImageView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), ViewTreeObserver.OnGlobalLayoutListener {

    private val mHandlerThread by lazy {
        HandlerThread("ScaleImageView-work")
    }

    private lateinit var mWorkHandler: Handler

    private var mScaleMatrix = Matrix()
    private var mFocusPoint = PointF()
    private var mGestureDetector: GestureDetector
    private var mScaleGestureDetector: ScaleGestureDetector
    private var mBaseScale = 0F
    private var mNormalScale = 0F
    private var mMaxScale = 0F
    private var mMinScale = 0F
    private var mIsAutoScale = false
    private var mIsDownSlide = false
    private var mIsScale = false
    private var mIsDisableScale = false
    val mOptions = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
    }
    var mBitmapWidth = 0
    var mBitmapHeight = 0
    var mBitmapRegionDecoder : BitmapRegionDecoder? = null

    private var mOnDownSlideListener: IOnDownSlideListener? = null
    private var mScroller = Scroller(context)

    private val mOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            performClick()
            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
//            if (mIsAutoScale || !getMatrixRectF().contains(e.x, e.y)) {
            if(mIsDisableScale || mIsAutoScale){
                return false
            }
            mFocusPoint.x = e.x
            mFocusPoint.y = e.y

            val scale = getScale()

            if(scale >= mMaxScale){
                autoScale(mBaseScale)
            }
            else if(scale >= mNormalScale){
                autoScale(mMaxScale)
            }
            else if(scale >= mBaseScale){
                autoScale(mNormalScale)
            }
            else{
                autoScale(mBaseScale)
            }
            mIsAutoScale = true
            mIsScale = true
            return true
        }

        var mLastDownSlideDis = 0F

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (mIsScale) return false
            if(scroll(distanceX,distanceY)){
                return true
            }
            val distanceRawY = e1.rawY - e2.rawY
            val distanceRawX = e1.rawX - e2.rawX
            if (Math.abs(distanceRawY) > 0 && Math.abs(distanceRawY) > Math.abs(distanceRawX) + dp2px(20F) && getScale() <= mBaseScale) {
                if (!mIsDownSlide) {
                    mIsDownSlide = true
                    mLastDownSlideDis = distanceRawY
                    mOnDownSlideListener?.onStart()
                }
                if (mIsDownSlide) {
                    parent?.requestDisallowInterceptTouchEvent(true)
                    mOnDownSlideListener?.onScroll(mLastDownSlideDis - distanceRawY)
                    mLastDownSlideDis = distanceRawY
                    return true
                }
            }
            parent?.requestDisallowInterceptTouchEvent(false)
            return false
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return if(getScale() > mBaseScale){
//                val rect = getMatrixRectF()
//                mScroller.fling(e2.x.toInt(),e2.y.toInt(),velocityX.toInt(),velocityY.toInt(),0, 100,0, 100)
//                invalidate()
                true
            } else {
                parent?.requestDisallowInterceptTouchEvent(false)
                endDownSlide(mIsScale, velocityY)
                super.onFling(e1, e2, velocityX, velocityY)
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scroll((mScroller.currX - mScroller.startX).toFloat(), (mScroller.currY - mScroller.startY).toFloat())
            postInvalidate()
        }
    }

    private fun scroll(distanceX: Float,distanceY: Float): Boolean{
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
        if (deltaX != 0F || deltaY != 0F) {
            parent.requestDisallowInterceptTouchEvent(true)
            mScaleMatrix.postTranslate(deltaX, deltaY)
            reDraw()
//                endDownSlide()
            return true
        }
        parent.requestDisallowInterceptTouchEvent(false)
        return false
    }

    private val mOnScaleGestureListener =
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            private var mScaleFactorOld = 1F

            override fun onScale(detector: ScaleGestureDetector): Boolean {
//                if (!getMatrixRectF().contains(mFocusPoint.x, mFocusPoint.y)) return false
                if (mIsDisableScale) return false
                scale(detector.scaleFactor * mScaleFactorOld)
                return false
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                mFocusPoint.set(detector.focusX, detector.focusY)
                mScaleFactorOld = getScale()
                mIsScale = true
//                endDownSlide()
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                super.onScaleEnd(detector)
                if (getScale() > mMaxScale) {
                    autoScale(mMaxScale)
                } else if (getScale() < mMinScale) {
                    autoScale(mMinScale)
                } else {
                    reDraw()
                    mIsScale = false
                }
            }
        }

    init {
        mHandlerThread.start()
        mWorkHandler = Handler(mHandlerThread.looper)
        mGestureDetector = GestureDetector(context, mOnGestureListener)
        mScaleGestureDetector = ScaleGestureDetector(context, mOnScaleGestureListener)
        scaleType = ScaleType.MATRIX
    }

    fun setOnDownSlideListener(listener: IOnDownSlideListener?) {
        mOnDownSlideListener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val scaleTouch = mScaleGestureDetector.onTouchEvent(event)
        val touch = mGestureDetector.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP) {
            endDownSlide()
        }
        if(event.action == MotionEvent.ACTION_DOWN){
            if(getScale() > mBaseScale){
                parent?.requestDisallowInterceptTouchEvent(true)
            }
            else{
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return true
    }

    private fun endDownSlide() {
        if (mIsDownSlide) {
            mIsDownSlide = false
            mOnDownSlideListener?.onEnd(true, 0F)
        }
    }

    private fun endDownSlide(cancel: Boolean, velocity: Float) {
        if (mIsDownSlide) {
            mIsDownSlide = false
            mOnDownSlideListener?.onEnd(cancel, velocity)
        }
    }

    private fun scale(scale: Float) {
        val oldScale = getScale()
        mScaleMatrix.postScale(scale / oldScale, scale / oldScale, mFocusPoint.x, mFocusPoint.y)
        borderAndCenterCheck()
        reDraw()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
        mScaleMatrix.reset()
        mWorkHandler.removeCallbacksAndMessages(null)
        setImageDrawable(null)
    }

    private fun loadImage(){
        var dw = 0 // 图片固有宽度
        var dh = 0 // 图片固有高度
        val d = drawable?: return
        dw = d.intrinsicWidth
        dh = d.intrinsicHeight
        // 获取控件的宽度和高度
        val width = measuredWidth
        val height = measuredHeight
        mScaleMatrix = Matrix()
        // 图片宽度大于控件宽度但高度小于控件高度
        val scaleW = width * 1F / dw
        val scaleH = height * 1F / dh
        mNormalScale = if (scaleW > scaleH) scaleW else scaleH
        mBaseScale = if (scaleW < scaleH) scaleW else scaleH

        mMaxScale = mNormalScale * 2F
        mMinScale = mBaseScale * 0.7F


        // 将图片移动到手机屏幕的中间位置
        val dx = width / 2 - dw / 2.toFloat()
        val dy = height / 2 - dh / 2.toFloat()
        mScaleMatrix.postTranslate(dx, dy)
        mScaleMatrix.postScale(mBaseScale, mBaseScale, (width / 2).toFloat(), (height / 2).toFloat())
        reDraw()
    }

    private fun reDraw(){
        imageMatrix = mScaleMatrix
    }

    private fun getDisplayRegion(): RectF {
        val imageRect = getMatrixRectF()
        return RectF().apply {
            left = if (imageRect.left < 0) -imageRect.left else 0F
            top = if (imageRect.top < 0) -imageRect.top else 0F
            right = if (imageRect.right > width) width + left else imageRect.right
            bottom = if (imageRect.bottom > height()) height + top else imageRect.bottom
        }
    }

    private fun loadBitmap(listener: IResultListener<InputStream>,unknownListener: IVoidListener){
        mIsDisableScale = true
        post {
            mWorkHandler.post {
                mOptions.inJustDecodeBounds = true
                var inputStream = listener.onResult()
                BitmapFactory.decodeStream(inputStream, null, mOptions)
                if (isGif(mOptions.outMimeType)) {
                    post {
                        mIsDisableScale = false
                        unknownListener.invoke()
                    }
                }
                else {
//            inputStream = listener.onResult()
//            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false)
                    mBitmapWidth = mOptions.outWidth
                    mBitmapHeight = mOptions.outHeight
                    mOptions.inSampleSize = calculateInSampleSize(mBitmapWidth, mBitmapHeight, measuredWidth, measuredHeight)
                    mOptions.inJustDecodeBounds = false
                    LogUtils.d("123456456", "mOptions.inSampleSize ${mOptions.inSampleSize} mBitmapWidth $mBitmapWidth mBitmapHeight $mBitmapHeight")
                    inputStream?.close()
                    inputStream = listener.onResult()
                    val bitmap = BitmapFactory.decodeStream(inputStream, null, mOptions)
                    inputStream?.close()
//                val cache = AndroidFileUtils.getCachePath(context) + "bitmap_${System.currentTimeMillis()}"
                    setBitmap(bitmap)
                }
            }
        }
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight 计算inSampleSize（采样率）
     */
    private fun calculateInSampleSize(srcWidth: Int,srcHeight: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        while ((srcWidth / inSampleSize) > reqHeight || (srcHeight / inSampleSize) > reqWidth) {
            inSampleSize *= 2
        }
        LogUtils.d("123456456","srcWidth $srcWidth srcHeight $srcHeight reqWidth $reqWidth reqHeight $reqHeight inSampleSize $inSampleSize")
        return inSampleSize
    }

    private fun isGif(mimeType: String): Boolean{
        return mimeType.endsWith("gif",true)
    }

    private fun setBitmap(bitmap: Bitmap?){
        if(bitmap == null) return
        post {
            setImageBitmap(bitmap)
            loadImage()
            mIsDisableScale = false
        }
    }

    fun setImage(path: String?,unknownListener: IVoidListener) {
        if(TextUtils.isEmpty(path)) return
        loadBitmap(IResultListener {
            FileUtils.getInputStreamByFile(path)
        },unknownListener)
    }

    fun setImage(file: File?,unknownListener: IVoidListener) {
        if(file == null || !file.exists()) return
       setImage(file.absolutePath,unknownListener)
    }

    fun setImage(uri: Uri?,unknownListener: IVoidListener) {
        if(uri == null) return
        loadBitmap(IResultListener {
            AndroidFileUtils.getInputStreamByUri(context,uri)
        },unknownListener)
    }

    fun setImage(resId: Int,unknownListener: IVoidListener){
        loadBitmap(IResultListener {
            context.resources.openRawResource(resId)
        },unknownListener)
    }

    override fun onGlobalLayout() {
        loadImage()
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
        if(drawable != null){
            rectF.set(0F, 0F, drawable!!.intrinsicWidth.toFloat(), drawable!!.intrinsicHeight.toFloat())
            matrix.mapRect(rectF)
        }
        return rectF
    }

    /**
     * 换成属性动画也挺丝滑的
     */
    inner class AutoScale(targetScale: Float) : Runnable {

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
            if (mTmpScale == 0F) return
            //进行缩放
            mScaleMatrix.postScale(mTmpScale, mTmpScale, mFocusPoint.x, mFocusPoint.y)
            borderAndCenterCheck()
            reDraw()

            val currentScale = getScale()
            if (mTmpScale > 1.0f && currentScale < mTargetScale || mTmpScale < 1.0f && currentScale > mTargetScale) {
                //这个方法是重新调用run()方法
                postDelayed(this, 15)
            } else {
                //设置为我们的目标值
                val scale = mTargetScale / currentScale
                mScaleMatrix.postScale(scale, scale, mFocusPoint.x, mFocusPoint.y)
                borderAndCenterCheck()
                reDraw()
                mIsAutoScale = false
                mIsScale = false
            }
        }

    }

    private fun autoScale(targetScale: Float){
//        postDelayed(AutoScale(targetScale), 15)
//        return
        if(getScale() == targetScale) {
            mIsAutoScale = false
            mIsScale = false
            return
        }
        val animator = ValueAnimator.ofFloat(getScale(),targetScale)
        animator.duration = 300
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            scale(it.animatedValue as Float)
        }
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                scale(targetScale)
                mIsAutoScale = false
                mIsScale = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                scale(targetScale)
                mIsAutoScale = false
                mIsScale = false
            }
            override fun onAnimationStart(animation: Animator?) {}
        })
        animator.start()
    }

    interface IOnDownSlideListener {
        fun onStart()
        fun onScroll(distance: Float)
        fun onEnd(cancel: Boolean, velocity: Float)
    }


}