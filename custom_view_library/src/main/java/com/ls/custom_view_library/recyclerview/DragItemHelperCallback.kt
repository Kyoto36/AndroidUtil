package com.ls.custom_view_library.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.IDoubleListener

/**
 * @ClassName: DragItemHelperCallback
 * @Description:
 * @Author: ls
 * @Date: 2020/8/13 16:47
 */
open class DragItemHelperCallback(isHorizontal: Boolean,isVertical: Boolean): ItemTouchHelper.Callback() {

    private val mIsHorizontal = isHorizontal
    private val mIsVertical = isVertical
    private var mOnItemMoveListener: IDoubleListener<Int,Int>? = null

    fun setOnItemMoveListener(listener: IDoubleListener<Int,Int>){
        mOnItemMoveListener = listener
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var dragFlags = 0
        if(mIsHorizontal){
            dragFlags = dragFlags or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }
        if(mIsVertical){
            dragFlags = dragFlags or ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
        return makeMovementFlags(dragFlags,0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //被按下拖拽时候的position
        val fromPosition = viewHolder.adapterPosition
        //当前拖拽到的item的posiiton
        val toPosition = target.adapterPosition
        mOnItemMoveListener?.onValue(fromPosition,toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

}