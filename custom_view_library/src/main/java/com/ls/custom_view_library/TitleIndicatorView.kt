package com.ls.custom_view_library

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.ls.comm_util_library.LogUtils
import com.ls.comm_util_library.Util

class TitleIndicatorView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mIndicatorHeight = Util.dp2px(3F).toInt()
    private var mIndicatorBgColor = Color.RED

    private var mTitleContainer = LinearLayout(context).apply {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private var mIndicator = View(context).apply {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,mIndicatorHeight).apply {
            addRule(ALIGN_PARENT_BOTTOM)
        }
    }
    
    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleIndicatorView)
        mIndicatorHeight = array.getDimensionPixelSize(R.styleable.TitleIndicatorView_indicatorHeight,mIndicatorHeight)
        mIndicatorBgColor = array.getColor(R.styleable.TitleIndicatorView_indicatorBgColor,mIndicatorBgColor)
        array.recycle()
        mIndicator.setBackgroundColor(mIndicatorBgColor)
        addView(mTitleContainer)
        addView(mIndicator)
    }

    private var mOnItemClickListener: OnClickListener? = null
    fun setOnItemClickListener(listener: OnClickListener){
        mOnItemClickListener = listener
    }

    private fun setIndicatorAnimator(animator: ValueAnimator) {
        animator.duration = 200
        animator.addUpdateListener { animation ->
            val currValue = animation.animatedValue as Int
            changeIndicator(currValue,0)
        }
        animator.start()
    }

    private fun changeIndicator(leftMargin: Int,width: Int){
        val lp = mIndicator.layoutParams as LayoutParams
        if(width != 0){
            lp.width = width
        }
        lp.leftMargin = leftMargin
        mIndicator.requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val items = ArrayList<View>()
        var item: View
        for (i in 0 until childCount){
            item = getChildAt(i)
            if(item != mTitleContainer && item != mIndicator){
                items.add(item)
            }
        }
        for (i in items){
            removeView(i)
            mTitleContainer.addView(i)
            i.setOnClickListener {
                val mIndicatorLp = mIndicator.layoutParams as LayoutParams
                setIndicatorAnimator(ValueAnimator.ofInt(mIndicatorLp.leftMargin,it.left))
                mOnItemClickListener?.onClick(it)
            }
        }
        val visibleChildCount = mTitleContainer.childCount
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var lp: LinearLayout.LayoutParams
        for (i in 0 until visibleChildCount){
            item = mTitleContainer.getChildAt(i)
            lp = item.layoutParams as LinearLayout.LayoutParams
            lp.height = measuredHeight - mIndicatorHeight
            lp.width = measuredWidth / visibleChildCount

        }
//        mTitleContainer.layoutParams.height = measuredHeight - mIndicatorHeight
        mIndicator.layoutParams.height = mIndicatorHeight
        mIndicator.layoutParams.width = measuredWidth / visibleChildCount
    }
    
}
