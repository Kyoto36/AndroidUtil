package com.ls.test.testutils.thumbnails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.IDoubleListener
import com.ls.comm_util_library.thumbnails.ThumbnailBean
import com.ls.glide_library.GlideUtils
import com.ls.test.testutils.R
import kotlinx.android.synthetic.main.item_thumbnails.view.*

/**
 * @ClassName: ThumbnailsAdater
 * @Description:
 * @Author: ls
 * @Date: 2020/8/13 15:54
 */ 
class DefaultThumbnailsAdapter(context: Context, datas : MutableList<ThumbnailBean>?): RecyclerView.Adapter<DefaultThumbnailsAdapter.ViewHolder>() {

    private val mContext = context
    private var mDatas = datas
    private var mOnItemClickListener: IDoubleListener<Int, ThumbnailBean>? = null

    inner class ViewHolder: RecyclerView.ViewHolder{

        val image: ImageView

        constructor(parent: ViewGroup): super(LayoutInflater.from(mContext).inflate(R.layout.item_thumbnails,parent,false)){
            image = itemView.image
        }
    }

    fun setItemClickListener(listener: IDoubleListener<Int, ThumbnailBean>){
        mOnItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mDatas?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GlideUtils.load(mDatas!![position].data,holder.image,-1,-1)
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onValue(position,mDatas!![position])
        }

    }
}