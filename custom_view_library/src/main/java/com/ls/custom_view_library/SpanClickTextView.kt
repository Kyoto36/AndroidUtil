package com.ls.custom_view_library

import android.content.Context
import android.graphics.Color
import android.system.Os.link
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import com.ls.comm_util_library.LogUtils
import com.ls.comm_util_library.PreBackgroundColorSpan


/**
 * @ClassName: SpanClickTextView
 * @Description:解决 setMovementMethod 和 Ellipsize 不能同时存在的bug
 * 以及实现微博部分文字点击效果，使用PreBackgroundColorSpan
 * @Author: ls
 * @Date: 2020/3/4 11:41
 */
class SpanClickTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mBackgroundColorSpan: BackgroundColorSpan? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event!!.action
        val off: Int
        val text = text
        if(action == MotionEvent.ACTION_DOWN){
            off = calcOff(text,event)
            if(off != -1){
                val spans = getSpans(text, off, PreBackgroundColorSpan::class.java)
                if(spans?.isNotEmpty() == true && spans[0] != null){
                    removeSpan(text,mBackgroundColorSpan)
                    mBackgroundColorSpan = BackgroundColorSpan(spans[0]!!.color)
                    setSpan(text,mBackgroundColorSpan!!,spans[0]!!.start,spans[0]!!.end)
                }
            }
        }
        else if (action == MotionEvent.ACTION_UP) {
            removeSpan(text,mBackgroundColorSpan)
            off = calcOff(text,event)
            if(off != -1) {
                val spans = getSpans(text, off, ClickableSpan::class.java)
                if (spans?.isNotEmpty() == true) {
                    spans[0]?.onClick(this)
                    return true
                }
            }
        }
        else if(action == MotionEvent.ACTION_CANCEL){
            removeSpan(text,mBackgroundColorSpan)
        }
        return super.onTouchEvent(event)
    }

    private fun calcOff(text: CharSequence,event: MotionEvent): Int {
        var off = -1
        if (text is SpannableStringBuilder || text is SpannedString
                || text is SpannableString) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= totalPaddingLeft
            y -= totalPaddingTop
            x += scrollX
            y += scrollY
            val line = layout.getLineForVertical(y)
            off = layout.getOffsetForHorizontal(line, x.toFloat())
        }
        return off
    }

    private fun <T> getSpans(text: CharSequence, off: Int,kind: Class<T>): Array<T?>? {
        return when (text) {
            is SpannableStringBuilder -> {
                text.getSpans(off, off, kind)
            }
            is SpannableString -> {
                text.getSpans(off, off, kind)
            }
            else -> {
                (text as SpannedString).getSpans(off, off, kind)
            }
        }
    }

    private fun setSpan(text: CharSequence,obj: Any?,start: Int,end: Int){
        if(obj != null) {
            val temp = text.toSpannable()
            temp.setSpan(obj, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setText(temp)
        }
    }

    private fun removeSpan(text: CharSequence,obj: Any?){
        if(obj != null){
            val temp = text.toSpannable()
            temp.removeSpan(obj)
            setText(temp)
        }
    }
}