package com.ls.test.testutils

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.PathUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ls.comm_util_library.ISingleResultListener
import com.ls.comm_util_library.PathFactory
import com.ls.comm_util_library.PathImageView
import com.ls.comm_util_library.dp2px
import com.ls.glide_library.GlideApp
import kotlinx.android.synthetic.main.activity_path_view.*
import kotlinx.android.synthetic.main.dialog_test_file_copy_layout.*
import kotlinx.android.synthetic.main.item_path_view.view.*
import kotlin.random.Random

class PathViewActivity : AppCompatActivity() {

    private val mAdapter by lazy {
        Adapter(this)
    }

    private fun getDrawableId(): Int{
        val i = Random.nextInt(5)
        if(i == 1) return R.drawable.verification_code_1
        else if(i == 2) return R.drawable.verification_code_2
        else if(i == 3) return R.drawable.verification_code_3
        else if(i == 4) return R.drawable.verification_code_4
        else if(i == 5) return R.drawable.verification_code_5
        else return R.drawable.ic_launcher_foreground
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_view)

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        add.setOnClickListener {
            val transformation = PathCropTransformation(PathFactory.getSquare(6,
                (imageView.measuredWidth / 2).toFloat(),0F),30F,dp2px(1F),Color.RED)
//            val transformation = PathCropTransformation(PathFactory.getCircle((imageView.measuredWidth / 2).toFloat(),0F),0F,dp2px(1F),Color.RED)
            val options = RequestOptions.bitmapTransform(CustomCropTransformation((imageView.measuredWidth / 2).toFloat()))
            Glide.with(this).load(getDrawableId()).apply(options).into(imageView)
            circleView.setImageResource(getDrawableId())
            mAdapter.add(1)
        }

        addAll.setOnClickListener {
            mAdapter.addAll(-1,3)
        }

        remove.setOnClickListener {
            mAdapter.remove(3)
        }

        removeAll.setOnClickListener {
            mAdapter.removeAll(100,3)
        }


    }

    class Adapter(context: Context): RecyclerView.Adapter<Adapter.ViewHolder>() {
        private var size = 20
        private val mContext = context
        class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_path_view,parent,false)) {
            init {
                itemView.pathView.setConfig(ISingleResultListener {
                    val config = PathImageView.Config()
                    config.setBorderWidth(10F)
                    config.setBorderColor(Color.GREEN)
                    config.setRotate(30F)
                    val radius = if(it.width > it.height) (it.height / 2).toFloat() else (it.width / 2).toFloat()
//                val path = PathFactory.getSquare(6,radius, config.getBorderWidth())
                    val path = PathFactory.getCircle(radius, config.getBorderWidth())
                    config.setPath(path)
                })
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent)
        }

        fun add(index: Int){
            if(index == -1 || index >= size - 1){
                size++
                notifyItemInserted(size - 1)
                return
            }
            size++
            notifyItemInserted(index)
        }

        fun addAll(index: Int,count: Int){
            if(count <= 0) return
            if(index == -1 || index >= size - 1){
                size+=count
                notifyItemRangeInserted(size - count,count)
                return
            }
            size+=count
            notifyItemRangeInserted(index,count)
        }

        fun remove(index: Int){
            if(index <= 0){
                size--
                notifyItemRemoved(0)
                return
            }
            else if(index >= size - 1){
                size--
                notifyItemRemoved(size)
            }
            else{
                size--
                notifyItemRemoved(index)
            }
        }

        fun removeAll(index: Int,count: Int){
            if(count <= 0) return
            if(index <= 0){
                size-=count
                notifyItemRangeRemoved(0,count)
                return
            }
            else if(index >= size - 1){
                size-=count
                notifyItemRangeRemoved(size,count)
            }
            else{
                size-=count
                notifyItemRangeRemoved(index,count)
            }
        }

        override fun getItemCount(): Int {
            return size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            GlideApp.with(mContext).load(R.drawable.verification_code_2).to(holder.itemView.pathView)
        }
    }
}