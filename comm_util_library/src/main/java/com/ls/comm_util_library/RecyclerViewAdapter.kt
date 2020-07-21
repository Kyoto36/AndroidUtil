package com.ls.comm_util_library

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName: RecyclerViewAdapter
 * @Description:
 * @Author: ls
 * @Date: 2020/3/10 13:48
 */
abstract class RecyclerViewAdapter<VH: RecyclerView.ViewHolder,T>(context: Context,datas: MutableList<T>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var mContext = context
    protected var mDatas = datas

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> {
                HeaderViewHolder(mContext)
            }
            FOOTER_TYPE -> {
                FooterViewHolder(mContext)
            }
            else -> {
                onCreateItemViewHolder(parent, viewType)
            }
        }
    }

    abstract fun onCreateItemViewHolder(parent: ViewGroup,viewType: Int): VH

    final override fun getItemCount(): Int {
        return getRealItemCount() + (if(mHeaderView != null) 1 else 0) + (if(mFooterView != null) 1 else 0)
    }


    open fun getRealItemCount(): Int{
        return mDatas?.size?:0
    }

    final override fun getItemViewType(position: Int): Int {
        return when{
            position == 0 && mHeaderView != null -> HEADER_TYPE
            position == itemCount - 1 && mFooterView != null -> FOOTER_TYPE
            else -> getViewType(getRealPosition(position))
        }
    }

    open fun getViewType(position: Int): Int{ return 0 }

    private fun getRealPosition(position: Int): Int{
        return if(mHeaderView != null) position - 1 else position
    }

    fun isHeader(position: Int): Boolean{
        return getItemViewType(position) == HEADER_TYPE
    }

    fun isFooter(position: Int): Boolean{
        return getItemViewType(position) == FOOTER_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isHeader(position)){
            (holder as HeaderViewHolder).updateView(mHeaderView)
        }
        else if(isFooter(position)){
            (holder as FooterViewHolder).updateView(mFooterView)
        }
        else {
            onBindItemViewHolder(holder as VH, getRealPosition(position))
        }
    }


    abstract fun onBindItemViewHolder(holder: VH,position: Int)

    private var mHeaderView: View? = null
    fun setHeaderView(view: View?){
        mHeaderView = view
    }

    private var mFooterView: View? = null
    fun setFooterView(view: View?){
        mFooterView = view
    }

    open class HeaderViewHolder(context: Context): RecyclerView.ViewHolder(RelativeLayout(context)){
        open fun updateView(view : View?){
            val headerView = itemView as ViewGroup
            headerView.removeAllViews()
            if(view == null){
                headerView.visibility = View.GONE
            }
            else {
                headerView.visibility = View.VISIBLE
                headerView.addView(view)
            }
        }
    }

    open class FooterViewHolder(context: Context): RecyclerView.ViewHolder(RelativeLayout(context)){
        open fun updateView(view : View?){
            val footerView = itemView as ViewGroup
            footerView.removeAllViews()
            if(view == null){
                footerView.visibility = View.GONE
            }
            else {
                footerView.visibility = View.VISIBLE
                footerView.addView(view)
            }
        }
    }

    companion object{
        private val HEADER_TYPE = 3001
        private val FOOTER_TYPE = 3002
    }
}