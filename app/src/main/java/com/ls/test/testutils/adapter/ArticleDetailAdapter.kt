package com.ls.test.testutils.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ls.glide_library.GlideUtils
import com.ls.test.testutils.R
import kotlinx.android.synthetic.main.item_image.view.*

/**
 * @ClassName: ArticleDetailAdapter
 * @Description:
 * @Author: ls
 * @Date: 2020/8/12 14:01
 */
class ArticleDetailAdapter(context: Context,list: MutableList<String>?) : RecyclerView.Adapter<ArticleDetailAdapter.ViewHolder>() {
    private val mContext = context
    private var mList = list

    inner class ViewHolder : RecyclerView.ViewHolder{
        val mImage: ImageView

        constructor(parent: ViewGroup): super(LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false)){
            mImage = itemView.image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("789789","onCreateViewHolder")
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("789789","position $position")
        GlideUtils.load(mList!![position],holder.mImage,-1,-1)
    }

    fun setData(data: MutableList<String>?){
        mList = data
        notifyDataSetChanged()
    }
}