package com.ls.test.testutils

import android.content.Context
import android.text.*
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import com.ls.comm_util_library.ColorUtils
import com.ls.comm_util_library.ISingleListener

/**
 * @ClassName: CustomTextView
 * @Description: 在不使用MovementMethod的时候，这个的TextClickSpan也会起作用
 *               在一个view中需要点击不同的字符背景色也不同时可以使用
 *               在不能聚焦的情况下，统一高亮色失效时可以使用
 * @Author: ls
 * @Date: 2020/9/29 13:39
 */
class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var mBackgroundColorSpan: BackgroundColorSpan? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val superResult = super.onTouchEvent(event)
        val action = event.action
        val off: Int
        val text = text.toSpannable()
        if (action == MotionEvent.ACTION_DOWN) {
            off = calcOff(text, event)
            if (off != -1) {
                val spans = text.getSpans(off, off, TextClickSpan::class.java)
                if (spans?.isNotEmpty() == true && spans[0] != null) {
                    removeSpan(text, mBackgroundColorSpan)
                    val click = spans[0]!!
                    if (click.getColor() != -1) {
                        val bgColor = ColorUtils.changeAlpha(spans[0]!!.getColor(),0.5F)
                        mBackgroundColorSpan = BackgroundColorSpan(bgColor)
                        setSpan(text, mBackgroundColorSpan!!, text.getSpanStart(click), text.getSpanEnd(click))
                    }
                }
            }
            return true
        } else if (action == MotionEvent.ACTION_UP) {
            removeSpan(text, mBackgroundColorSpan)
            off = calcOff(text, event)
            if (off != -1) {
                val spans = text.getSpans(off, off, TextClickSpan::class.java)
                if (spans?.isNotEmpty() == true) {
                    spans[0]?.onClick()
                }
            }
            return true
        } else if (action == MotionEvent.ACTION_CANCEL) {
            removeSpan(text, mBackgroundColorSpan)
            return true
        }
        return superResult
    }

    private fun calcOff(text: Spannable?, event: MotionEvent): Int {
        var off = -1
        if(text != null) {
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

    private fun setSpan(text: Spannable?, obj: Any?, start: Int, end: Int) {
        if (text != null && obj != null) {
            text.setSpan(obj, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setText(text)
        }
    }

    private fun removeSpan(text: Spannable?, obj: Any?) {
        if (text != null && obj != null) {
            text.removeSpan(obj)
            setText(text)
        }
    }

    class TextClickSpan : CharacterStyle {
        private var mColor = -1
        private var mUnderline = true
        private val mText: CharSequence
        private val mOnClickListener: ISingleListener<CharSequence>?

        constructor(
            text: CharSequence,
            onClickListener: ISingleListener<CharSequence>?
        ) : this(text, -1, true, onClickListener)

        constructor(
            text: CharSequence,
            color: Int,
            onClickListener: ISingleListener<CharSequence>?
        ) : this(text, color, true, onClickListener)

        constructor(
            text: CharSequence,
            color: Int,
            underline: Boolean,
            onClickListener: ISingleListener<CharSequence>?
        ) : super() {
            this.mColor = color
            this.mUnderline = underline
            this.mText = text
            this.mOnClickListener = onClickListener
        }

        fun getColor(): Int {
            return mColor
        }

        fun onClick() {
            mOnClickListener?.onValue(mText)
        }

        override fun updateDrawState(tp: TextPaint) {
            if (mColor != -1) {
                //设置颜色
                tp.color = mColor
            }
            //设置是否要下划线
            tp.isUnderlineText = mUnderline
        }
    }
}