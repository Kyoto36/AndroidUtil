package com.ls.custom_view_library.verticaltextview

import android.text.TextUtils
import android.util.Log
import java.util.ArrayList

class DefaultLineBreakRule: ILineBreakRule {
    override fun getTextSize(text: String?, maxWidth: Int, maxHeight: Int, lineSpace: Float, wordSpace: Float, fixSize: Int, getCharSize: IGetCharSize, lineTexts: ArrayList<LineText>,vertical: Boolean): WidthAndHeight {
        if(maxHeight == 0 || maxWidth == 0 || TextUtils.isEmpty(text)){
            return WidthAndHeight(0F,0F)
        }
        if(vertical) return getTextSizeFormVertical(text, maxWidth, maxHeight, lineSpace, wordSpace, fixSize, getCharSize, lineTexts)
        else return getTextSizeFormHorizontal(text, maxWidth, maxHeight, lineSpace, wordSpace, fixSize, getCharSize, lineTexts)
    }

    private fun getTextSizeFormVertical(text: String?, maxWidth: Int, maxHeight: Int, lineSpace: Float, wordSpace: Float, fixSize: Int, getCharSize: IGetCharSize, lineTexts: ArrayList<LineText>): WidthAndHeight{
        var widthSize = 0F
        var heightSize = 0F
        var currWidth = 0F
        var currHeight = 0F
        var charWidthAndHeight: WidthAndHeight
        val tempWidthAndHeight = getCharSize.getCharSize('正')
        var char: Char
        var charMaxWidth = tempWidthAndHeight.width
        var lineNum = 0
        var line = if(lineTexts.size <= lineNum)  {
            val temp = LineText()
            lineTexts.add(temp)
            temp
        } else lineTexts[lineNum].clear()
        for (i in text!!.indices) {
            char = text[i]
            charWidthAndHeight = getCharSize.getCharSize(char)
            if(currHeight == 0F && maxHeight > 0 && charWidthAndHeight.height > maxHeight){ //如果每列第一个字的高度都大雨最大限定高度，那么就不用继续计算了，继续计算只会产生死循环
                break
            }
            if(maxWidth > 0 && currWidth > maxWidth){ // 如果宽度不够显示当前列，那就也不用计算了，节约性能
                charMaxWidth = 0F
                break
            }
            if(fixSize in 1 .. i){
                break
            }
            charMaxWidth = Math.max(charMaxWidth,charWidthAndHeight.width) // 本行字中宽度最大的
            if(char == '\n' || (maxHeight > 0 && (currHeight + charWidthAndHeight.height) > maxHeight)){ // 如果当前字符是换行符或者剩余高度已经不能显示下当前字符，那就换列
                // 如果最大高度小于0，说明测量模式是MeasureSpec.UNSPECIFIED，那就不用在意最大限定高度了，直接取历史最大高度和当前列的高度的最大值，
                // 否则比完历史高度与当前列的高度最大值，然后再拿最大值与最大限定高度比最小值
                heightSize = if(maxHeight < 0) Math.max(heightSize,currHeight) else Math.min(Math.max(heightSize,currHeight), maxHeight.toFloat())
                currWidth += charMaxWidth + lineSpace // 加上行间距
                line.setWidthAndHeight(charMaxWidth,if(currHeight > wordSpace) currHeight - wordSpace else currHeight)
                lineNum++
                line = if(lineTexts.size <= lineNum)  {
                    val temp = LineText()
                    lineTexts.add(temp)
                    temp
                } else lineTexts[lineNum].clear()
                currHeight = 0F // 换行之后重置高度
                if(char == '\n') { // 如果当前字符是换行符，那就不用统计其高度
                    continue
                }
            }
            line.addChar(char)
            currHeight += charWidthAndHeight.height + wordSpace // 加上字间距
        }
        // 最后一行也要统计在其中
        heightSize = if(maxHeight < 0) Math.max(heightSize,currHeight) else Math.min(Math.max(heightSize,currHeight), maxHeight.toFloat())
        currWidth += charMaxWidth
        line.setWidthAndHeight(charMaxWidth,if(currHeight > wordSpace) currHeight - wordSpace else currHeight)
        if(lineTexts.size > lineNum + 1){
            lineTexts.removeAll(lineTexts.subList(lineNum + 1,lineTexts.size))
        }
        // 如果最大宽度小于0，说明测量模式是MeasureSpec.UNSPECIFIED，那就不用在意最大限定宽度了，直接取测量的宽度
        // 否则那测量宽度和最大限定宽度取最小值
        widthSize = if(maxWidth < 0) currWidth else Math.min(currWidth,maxWidth.toFloat())
        Log.d("VerticalTextView","maxWidth $maxWidth currWidth $currWidth maxHeight $maxHeight heightSize $heightSize")
        return WidthAndHeight(widthSize, heightSize)
    }

    private fun getTextSizeFormHorizontal(text: String?, maxWidth: Int, maxHeight: Int, lineSpace: Float, wordSpace: Float, fixSize: Int, getCharSize: IGetCharSize, lineTexts: ArrayList<LineText>): WidthAndHeight{
        var widthSize = 0F
        var heightSize = 0F
        var currWidth = 0F
        var currHeight = 0F
        var charWidthAndHeight: WidthAndHeight
        val tempWidthAndHeight = getCharSize.getCharSize('正')
        var char: Char
        var charMaxHeight = tempWidthAndHeight.height
        var lineNum = 0
        var line = if(lineTexts.size <= lineNum)  {
            val temp = LineText()
            lineTexts.add(temp)
            temp
        } else lineTexts[lineNum].clear()
        for (i in text!!.indices) {
            char = text[i]
            charWidthAndHeight = getCharSize.getCharSize(char)
            if(currWidth == 0F && maxWidth > 0 && charWidthAndHeight.width > maxWidth){ //如果每列第一个字的宽度都大雨最大限定宽度，那么就不用继续计算了，继续计算只会产生死循环
                break
            }
            if(maxHeight > 0 && currHeight > maxHeight){ // 如果高度不够显示当前行，那就也不用计算了，节约性能
                charMaxHeight = 0F
                break
            }
            if(fixSize in 1 .. i){
                break
            }
            charMaxHeight = Math.max(charMaxHeight,charWidthAndHeight.height) // 本行字中高度最大的
            if(char == '\n' || (maxWidth > 0 && (currWidth + charWidthAndHeight.width) > maxWidth)){ // 如果当前字符是换行符或者剩余宽度已经不能显示下当前字符，那就换列
                // 如果最大宽度小于0，说明测量模式是MeasureSpec.UNSPECIFIED，那就不用在意最大限定宽度了，直接取历史最大宽度和当前列的宽度的最大值，
                // 否则比完历史宽度与当前列的宽度最大值，然后再拿最大值与最大限定宽度比最小值
                widthSize = if(maxWidth < 0) Math.max(widthSize,currWidth) else Math.min(Math.max(widthSize,currWidth), maxWidth.toFloat())
                currHeight += charMaxHeight + lineSpace // 加上行间距
                line.setWidthAndHeight(if(currWidth > wordSpace) currWidth - wordSpace else currWidth,charMaxHeight)
                lineNum++
                line = if(lineTexts.size <= lineNum)  {
                    val temp = LineText()
                    lineTexts.add(temp)
                    temp
                } else lineTexts[lineNum].clear()
                currWidth = 0F  // 换行之后重置宽度
                if(char == '\n') { // 如果当前字符是换行符，那就不用统计其高度
                    continue
                }
            }
            line.addChar(char)
            currWidth += charWidthAndHeight.width + wordSpace // 加上字间距
        }
        // 最后一行也要统计在其中
        widthSize = if(maxWidth < 0) Math.max(widthSize,currWidth) else Math.min(Math.max(widthSize,currWidth), maxWidth.toFloat())
        currHeight += charMaxHeight
        line.setWidthAndHeight(if(currWidth > wordSpace) currWidth - wordSpace else currWidth,charMaxHeight)
        if(lineTexts.size > lineNum + 1){
            lineTexts.removeAll(lineTexts.subList(lineNum + 1,lineTexts.size))
        }
        // 如果最大高度小于0，说明测量模式是MeasureSpec.UNSPECIFIED，那就不用在意最大限定高度了，直接取测量的高度
        // 否则那测量高度和最大限定高度取最小值
        heightSize = if(maxHeight < 0) currHeight else Math.min(currHeight,maxHeight.toFloat())
        Log.d("VerticalTextView","maxWidth $maxWidth currWidth $currWidth maxHeight $maxHeight heightSize $heightSize")
        return WidthAndHeight(widthSize, heightSize)
    }

    companion object{
        val instance: DefaultLineBreakRule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){DefaultLineBreakRule()}
    }

}