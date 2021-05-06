package com.ls.custom_view_library.verticaltextview;

import java.util.ArrayList;

/**
 * 换行规则
 */
public interface ILineBreakRule {
    /**
     *
     * @param text 要测量的文字
     * @param maxWidth 最大可显示宽度，-1为不限制
     * @param maxHeight 最大可显示高度，-1为不限制
     * @param lineSpace 行间距
     * @param wordSpace 字间距
     * @param fixSize 最大可显示文字 <=0 不限制
     * @param getCharSize 获取一个文字的大小
     * @param lineTexts 分断后的文字容器，不能重新赋值，算是一个回传字段
     * @return
     */
    WidthAndHeight getTextSize(String text, int maxWidth, int maxHeight, float lineSpace, float wordSpace, int fixSize,IGetCharSize getCharSize, ArrayList<LineText> lineTexts,boolean vertical);
}
