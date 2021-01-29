package com.ls.comm_util_library

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

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

    open fun setDatas(datas: MutableList<T>?){
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

    fun updateData(index: Int,isAnim: Boolean = false){
        if(mDatas?.size?:0 > index){
            if(isAnim){
                notifyItemChanged(getViewPosition(index))
            }
            else {
                notifyItemChanged(getViewPosition(index), index)
            }
        }
    }

    fun updateData(index: Int, data: T?,isAnim: Boolean = false){
        if(data == null){
            updateData(index,isAnim)
            return
        }
        if(mDatas?.size?:0 > index){
            val temp = mDatas!![index]
            if(temp == data){
                updateData(index)
            }
            mDatas!![index] = data
            if(isAnim){
                notifyItemChanged(getViewPosition(index))
            }
            else {
                notifyItemChanged(getViewPosition(index), index)
            }
        }
    }

    fun getItemData(index: Int): T?{
        val count = getRealItemCount()
        if(index < 0 || index > count-1){
            return null
        }
        return mDatas?.get(index)
    }

    /**
     * 查找当前数据在list中的位置，不存在返回-1
     * @param data T
     * @return Int
     */
    fun getItemIndex(data: T): Int{
        if(mDatas.isNullOrEmpty()){
            return -1
        }
        return mDatas!!.indexOf(data)
    }

    /**
     * 有些以Any或者Object为item的，就算添加列表页不会走到添加列表的方法
     * @param index Int
     * @param datas MutableList<T>?
     */
    fun insertDatas(index: Int,datas: MutableList<T>?,isAnim: Boolean){
        insertData(index, datas,isAnim)
    }

    fun insertData(index: Int,datas: MutableList<T>?,isAnim: Boolean = true){
        if(datas.isNullOrEmpty()){
            return
        }
        if(mDatas == null) mDatas = ArrayList()
        var position = 0
        if(index <= 0){
            mDatas!!.addAll(0, datas.toList())
            position = getViewPosition(0)
            if(isAnim) {
                notifyItemRangeInserted(position, datas.size)
            }
        }
        else if(index >= mDatas!!.size){
            val oldPosition = getViewPosition(mDatas!!.size - 1)
            mDatas!!.addAll(datas)
            position = getViewPosition(mDatas!!.size - 1)
            if(isAnim) {
                notifyItemRangeChanged(oldPosition, datas.size)
            }
        }
        else {
            mDatas!!.addAll(index, datas.toList())
            position = getViewPosition(index + datas.size)
            if(isAnim) {
                notifyItemRangeChanged(position, datas.size)
            }
        }
        if(isAnim) {
            if ((itemCount - position) > 0) {
                notifyItemRangeChanged(position, itemCount - position, 0)
            }
        }
        else{
            notifyDataSetChanged()
        }
    }

    fun insertData(index: Int,vararg datas: T){
        if(datas.isNullOrEmpty()){
           return
        }
        insertData(index,datas.toMutableList())
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
            notifyItemRangeChanged(position, itemCount - position,0)
        }
    }

    fun removeData(index: Int){
        if(!mDatas.isNullOrEmpty() && index < mDatas!!.size){
            mDatas!!.removeAt(index)
            notifyItemRemoved(getViewPosition(index))
            notifyItemRangeChanged(getViewPosition(index), mDatas!!.size - index,0)
        }
    }

    fun removeData(data: T){
        if(!mDatas.isNullOrEmpty()){
            val index = mDatas!!.indexOf(data)
            if(index > -1){
                removeData(index)
            }

        }
    }

    fun addData(items: MutableList<out T>?,endRefresh: Boolean = false){
        if(items.isNullOrEmpty()){
            return
        }
        if(mDatas == null) mDatas = ArrayList()
        mDatas!!.addAll(items)
        val position = getViewPosition(mDatas!!.size - items.size)
        if(position > 0 && endRefresh){
            notifyItemRangeInserted(position,items.size)
            notifyItemChanged(position - 1,0)
        }
        else{
            notifyItemRangeInserted(position,items.size)
        }
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
        holder.itemCount = getRealItemCount()
        holder.setScrolling(mIsScrolling)

        val item = mDatas!![index]
        holder.itemView.setOnClickListener (object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                mOnItemClickListener?.onValue(index, item)
            }
        })
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