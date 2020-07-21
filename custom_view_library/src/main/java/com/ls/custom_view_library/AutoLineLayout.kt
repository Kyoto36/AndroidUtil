package com.ls.custom_view_library

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginTop
import com.ls.comm_util_library.LogUtils
import com.ls.custom_view_library.R

/**
 * @ClassName: AutoLineLayout
 * @Description:
 * @Author: ls
 * @Date: 2020/1/20 11:27
 */
class AutoLineLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mMaxLines = Int.MAX_VALUE

    private var mVisibilityViews: MutableList<MutableList<View>>? = null

    init {
        orientation = HORIZONTAL
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AutoLineLayout)
        mMaxLines = ta.getInt(R.styleable.AutoLineLayout_maxLines,Int.MAX_VALUE)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode =  MeasureSpec.getMode(widthMeasureSpec)
        val heightMode =  MeasureSpec.getMode(heightMeasureSpec)
        val widthSize =  MeasureSpec.getSize(widthMeasureSpec)
        val heightSize =  MeasureSpec.getSize(heightMeasureSpec)
        var childView: View
        var childLayoutParams : MarginLayoutParams
        var childWidth = 0
        var childHeight = 0
        var lineWidth = paddingLeft
        var selfWidth = 0
        var selfHeight = 0
        var lineCount = 0
        if(mVisibilityViews == null){
            mVisibilityViews = ArrayList()
        }
        mVisibilityViews!!.clear()
        var lineViews = ArrayList<View>()
        for (i in 0 until childCount){
            childView = getChildAt(i)
            measureChild(childView,widthMeasureSpec,widthMeasureSpec)
            childLayoutParams = childView.layoutParams as MarginLayoutParams
            childWidth = childView.measuredWidth + childLayoutParams.leftMargin + childLayoutParams.rightMargin
            childHeight = childView.measuredHeight + childLayoutParams.topMargin + childLayoutParams.bottomMargin
            if(lineWidth + childWidth > widthSize){ // 如果当前view的宽度加上之间的累计宽度大于最大宽度，则新起一行
                if(lineCount >= mMaxLines - 1){ // 如果行数大于等于最大行数 -1，则终止测量（-1是因为最后一行需要在后面统一加上）
                    childLayoutParams.width = widthSize - lineWidth - childLayoutParams.leftMargin - childLayoutParams.rightMargin - paddingRight
                    // 修改了子view的宽度，重新测量，免得layout的时候出现子view的内容显示不全
                    measureChild(childView,widthMeasureSpec,widthMeasureSpec)
                    lineViews.add(childView)
                    selfWidth = widthSize
                    break
                }
                selfWidth = Math.max(selfWidth,lineWidth) // 测量到目前为止，之前行数中的最大宽度和当前行累计宽度相比，取最大值
                lineWidth =  paddingLeft // 另起一行，累计宽度清零
                if(lineViews.isNotEmpty()){ // 当前行在本view之前以及追加过view，就将当前行加入可显示集合中
                    mVisibilityViews!!.add(lineViews)
                }
                lineViews = ArrayList<View>() // 以当前view为起点新开一行
                lineViews.add(childView)
                selfHeight += childHeight
                lineWidth += childWidth
                lineCount++
            }
            else{
                lineViews.add(childView) // 累计行宽加上本view宽度，未超过widthSize，继续累加
                lineWidth += childWidth
            }
        }
        if(lineViews.isNotEmpty()){ // 最后一行未计入可显示集合，两种情况：
                                    // 一、计算完最后一个元素之后未占满最后一行，则未计入集合；
                                    // 二、新起一行时，发现超过了最大行数，最后一行则未计入集合
            selfWidth = Math.min(selfWidth,lineWidth)
            selfHeight += childHeight
            mVisibilityViews!!.add(lineViews)
        }

        setMeasuredDimension(if (widthMode ==  MeasureSpec.EXACTLY) widthSize else selfWidth,
            if (heightMode ==  MeasureSpec.EXACTLY) heightSize else selfHeight + paddingBottom + paddingTop)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        super.onLayout(changed, l, t, r, b)
        var startX = paddingLeft
        var startY = paddingRight
        var maxLineHeight = 0
        var childRight = 0
        var childBottom = 0
        var childLayoutParams: MarginLayoutParams
        for (lineViews in mVisibilityViews!!){
            for (view in lineViews){
                childLayoutParams = view.layoutParams as MarginLayoutParams
                maxLineHeight = Math.max(maxLineHeight,childLayoutParams.topMargin + view.measuredHeight+ childLayoutParams.bottomMargin)
                childRight = startX + childLayoutParams.leftMargin + view.measuredWidth
                childBottom = startY + childLayoutParams.topMargin + view.measuredHeight
                view.layout(startX + childLayoutParams.leftMargin,startY + childLayoutParams.topMargin,childRight,childBottom)
                startX += childLayoutParams.leftMargin + view.measuredWidth + childLayoutParams.rightMargin
            }
            startX = paddingRight
            startY += maxLineHeight
            maxLineHeight = 0
        }
    }
}