package com.ls.test.testutils.thumbnails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ls.comm_util_library.IDoubleListener
import com.ls.comm_util_library.thumbnails.ImageBean
import com.ls.glide_library.GlideApp
import com.ls.test.testutils.R
import kotlinx.android.synthetic.main.item_thumbnails.view.*


/**
 * @ClassName: ThumbnailsAdater
 * @Description:
 * @Author: ls
 * @Date: 2020/8/13 15:54
 */ 
class DefaultThumbnailsAdapter(context: Context, datas : MutableList<ImageBean>?): RecyclerView.Adapter<DefaultThumbnailsAdapter.ViewHolder>() {

    private val mContext = context
    private var mDatas = datas
    private var mOnItemClickListener: IDoubleListener<Int, ImageBean>? = null
    private val mLoadStrategy by lazy {
        GlideApp.getAlbumStrategy(mContext,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background)
    }

    inner class ViewHolder: RecyclerView.ViewHolder{

        val image: ImageView

        constructor(parent: ViewGroup): super(LayoutInflater.from(mContext).inflate(R.layout.item_thumbnails,parent,false)){
            image = itemView.image
        }
    }

    fun setItemClickListener(listener: IDoubleListener<Int, ImageBean>){
        mOnItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mDatas?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(mContext)
//            .load(mDatas!![position].uri)
////            .transition(new GenericTransitionOptions<>().transition(R.anim.glide_anim))
//            .transition(GenericTransitionOptions<Any>().transition(android.R.anim.slide_in_left))
//            .transition(DrawableTransitionOptions().crossFade(150))
//            .apply(
//                RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .centerCrop()
//                    .placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background)
//            )
//            .thumbnail(0.5f)
//            .into(holder.image)
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
        mLoadStrategy.from(mDatas!![position].uri).into(holder.image)
//        GlideUtils.load(mDatas!![position].uri,holder.image,R.drawable.ic_launcher_background,-1)
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onValue(position,mDatas!![position])
        }

    }
}