package com.ls.test.testutils

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.ISingleListener
import com.ls.comm_util_library.TxtUtils
import com.ls.comm_util_library.toast
import kotlinx.android.synthetic.main.activity_text_view.*
import kotlinx.android.synthetic.main.item_text.view.*

class TextViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view)

        val name = SpannableString("fhjdka")
//        name.setSpan(CustomTextView.TextClickSpan(name, Color.BLUE, ISingleListener {
//            toast(name.toString())
//        }),0,name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        name.setSpan(IconTextSpan(this,R.color.colorPrimaryDark,name.toString()),0,name.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)



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