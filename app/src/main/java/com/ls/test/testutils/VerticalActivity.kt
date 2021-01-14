package com.ls.test.testutils

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ls.comm_util_library.toast
import com.ls.custom_view_library.advancedtextview.VerticalTextView
import kotlinx.android.synthetic.main.activity_vertical.*

class VerticalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical)
        textArea.post {
            val aa = format(txt,textArea);
            Log.d("VerticalTextView","aa = " + aa);
            textArea.setText(aa)
            textArea.requestLayout()
        }
    }

    private fun generateTextView(txt: String): TextView {
        val text = TextView(this)
        text.maxEms = 1
        text.text = txt
        return text
    }

    var txt = """
        测试
        这是一段测试文字。主要是为了测试直排版文本框的显示效果。为了能更好的体验感受。。。。我特意增加了比较接。。。。。。。近书法的字体和颜色，如果有什么改进的建议请发邮件到我的邮箱吧。
        竖直排版的TextView需要配合HorizontalScrollView使用才能有更佳的效果。当然，如果你有时间的话，也可以给这个类加加上滚动的功能。
        """.trimIndent()

    fun format(txt: String,textView: VerticalTextView): String? {
        val textWidth = textView.measuredWidth - textView.paddingLeft - textView.paddingRight
        val textHeight = textView.measuredHeight - textView.paddingTop - textView.paddingBottom
        val and = '&'
        val sb = StringBuilder()
        val temp = txt.replace("[\\p{P}]".toRegex(), and.toString() + "")
        val chars = temp.toCharArray()
        var lineStart = 0
        var symbolNumber = 0
        var item: Char
        var end = false
        var lineHeight = 0F
        var charHeight = 0F
        val textSize = textView.textSize
        var lineNumbers = 0F
        var totalWidth = 0F
        while (!end && lineStart < chars.size - 1) {
            for (i in lineStart until chars.size) {
                item = chars[i]
                charHeight = textView.getCharHeight(java.lang.String.valueOf(item))
                charHeight = (if(charHeight > textSize) charHeight else textSize) + textView.charSpacingExtra
                lineHeight += charHeight
                if (item == and) {
                    symbolNumber++
                }
                if (i >= chars.size - 1) {
                    sb.append(txt, lineStart, chars.size)
                    lineNumbers++
                    end = true
                } else if (item == '\n') {
                    symbolNumber = 0
                    sb.append(txt, lineStart, i + 1)
                    lineStart = i + 1
                    lineHeight = 0F
                    lineNumbers++
                    break
                } else if (lineHeight > textHeight && i - 1 > lineStart) {
                    symbolNumber = 0
                    var tempStart = i - 1
                    var k: Int
                    for (j in 1..4) {
                        k = i - j
                        if (k < 0) {
                            break
                        }
                        if (chars[k] != and) {
                            if (k > lineStart) {
                                tempStart = k
                            }
                            break
                        }
                    }
                    sb.append(txt, lineStart, tempStart).append("\n")
                    lineStart = tempStart
                    lineHeight = 0F
                    lineNumbers++
                    break
                } else if ((symbolNumber >= 2 && item != and)) {
                    if (i - lineStart > symbolNumber) {
                        sb.append(txt, lineStart, i).append("\n")
                        lineStart = i
                        lineHeight = 0F
                        break
                    }
                    symbolNumber = 0
                    lineNumbers++
                }
            }
        }
        totalWidth = (lineNumbers * textSize) + ((lineNumbers - 1) * textView.lineSpacingExtra)
//        ((textLines + 1) * getTextSize() + lineSpacingExtra * (textLines - 1));
        if(totalWidth > textWidth){
            toast("文字过多！！" + totalWidth + " " + textWidth + " " + textView.lineSpacingExtra)
        }
        return sb.toString()
    }
}