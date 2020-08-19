package com.ls.comm_util_library

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.RecyclerViewAdapter

/**
 * @ClassName: CommRecyclerViewAdapter
 * @Description:
 * @Author: ls
 * @Date: 2019/9/11 17:13
 */
abstract class CommRecyclerViewAdapter<VH: CommRecyclerViewAdapter.CommViewHolder<T>,T>(context: Context, datas: MutableList<T>?): RecyclerViewAdapter<VH,T>(context, datas) {

    protected var mIsScrolling = false
    protected var mOnItemClickListener: IDoubleListener<Int,T>? = null

    fun setScrolling(isScrolling: Boolean){
        mIsScrolling = isScrolling
        if(!mIsScrolling){
//            notifyDataSetChanged()
        }
    }

    fun setDatas(datas: MutableList<T>?){
        mDatas = datas
        notifyDataSetChanged()
    }
    
    open fun getDatas(): List<T>?{
        return mDatas
    }

    fun addData(vararg data: T){
        if(data.isEmpty()){
            return
        }
        if(mDatas == null) mDatas = ArrayList()
        mDatas!!.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: IDoubleListener<Int,T>){
        mOnItemClickListener = listener
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if(holder is CommViewHolder<*>) holder.onViewRecycled()
    }

    override fun onBindItemViewHolder(holder: VH, position: Int) {
        holder.itemCount = itemCount
        holder.setScrolling(mIsScrolling)

        val item = mDatas!![position]
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onValue(position,item)
        }
        holder.bindData(item,position)
    }

    abstract class CommViewHolder<T>(view: View): RecyclerView.ViewHolder(view){
        var itemCount = 0
        abstract fun bindData(data: T,position: Int)

        open fun onViewRecycled(){}

        protected var mIsScrolling = false

        fun setScrolling(isScrolling: Boolean){
            mIsScrolling = isScrolling
        }
    }
}