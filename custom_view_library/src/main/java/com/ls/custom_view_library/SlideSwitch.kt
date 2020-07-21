package com.ls.custom_view_library

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ls.comm_util_library.LogUtils
import com.ls.comm_util_library.Util

class SlideSwitch @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG) //抗锯齿

    internal var isOn = false
    internal var curX = 0f
    internal var centerY: Float = 0.toFloat() //y固定
    internal var viewWidth: Float = 0.toFloat()
    internal var radius: Float = 0.toFloat()
    internal var lineStart: Float = 0.toFloat() //直线段开始的位置（横坐标，即
    internal var lineEnd: Float = 0.toFloat() //直线段结束的位置（纵坐标
    internal var lineWidth: Float = 0.toFloat()
    internal val SCALE = 4 // 控件长度为滑动的圆的半径的倍数
    internal var onStateChangedListener: OnStateChangedListener? = null


    private var mTouch = false

    private var mOnColor = Color.RED
    private var mOffColor = Color.GRAY
    private var mIndicatorColor = Color.WHITE

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SwitchView)
        mOnColor = array.getColor(R.styleable.SwitchView_onColor,mOnColor)
        mOffColor = array.getColor(R.styleable.SwitchView_offColor,mOffColor)
        mIndicatorColor = array.getColor(R.styleable.SwitchView_indicatorColor,mIndicatorColor)
        array.recycle()
    }

    fun setTouch(enableTouch: Boolean){
        mTouch = enableTouch
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mTouch) {
            curX = event.x
            if (event.action == MotionEvent.ACTION_UP) {
                if (curX > viewWidth / 2) {
                    curX = lineEnd
                    if (!isOn) {
                        //只有状态发生改变才调用回调函数， 下同
                        onStateChangedListener?.onStateChanged(true)
                        isOn = true
                    }
                } else {
                    curX = lineStart
                    if (isOn) {
                        onStateChangedListener?.onStateChanged(false)
                        isOn = false
                    }
                }
            }
            /*通过刷新调用onDraw*/
            this.postInvalidate()
            return true
        } else {
            return super.onTouchEvent(event)
        }
    }

    private var mSwitchOnAnim: ValueAnimator? = null
    fun switchOn() {
        if(!isOn) {
            if(null != mSwitchOffAnim && mSwitchOffAnim!!.isRunning){
                mSwitchOffAnim!!.end()
            }
            isOn = true
            mSwitchOnAnim = ValueAnimator.ofFloat(lineStart, lineEnd)
            mSwitchOnAnim!!.duration = 300
            mSwitchOnAnim!!.addUpdateListener {
                curX = it.animatedValue as Float
                postInvalidate()
            }
            mSwitchOnAnim!!.start()
            onStateChangedListener?.onStateChanged(true)
        }
    }

    private var mSwitchOffAnim: ValueAnimator? = null
    fun switchOff(){
        if(isOn) {
            if(null != mSwitchOnAnim && mSwitchOnAnim!!.isRunning){
                mSwitchOnAnim!!.end()
            }
            isOn = false
            mSwitchOffAnim = ValueAnimator.ofFloat(lineEnd, lineStart)
            mSwitchOffAnim!!.duration = 300
            mSwitchOffAnim!!.addUpdateListener {
                curX = it.animatedValue as Float
                postInvalidate()
            }
            mSwitchOffAnim!!.start()
            onStateChangedListener?.onStateChanged(false)
        }
    }

    fun toggleSwitch(listener: OnStateChangedListener){
        setOnStateChangedListener(listener)
        if(isOn){
            switchOff()
        }
        else{
            switchOn()
        }
    }

    fun setOn(on: Boolean){
        isOn = on
        post {
            if(isOn){
                curX = lineEnd
                postInvalidate()
            }
            else{
                curX = lineStart
                postInvalidate()
            }
        }
    }

    fun isOn(): Boolean{
        return isOn
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /*保持宽是高的SCALE / 2倍， 即圆的直径*/
        this.setMeasuredDimension(this.measuredWidth, this.measuredWidth * 2 / SCALE)
        viewWidth = this.measuredWidth.toFloat()
        radius = viewWidth / SCALE
        lineWidth = radius * 2f //直线宽度等于滑块直径
        if(curX == 0F) {
            curX = radius
        }
        centerY = (this.measuredWidth / SCALE).toFloat() //centerY为高度的一半
        lineStart = radius
        lineEnd = (SCALE - 1) * radius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*限制滑动范围*/
        curX = if (curX > lineEnd) lineEnd else curX
        curX = if (curX < lineStart) lineStart else curX

        val save = canvas.saveLayer(0F,0F,width.toFloat(),height.toFloat(),mPaint, Canvas.ALL_SAVE_FLAG)
        /*划线*/
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = lineWidth
        /*左边部分的线，绿色*/
        mPaint.color = mOnColor
        canvas.drawLine(lineStart, centerY, curX, centerY, mPaint)
        /*右边部分的线，灰色*/
        mPaint.color = mOffColor
        canvas.drawLine(curX, centerY, lineEnd, centerY, mPaint)

        /*画圆*/
        /*画最左和最右的圆，直径为直线段宽度， 即在直线段两边分别再加上一个半圆*/
//        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        mPaint.style = Paint.Style.FILL
        mPaint.color = if (curX == lineEnd)  mOnColor else mOffColor
        canvas.drawCircle(lineEnd, centerY, lineWidth / 2, mPaint)
        mPaint.color = if (curX == lineStart) mOffColor else mOnColor
        canvas.drawCircle(lineStart, centerY, lineWidth / 2, mPaint)

        /*圆形滑块*/
        mPaint.color = if (curX > lineStart + (lineEnd / 2)) mOnColor else mOffColor
        canvas.drawCircle(curX, centerY, radius, mPaint)
        mPaint.xfermode = null
        mPaint.color = mIndicatorColor
        canvas.drawCircle(curX, centerY, radius - Util.dp2px(2F), mPaint)
        canvas.restoreToCount(save)
    }

    /*设置开关状态改变监听器*/
    fun setOnStateChangedListener(o: OnStateChangedListener) {
        this.onStateChangedListener = o
    }

    /*内部接口，开关状态改变监听器*/
    interface OnStateChangedListener {
        fun onStateChanged(state: Boolean)
    }

}