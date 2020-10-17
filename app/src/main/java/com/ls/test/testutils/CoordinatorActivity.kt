package com.ls.test.testutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ls.test.testutils.adapter.ArticleDetailAdapter
import kotlinx.android.synthetic.main.activity_coordinator.*

class CoordinatorActivity : AppCompatActivity() {

    private val mAdapter by lazy {
        ArticleDetailAdapter(this, RecyclerViewActivity.getImages())
    }

    private val mLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)


        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = mAdapter
    }


}