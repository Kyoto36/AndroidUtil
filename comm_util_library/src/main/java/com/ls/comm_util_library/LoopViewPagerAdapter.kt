package com.ls.comm_util_library

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

/**
 * 目前只能无限循环，无法做到只有一个的时候不循环
 * 目前有个bug，在设置了Transform之后，notifyDataSetChanged会造成view不正常
 * 暂无解，只能每次重新设置adapter，我也很绝望啊
 */
abstract class LoopViewPagerAdapter<T>(datas: MutableList<T>?) : ReuseViewPagerAdapter<T>(datas) {

    protected var mCurrentView: View? = null
    private val mAdapterData = ArrayList<T>()
    private var mInitPosition = 0
    private var mViewPager: ViewPager? = null

    init {
        dealAdapterData(datas ?: ArrayList())
    }

    fun getInitPosition(): Int{
        return mInitPosition
    }

    fun getRealPosition(position: Int): Int{
        return mDatas?.indexOf(mAdapterData[position])?: -1
    }

    override fun setDatas(datas: MutableList<T>?) {
        mDatas = datas ?: ArrayList()
        dealAdapterData(mDatas!!)
        notifyDataSetChanged()
    }

    private fun dealAdapterData(datas: MutableList<T>){
        mAdapterData.clear()
        mAdapterData.addAll(datas)
        if(datas.size > 1){
            mAdapterData.add(0, datas[datas.size - 1])
            mAdapterData.add(datas[0])
            mAdapterData.add(0, datas[datas.size - 2])
            mAdapterData.add(datas[1])
            mInitPosition = 2
        }

    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        mCurrentView = obj as View
    }

    fun getItemView(): View? {
        return mCurrentView
    }

    fun getDataSize(): Int {
        return mDatas?.size ?: 0
    }

    override fun getItem(position: Int): T {
        return mAdapterData[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position) as View
    }

    override fun getCount(): Int {
        return mAdapterData.size
    }
}