package com.ls.comm_util_library

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName: CommRecyclerViewAdapter
 * @Description:
 * @Author: ls
 * @Date: 2019/9/11 17:13
 */
abstract class CommRecyclerViewAdapter<VH: CommRecyclerViewAdapter.CommViewHolder<T>,T>(context: Context, datas: MutableList<T>?): RecyclerViewAdapter<VH,T>(context, datas) {

    protected var mIsScrolling = false
    protected var mOnItemClickListener: IDoubleListener<Int,T>? = null
    protected var mOnChildViewClickListener: IThreeListener<Int,Int,T>? = null

    fun setScrolling(isScrolling: Boolean){
        mIsScrolling = isScrolling
    }

    fun setDatas(datas: MutableList<T>?){
        mDatas = datas
        notifyDataSetChanged()
    }
    
    open fun getDatas(): MutableList<T>?{
        return mDatas
    }

    fun addData(vararg data: T){
        if(data.isEmpty()){
            return
        }
        if(mDatas == null) mDatas = ArrayList()
        mDatas!!.addAll(data)
        notifyItemRangeInserted(getViewPosition(mDatas!!.size - data.size),data.size)
    }

    fun updateData(index: Int){
        if(mDatas?.size?:0 > index){
            notifyItemChanged(getViewPosition(index))
        }
    }

    fun updateData(index: Int, data: T?){
        if(data == null){
            updateData(index)
            return
        }
        if(mDatas?.size?:0 > index){
            mDatas!![index] = data
            notifyItemChanged(getViewPosition(index))
        }
    }

    fun getItemData(index: Int): T?{
        if(mDatas?.size?:0 > index){
            return mDatas?.get(index)
        }
        return null
    }

    fun insertData(index: Int,data: T){
        if(mDatas == null) mDatas = ArrayList()
        var position = 0
        if(index <= 0){
            mDatas!!.add(0,data)
            position = getViewPosition(0)
            notifyItemInserted(position)
        }
        else if(index >= mDatas!!.size){
            mDatas!!.add(data)
            position = getViewPosition(mDatas!!.size - 1)
            notifyItemInserted(position)
        }
        else {
            mDatas!!.add(index,data)
            position = getViewPosition(index)
            notifyItemInserted(position)
        }
        if((itemCount - position) > 0) {
            LogUtils.d("insertData", "position ${position} itemCount - position ${itemCount - position} $itemCount")
            notifyItemRangeChanged(position, itemCount - position)
        }
    }

    fun removeData(index: Int){
        if(!mDatas.isNullOrEmpty()){
            mDatas!!.removeAt(index)
            notifyItemRemoved(getViewPosition(index))
            notifyItemRangeChanged(getViewPosition(index), mDatas!!.size - index)
        }
    }

    fun addData(items: MutableList<out T>?){
        if(items.isNullOrEmpty()){
            return
        }
        if(mDatas == null) mDatas = ArrayList()
        mDatas!!.addAll(items)
        notifyItemRangeInserted(getViewPosition(mDatas!!.size - items.size),items.size)
    }

    fun setOnItemClickListener(listener: IDoubleListener<Int,T>){
        mOnItemClickListener = listener
    }

    fun setOnChildViewClickListener(onChildViewClickListener: IThreeListener<Int,Int,T>){
        mOnChildViewClickListener = onChildViewClickListener
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if(holder is CommViewHolder<*>) holder.onViewRecycled()
    }

    override fun onBindItemViewHolder(holder: VH, index: Int) {
        holder.itemCount = itemCount
        holder.setScrolling(mIsScrolling)

        val item = mDatas!![index]
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onValue(index,item)
        }
        holder.setOnChildViewClickListener(mOnChildViewClickListener)
        holder.bindData(item,index)
    }

    abstract class CommViewHolder<T>(view: View): RecyclerView.ViewHolder(view){
        var itemCount = 0
        protected var mOnChildViewClickListener: IThreeListener<Int,Int,T>? = null
        abstract fun bindData(data: T,index: Int)

        open fun onViewRecycled(){}

        protected var mIsScrolling = false

        fun setScrolling(isScrolling: Boolean){
            mIsScrolling = isScrolling
        }

        fun setOnChildViewClickListener(onChildViewClickListener: IThreeListener<Int,Int,T>?){
            mOnChildViewClickListener = onChildViewClickListener
        }
    }
}