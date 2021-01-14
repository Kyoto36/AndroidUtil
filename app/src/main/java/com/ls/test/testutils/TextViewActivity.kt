package com.ls.test.testutils

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.ISingleListener
import com.ls.comm_util_library.toast
import kotlinx.android.synthetic.main.activity_text_view.*
import kotlinx.android.synthetic.main.item_text.view.*

class TextViewActivity : AppCompatActivity() {

    private val txt = """测试
这是一段测试文字，主要是为了测试竖直排版TextView的显示效果。为了能更好的体验感受，我特意增加了比较接近书法的字体和颜色，如果有什么改进的建议请发邮件到我的邮箱吧。
竖直排版的TextView需要配合HorizontalScrollView使用才能有更佳的效果。当然，如果你有时间的话，也可以给这个类加上滚动的功能。
 测试
这是一段测试文字，主要是为了测试竖直排版TextView的显示效果。为了能更好的体验感受，我特意增加了比较接近书法的字体和颜色，如果有什么改进的建议请发邮件到我的邮箱吧。
竖直排版的TextView需要配合HorizontalScrollView使用才能有更佳的效果。当然，如果你有时间的话，也可以给这个类加上滚动的功能。"""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view)

        val name = SpannableString("fhjdka")
//        name.setSpan(CustomTextView.TextClickSpan(name, Color.BLUE, ISingleListener {
//            toast(name.toString())
//        }),0,name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        name.setSpan(IconTextSpan(this,R.color.colorPrimaryDark,name.toString()),0,name.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.post {
            val width = textView.measuredWidth - textView.paddingLeft - textView.paddingRight;
            val height = textView.measuredHeight - textView.paddingTop - textView.paddingBottom
        }


        val sb = SpannableStringBuilder()
        sb.append(name)
        sb.append("fasd1f2as1f32ds1f2asd3f132asdf132asd1fdsf1s")
        fixText.text = sb
        textList.adapter = Adapter()
        textList.layoutManager = LinearLayoutManager(this)
    }

    class Adapter(): RecyclerView.Adapter<Adapter.ViewHolder>() {
        inner class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text,parent,false)) {
            val itemText = itemView.itemText
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return 30
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val sb = SpannableStringBuilder()
            val name = SpannableString("456456")
            name.setSpan(CustomTextView.TextClickSpan(name, Color.BLUE, ISingleListener {
                holder.itemView.context.toast(name.toString())
            }),0,name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            sb.append(name)
            sb.append("123123132132132123")
            holder.itemText.text = sb

        }
    }
}