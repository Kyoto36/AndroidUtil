package com.ls.custom_view_library.verticaltextview;

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.ls.custom_view_library.R

class VerticalTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mPaint: Paint = TextPaint()
    private var mText: String? = null
    private var mHint: String? = null
    private var mTextColor: Int = Color.BLACK
    private var mHintColor: Int = Color.GRAY
    private var mParentAllocateSize = WidthAndHeight()
    private var mWordSpace = 0F
    private var mLineSpace = 0F
    private var mGravity = GRAVITY_LEFT
    private var mFixSize = 0
    private var mTypeface: Typeface? = null
    private var mIncludePad = true
    private var mIsVertical = true

    private var mLineTexts = ArrayList<LineText>()
    private var mLineBreadRule: ILineBreakRule = DefaultLineBreakRule.instance

    init {
        mPaint.isAntiAlias = true
        mPaint.textAlign = Paint.Align.LEFT
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LSVerticalTextView)
        mPaint.textSize = ta.getDimension(R.styleable.LSVerticalTextView_textSize,60f)
        mTextColor = ta.getColor(R.styleable.LSVerticalTextView_textColor,mTextColor)
        mHintColor = ta.getColor(R.styleable.LSVerticalTextView_hintColor,mHintColor)
        mText = ta.getString(R.styleable.LSVerticalTextView_text)
        mHint = ta.getString(R.styleable.LSVerticalTextView_hint)
        mWordSpace = ta.getDimension(R.styleable.LSVerticalTextView_wordSpace,mWordSpace)
        mLineSpace = ta.getDimension(R.styleable.LSVerticalTextView_lineSpace,mLineSpace)
        mGravity = ta.getInt(R.styleable.LSVerticalTextView_gravity,mGravity)
        mFixSize = ta.getInt(R.styleable.LSVerticalTextView_fixSize,mFixSize)
        mIsVertical = ta.getBoolean(R.styleable.LSVerticalTextView_vertical,mIsVertical)
        mIncludePad = ta.getBoolean(R.styleable.LSVerticalTextView_includeFontPadding,mIncludePad)
        val assetPath = ta.getString(R.styleable.LSVerticalTextView_typeface)
        if(!TextUtils.isEmpty(assetPath)) {
            mTypeface = Typeface.createFromAsset(context.assets, assetPath)
        }
        ta.recycle()
        if(mTypeface != null){
            mPaint.typeface = mTypeface
        }
    }

    fun setText(text: String){
        mText = text
        requestLayout()
    }

    fun getText(): String?{
        return mText
    }

    // 获取行间距
    fun getLineSpace(): Float{
        return mLineSpace
    }

    // 获取字间距
    fun getWordSpace(): Float{
        return mWordSpace
    }

    fun setTextColor(color: Int){
        mTextColor = color
        if(!TextUtils.isEmpty(mText)){// 如果mText不为空，那就立即重新绘制，以显示新的mTextColor
            requestLayout()
        }
    }

    fun setHint(hint: String){
        mHint = hint
        if(TextUtils.isEmpty(mText)){ // 如果mText是空，那就立即重新绘制，以显示新的Hint，否则不用重新绘制，节省性能
            postInvalidate()
        }
    }

    fun setTextSizeSp(sp: Float){
        mPaint.textSize = sp2px(sp)
        if(!TextUtils.isEmpty(mText) || !TextUtils.isEmpty(mHint)){ // 如果mText和mHint其中一个不为空，那就立即重新绘制，以显示新的TextSize
            postInvalidate()
        }
    }

    fun setTextSizePx(px: Float){
        mPaint.textSize = px
        if(!TextUtils.isEmpty(mText) || !TextUtils.isEmpty(mHint)){ // 如果mText和mHint其中一个不为空，那就立即重新绘制，以显示新的TextSize
            postInvalidate()
        }
    }

    fun setHintColor(color: Int){
        mHintColor = color
        if(TextUtils.isEmpty(mText) && !TextUtils.isEmpty(mHint)){ // 如果mText为空，并且mHint不为空，那就立即重新绘制，以显示新的mHintColor
            postInvalidate()
        }
    }

    /**
     * 设置换行规则，不设置使用默认换行规则
     * @param lineBreadRule ILineBreakRule
     */
    fun setLineBreakRule(lineBreadRule: ILineBreakRule){
        mLineBreadRule = lineBreadRule
    }

    /**
     * 设置字体
     * @param typeface Typeface?
     */
    fun setTypeface(typeface: Typeface?){
        mTypeface = typeface
        if(mTypeface != null){
            mPaint.typeface = mTypeface
            postInvalidate()
        }
    }

    /**
     * 获取画笔
     * @return Paint
     */
    fun getPaint(): Paint{
        return mPaint
    }

    /**
     * 是否包含字体自带内边距
     * @param includepad Boolean
     */
    fun setIncludeFontPadding(includepad : Boolean){
        mIncludePad = includepad
        postInvalidate()
    }

    /**
     *
     * 获取固定的文字个数
     * @return Int
     */
    fun getFixSize():Int{
        return mFixSize
    }

    /**
     * 指定文字是否能完全显示出来
     * 推荐在ui测量完成之后使用,并且在view宽高是warp的情况下算出来的数没用
     * @param text String
     * @return Boolean
     */
    fun canFillFull(text: String): Boolean{
        if(mFixSize > 0){
            return text.length <= mFixSize
        }
        if(mParentAllocateSize.width <= 0 && mParentAllocateSize.height <= 0){
            requestLayout()
        }
        if(mParentAllocateSize.width <= 0 || mParentAllocateSize.height <= 0){
            return false
        }
        if(mIsVertical) {
            // 宽度不限定，高度限定为父容器提供的最大高度减去上下边距
            val realWidthAndHeight = getTextSize(text, -1, (mParentAllocateSize.height - paddingTop - paddingBottom).toInt() + 2)
            // 如果文字的显示宽度大于父容器提供的最大宽度减去左右边距 return false
            return realWidthAndHeight.width + paddingLeft + paddingRight <= mParentAllocateSize.width
        }
        else{
            // 高度不限定，宽度限定为父容器提供的最大宽度减去左右边距
            val realWidthAndHeight = getTextSize(text, (mParentAllocateSize.width - paddingLeft - paddingRight).toInt() + 2, -1)
            // 如果文字的显示高度大于父容器提供的最大高度减去上下边距 return false
            return realWidthAndHeight.height + paddingTop + paddingBottom <= mParentAllocateSize.height
        }
    }

    /**
     * 预测能显示多少个文字（中文）
     * 推荐在ui测量完成之后使用,并且在view宽高是warp的情况下算出来的数没用
     * @return Int
     */
    fun getPreTextLength(): Int{
        if(mParentAllocateSize.width <= 0 && mParentAllocateSize.height <= 0){
            requestLayout()
        }
        if(mParentAllocateSize.width <= 0 || mParentAllocateSize.height <= 0){
            return 0
        }
        val tempWidthAndHeight = getTempCharWidthAndHeight()
        // 每竖行（也就是列）能显示的个数 = （父容器提供的最大宽度 + 一个行间距（最右边不需要行间距） - 上下边距） / （单个字的宽度 + 行间距）
        val columnWords = ((mParentAllocateSize.width + mLineSpace - paddingLeft - paddingRight) / (tempWidthAndHeight.width + mLineSpace)).toInt()
        // 每横排（也就是行）能显示的个数 = （父容器提供的最大高度 + 一个字间距（最下边不需要字间距） - 左右边距） / （单个字的高度 + 字间距）
        val rowWords = ((mParentAllocateSize.height + mWordSpace - paddingTop - paddingBottom) / (tempWidthAndHeight.height + mWordSpace)).toInt()
        return columnWords * rowWords
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mParentAllocateSize.setWidthAndHeight(widthSize.toFloat(),heightSize.toFloat())

        val textSize = getTextSize(
            if(TextUtils.isEmpty(mText)) mHint else mText,
            if(widthMode == MeasureSpec.UNSPECIFIED) -1 else widthSize - paddingLeft - paddingRight, // UNSPECIFIED不限定；限定最大宽度不能超过父容器提供的宽度减去左右内边距
            if(heightMode == MeasureSpec.UNSPECIFIED) -1 else heightSize - paddingTop - paddingBottom // UNSPECIFIED不限定；限定最大高度不能超过父容器提供的高度减去上下内边距
        )
        widthSize = if(widthMode == MeasureSpec.EXACTLY) {
            Log.d("VerticalTextView","宽度固定 width $widthSize")
            widthSize
        } else {
            Log.d("VerticalTextView","宽度不固定 width ${textSize.width}")
            (if(textSize.width <= 0) 0 else textSize.width.toInt() + 2) + paddingLeft + paddingRight // 测量宽度 <=0，就用0，否者用测量宽度 +2
        }
        heightSize = if(heightMode == MeasureSpec.EXACTLY){
            Log.d("VerticalTextView","高度固定 height $heightSize")
            heightSize
        }
        else{
            Log.d("VerticalTextView","高度不固定 height ${textSize.height}")
            (if(textSize.height <= 0) 0 else textSize.height.toInt() + 2) + paddingTop + paddingBottom // 测量高度 <=0，就用0，否者用测量高度 +2
        }
        setMeasuredDimension(widthSize,heightSize) // +2是因为float转int的时候小数会抹掉，所以给点冗余空间
    }

    private fun getTextSize(text: String?,maxWidth: Int,maxHeight: Int): WidthAndHeight{
        return mLineBreadRule.getTextSize(text,maxWidth,maxHeight,mLineSpace,mWordSpace,mFixSize, IGetCharSize { getCharWidthAndHeight(it) },mLineTexts,mIsVertical)
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canvas == null){
            return
        }
        if(!TextUtils.isEmpty(mText)){
            mPaint.color = mTextColor
            drawText(canvas,mPaint)
            return
        }
        if(!TextUtils.isEmpty(mHint)){
            mPaint.color = mHintColor
            drawText(canvas,mPaint)
            return
        }
    }

    private fun drawText(canvas: Canvas,paint: Paint){
        Log.d("VerticalTextView","draw measure $measuredWidth $measuredHeight")
        Log.d(Companion.TAG, "drawText: mLineTexts = $mLineTexts")
        if(mIsVertical) drawTextFormVertical(canvas, paint)
        else drawTextFormHorizontal(canvas, paint)
    }

    private fun drawTextFormVertical(canvas: Canvas,paint: Paint){
        var startX = if((mGravity and GRAVITY_RIGHT) == GRAVITY_RIGHT) measuredWidth - paddingRight.toFloat() else paddingLeft.toFloat()
        var startY = if((mGravity and GRAVITY_BOTTOM) == GRAVITY_BOTTOM) measuredHeight - paddingBottom.toFloat() else paddingTop.toFloat()
        var charWidthAndHeight: WidthAndHeight
        var baseLine : Float
        var lineText: String
        for (line in  mLineTexts){
            if(line == null || line.width <= 0){
                continue
            }
            if((mGravity and GRAVITY_RIGHT) == GRAVITY_RIGHT){
                // 从右至左需要在本行开始是用上一行的开始位置减去本行宽度
                startX  -= line.width
            }
            lineText = line.text
            if((mGravity and GRAVITY_BOTTOM) == GRAVITY_BOTTOM){
                lineText = line.reverse()
            }
            for (char in lineText){
                charWidthAndHeight = getCharWidthAndHeight(char)
                if((mGravity and GRAVITY_BOTTOM) == GRAVITY_BOTTOM){
                    startY -= charWidthAndHeight.height
                }
                // 获取每个字的基线，如果去掉字体自带边距就是用descent 否则用bottom 配合 getCharWidthAndHeight
                baseLine = charWidthAndHeight.height - (if(!mIncludePad) paint.fontMetrics.descent else paint.fontMetrics.bottom)
                canvas.drawText(char.toString(), startX, startY + baseLine, paint)
                if((mGravity and GRAVITY_BOTTOM) == GRAVITY_BOTTOM){
                    startY -= mWordSpace
                }
                else {
                    startY += (charWidthAndHeight.height + mWordSpace) // 绘制完之后下一个字的开始位置就是现在的开始位置加上现在的字的高度和字间距
                }
            }
            if((mGravity and GRAVITY_RIGHT) == GRAVITY_RIGHT) {
                // 从右至左排版，下一行的开始位置 - 行间距 - 下一行的宽度 （因为在当前行获取不到下一行的宽度，就只减一个行间距，下一行的宽度在下一行的循环开始获取）
                startX -= mLineSpace
            }
            else{
                // 从左至右排版，下一行的开始位置在 本行开始位置 + 本行宽度 + 行间距
                startX += line.width + mLineSpace
            }
            startY = if((mGravity and GRAVITY_BOTTOM) == GRAVITY_BOTTOM) measuredHeight - paddingBottom.toFloat() else paddingTop.toFloat()
        }
    }

    private fun drawTextFormHorizontal(canvas: Canvas,paint: Paint){
        var startX: Float
        var startY = paddingTop.toFloat()
        var charWidthAndHeight: WidthAndHeight
        var baseLine : Float
        var lineText: String
        for (line in  mLineTexts){
            if(line == null || line.height <= 0){
                continue
            }
            lineText = line.text
            if((mGravity and GRAVITY_CENTER) == GRAVITY_CENTER || (mGravity and GRAVITY_CENTER_HORIZONTAL) == GRAVITY_CENTER_HORIZONTAL){
                val diffSpace = ((measuredWidth - paddingLeft - paddingRight) - line.width)
                startX = paddingLeft + (diffSpace / 2)
            }
            else if((mGravity and GRAVITY_RIGHT) == GRAVITY_RIGHT){
                startX = measuredWidth - paddingRight.toFloat()
                lineText = line.reverse()
            }
            else{
                startX = paddingLeft.toFloat()
            }
            for (char in lineText){
                charWidthAndHeight = getCharWidthAndHeight(char)
                if((mGravity and GRAVITY_RIGHT) == GRAVITY_RIGHT && !((mGravity and GRAVITY_CENTER) == GRAVITY_CENTER || (mGravity and GRAVITY_CENTER_HORIZONTAL) == GRAVITY_CENTER_HORIZONTAL)) {
                    startX -= charWidthAndHeight.width
                }
                // 获取每个字的基线，如果去掉字体自带边距就是用descent 否则用bottom 配合 getCharWidthAndHeight
                baseLine = charWidthAndHeight.height - (if(!mIncludePad) paint.fontMetrics.descent else paint.fontMetrics.bottom)
                canvas.drawText(char.toString(), startX, startY + baseLine, paint)
                if((mGravity and GRAVITY_RIGHT) == GRAVITY_RIGHT && !((mGravity and GRAVITY_CENTER) == GRAVITY_CENTER || (mGravity and GRAVITY_CENTER_HORIZONTAL) == GRAVITY_CENTER_HORIZONTAL)) {
                    startX -= mWordSpace // 绘制完之后，下一个字的开始位置就是现在的开始位置减去字间距
                }
                else{
                    startX += (charWidthAndHeight.width + mWordSpace) // 绘制完之后，下一个字的开始位置就是现在的开始位置加上现在的字的宽度和字间距
                }
            }
            startY += line.height + mLineSpace
        }
    }

    fun getTempCharWidthAndHeight(): WidthAndHeight{
        return getCharWidthAndHeight('正')
    }

    private fun getCharWidthAndHeight(char: Char): WidthAndHeight{
        // 如果去掉字体自带内边距，使用 descent - ascent 否则使用 bottom - top
        val height = if(!mIncludePad) mPaint.fontMetrics.descent - mPaint.fontMetrics.ascent else mPaint.fontMetrics.bottom - mPaint.fontMetrics.top
        val widths = FloatArray(1)
        mPaint.getTextWidths(char.toString(),widths)
        val width = widths[0]
        return WidthAndHeight(width, height)
    }

    private fun sp2px(sp: Float): Float{
        return (sp * Resources.getSystem().displayMetrics.scaledDensity)
    }

    companion object {
        private const val TAG = "VerticalTextView"
        private const val GRAVITY_LEFT = 0x00000001
        private const val GRAVITY_RIGHT = 0x00000002
        private const val GRAVITY_BOTTOM = 0x00000004
        private const val GRAVITY_TOP = 0x00000008
        private const val GRAVITY_CENTER = 0x00000010
        private const val GRAVITY_CENTER_VERTICAL = 0x00000020
        private const val GRAVITY_CENTER_HORIZONTAL = 0x00000040
    }
}