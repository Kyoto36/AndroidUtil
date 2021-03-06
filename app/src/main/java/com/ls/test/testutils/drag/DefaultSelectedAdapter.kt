package com.ls.test.testutils.drag

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.Util
import com.ls.comm_util_library.thumbnails.ImageBean
import com.ls.glide_library.GlideApp
import com.ls.glide_library.GlideUtils
import com.ls.glide_library.strategy.ILoadStrategy
import com.ls.glide_library.strategy.ImageSelectorStrategy
import com.ls.test.testutils.R
import kotlinx.android.synthetic.main.item_selected.view.*

/**
 * @ClassName: DefaultSelectedAdapter
 * @Description:
 * @Author: ls
 * @Date: 2020/8/13 16:18
 */
class DefaultSelectedAdapter(context: Context, datas: MutableList<ImageBean>?) :
    RecyclerView.Adapter<DefaultSelectedAdapter.ViewHolder>() {

    private val mContext = context
    private var mDatas = datas
    private val mGlideLoader by lazy {
        val strategy = ImageSelectorStrategy()
        GlideApp.getLoader(mContext, strategy)
    }

    inner class ViewHolder : RecyclerView.ViewHolder {

        val image: ImageView

        constructor(parent: ViewGroup) : super(
            LayoutInflater.from(mContext).inflate(R.layout.item_selected, parent, false)
        ) {
            image = itemView.image
        }
    }

    fun addItem(bean: ImageBean): Int {
        if (mDatas == null) {
            mDatas = ArrayList()
        }
        mDatas!!.add(bean)
        notifyItemInserted(mDatas!!.size - 1)
        return mDatas!!.size - 1
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        if (mDatas == null || mDatas!!.size <= 0) return
        val fromBean = mDatas!![fromPosition]
        mDatas!!.removeAt(fromPosition)
        mDatas!!.add(toPosition, fromBean)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mDatas?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        GlideApp.with(mContext)
//            .load(mDatas!![position].uri)
////            .anim(android.R.anim.slide_in_left)
////            .crossFade(150)
//            .noDisk()
//            .centerCropRoundCorners(Util.dp2px(8F).toInt())
//            .toGlide()
//            .placeholder(R.drawable.ic_launcher_background)
//            .error(R.drawable.ic_launcher_background)
//            .thumbnail(0.5F)
//            .into(holder.image)
        mGlideLoader.from(mDatas!![position].uri).to(holder.image)
//        GlideUtils.load(mDatas!![position].path,holder.image,-1,-1)
    }
}