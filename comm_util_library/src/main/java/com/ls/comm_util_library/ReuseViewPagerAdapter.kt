package com.ls.comm_util_library

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import java.util.*

/**
 * 每个item都一样才能使用这个，不然会出现找不到view的情况
 */
abstract class ReuseViewPagerAdapter<T>(datas: MutableList<T>?): PagerAdapter(){
    protected val mReusePool: LinkedList<View> = LinkedList()
    protected var mDatas = datas

    open fun setDatas(datas: MutableList<T>?){
        mDatas = datas
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    protected var mOnItemClickListener: ISingleListener<T>? = null
    fun setOnItemClickListener(listener: ISingleListener<T>){
        mOnItemClickListener = listener
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        if(!mReusePool.contains(`object`)) {
            mReusePool.add(`object`)
        }

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = if(mReusePool.size == 0){
            createView()
        }
        else{
            mReusePool.removeFirst()
        }
        container.addView(view)
        view.setOnClickListener {
            mOnItemClickListener?.onValue(getItem(position))
        }
        bindData(getItem(position),view)
        return view
    }

    open fun getItem(position: Int): T{
        return mDatas!![position]
    }

    override fun getCount(): Int {
        return mDatas?.size?:0
    }

    abstract fun createView(): View

    abstract fun bindData(data: T,view: View)

}
