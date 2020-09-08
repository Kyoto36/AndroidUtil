package com.ls.comm_util_library

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName: RecyclerViewPreLoad
 * @Description:
 * @Author: ls
 * @Date: 2019/12/12 11:30
 */
abstract class RecyclerViewPreLoad: BasePreLoad() {
    private lateinit var mRecyclerView: RecyclerView
    private var mPreLastIndex = 5

    fun attach(recyclerView: RecyclerView,preLastIndex: Int = 5): RecyclerViewPreLoad{
        mRecyclerView = recyclerView
        mPreLastIndex = if(preLastIndex == 0) 5 else preLastIndex
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(isCanPreLoad()) {
                    startLoad()
                }
            }
        })
        return this
    }

    override fun isCanPreLoad(): Boolean {
        val layoutManager = mRecyclerView.layoutManager
        if(layoutManager is LinearLayoutManager){
            return !isLoading && mRecyclerView.isAttachedToWindow && layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - mPreLastIndex && layoutManager.findFirstVisibleItemPosition() > 0
        }
        if(layoutManager is GridLayoutManager){
            return !isLoading && mRecyclerView.isAttachedToWindow && layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - mPreLastIndex && layoutManager.findFirstVisibleItemPosition() > 0
        }
        return false
    }
}