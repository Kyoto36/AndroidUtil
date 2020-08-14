package com.ls.test.testutils

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.ls.comm_util_library.IDoubleListener
import com.ls.comm_util_library.MediaUtils
import com.ls.comm_util_library.TimeUtils
import com.ls.test.testutils.drag.DefaultSelectedAdapter
import com.ls.test.testutils.drag.DragItemHelperCallback
import com.ls.test.testutils.thumbnails.DefaultThumbnailsAdapter
import kotlinx.android.synthetic.main.activity_picture.*
import java.util.*
import kotlin.collections.ArrayList

class PictureActivity : AppCompatActivity() {

    private val mThumbnailsAdapter by lazy {
        DefaultThumbnailsAdapter(this,MediaUtils.loadImages(this))
    }

    private val mThumbnailsLayoutManager = GridLayoutManager(this,4)

    private val mSelectedLayoutManager = LinearLayoutManager(this,
        LinearLayoutManager.HORIZONTAL,false)

    private val mSelectedAdapter by lazy {
        DefaultSelectedAdapter(this, ArrayList())
    }

    private val mDragItemHelperCallback = DragItemHelperCallback(true,false)
    private val mItemTouchHelper = ItemTouchHelper(mDragItemHelperCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        thumbnails.layoutManager = mThumbnailsLayoutManager
        thumbnails.adapter = mThumbnailsAdapter

        selected.layoutManager = mSelectedLayoutManager
        selected.adapter = mSelectedAdapter
        mItemTouchHelper.attachToRecyclerView(selected)

        mDragItemHelperCallback.setOnItemMoveListener(IDoubleListener { fromPosition, toPosition ->
            mSelectedAdapter.moveItem(fromPosition, toPosition)
        })

        mThumbnailsAdapter.setItemClickListener(IDoubleListener { position, bean ->
            val addIndex = mSelectedAdapter.addItem(bean)
            mSelectedLayoutManager.scrollToPosition(addIndex)
        })

        Log.d("aaaaaa", MediaUtils.loadBuckets(this,3).toString())
        val limit = (System.currentTimeMillis() - TimeUtils.year2Millis(2)) / 1000
        Log.d("aaaaaa", MediaUtils.loadNewestImages(this, limit).toString())
    }


}
