package com.ls.test.testutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ls.test.testutils.adapter.ArticleDetailAdapter
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {

    private val mAdapter by lazy {
        ArticleDetailAdapter(this,getImages())
    }

    private val mLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = mAdapter

    }

    private fun getImages(): MutableList<String>{
        return listOf("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597224094137&di=70f85a437e46fdbc4eccafae5428ea90&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F14%2F75%2F01300000164186121366756803686.jpg"
        ,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597224094137&di=2c7497d4f48fd37c8263872ebc319acf&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg"
        ,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597224094137&di=31d3b6b87c64b35db0f8b20c29b32984&imgtype=0&src=http%3A%2F%2Fa0.att.hudong.com%2F56%2F12%2F01300000164151121576126282411.jpg"
        ,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597224094136&di=4d2505a214dcd4732c164bec5da96afa&imgtype=0&src=http%3A%2F%2Fa1.att.hudong.com%2F05%2F00%2F01300000194285122188000535877.jpg"
        ,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597224094136&di=fdd1ad15d52e9516c8d4284d14da37f5&imgtype=0&src=http%3A%2F%2Fa1.att.hudong.com%2F62%2F02%2F01300542526392139955025309984.jpg"
        ,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597224094136&di=03e714576f97c98a7539d0c05ba12301&imgtype=0&src=http%3A%2F%2Fp2.so.qhimgs1.com%2Ft01dfcbc38578dac4c2.jpg").toMutableList()
    }
}
