package com.ls.custom_view_library

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.LogUtils

/**
 * @ClassName: HeaderZoomRecyclerView
 * @Description:
 * @Author: ls
 * @Date: 2020/3/12 17:59
 */
class HeaderZoomRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    //    用于记录下拉位置
    private var mDown = 0f
    //    zoomView原本的宽高
    private var zoomViewWidth = 0
    private var zoomViewHeight = 0

    //    是否正在放大
    private var mScaling = false

    //    放大的view，默认为第一个子view
    private var zoomView: View? = null

    fun setZoomView(zoomView: View?) {
        this.zoomView = zoomView
    }

    //    滑动放大系数，系数越大，滑动时放大程度越大
    private var mScaleRatio = 0.4f

    fun setScaleRatio(mScaleRatio: Float) {
        this.mScaleRatio = mScaleRatio
    }

    //    最大的放大倍数
    private var mScaleTimes = 2f

    fun setScaleTimes(mScaleTimes: Int) {
        this.mScaleTimes = mScaleTimes.toFloat()
    }

    //    回弹时间系数，系数越小，回弹越快
    private var mReplyRatio = 0.5f

    fun setReplyRatio(mReplyRatio: Float) {
        this.mReplyRatio = mReplyRatio
    }

    fun clearSizeCache() {
        zoomViewWidth = 0
        zoomViewHeight = 0
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            zoomViewWidth = zoomView!!.measuredWidth
            zoomViewHeight = zoomView!!.measuredHeight
        }
        if (zoomView == null || zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            return super.onTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                var firstPosition = -1
                if(layoutManager is LinearLayoutManager){
                    firstPosition = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                }
                if(firstPosition == 0){
                    if (!mScaling) {
                        mDown = ev.y //滑动到顶部时，记录位置
                    }
                    val distance = ((ev.y - mDown) * mScaleRatio).toInt()
                    if (distance >= 0) {//若往上滑动
                        mScaling = true
                        setZoom(distance.toFloat())
                        return true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mScaling = false
                replyView()
            }
        }
        return super.onTouchEvent(ev)
    }

    /**放大view */
    private fun setZoom(s: Float) {
        val scaleTimes = ((zoomViewWidth + s) / (zoomViewWidth * 1.0)).toFloat()
        //        如超过最大放大倍数，直接返回
        if (scaleTimes > mScaleTimes) return
        val layoutParams = zoomView!!.layoutParams
        layoutParams.width = (zoomViewWidth + s).toInt()
        layoutParams.height = (zoomViewHeight * ((zoomViewWidth + s) / zoomViewWidth)).toInt()
        //        设置控件水平居中
        (layoutParams as MarginLayoutParams).setMargins(-(layoutParams.width - zoomViewWidth) / 2, 0, 0, 0)
        zoomView!!.layoutParams = layoutParams
    }

    /**回弹 */
    private fun replyView() {
        val distance = zoomView!!.layoutParams.width - zoomViewWidth.toFloat()
        // 设置动画
        val anim = ObjectAnimator.ofFloat(distance, 0.0f).setDuration(Math.abs(distance * mReplyRatio).toLong())
        anim.addUpdateListener { animation -> setZoom(animation.animatedValue as Float) }
        anim.start()
    }

}