package com.ls.test.testutils

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ls.test.testutils.adapter.ArticleDetailAdapter

class CoordinatorActivity : AppCompatActivity() {

    private val mAdapter by lazy {
        ArticleDetailAdapter(this, RecyclerViewActivity.getImages())
    }

    private val mLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)

        findViewById<ViewPager>(R.id.viewPager).adapter = object : FragmentPagerAdapter(supportFragmentManager,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
            override fun getItem(position: Int): Fragment {
                return RecyclerFragment(position)
            }

            override fun getCount(): Int {
                return 3
            }
        }
//        recyclerView.layoutManager = mLayoutManager
//        recyclerView.adapter = mAdapter
    }

    class RecyclerFragment(position: Int): Fragment() {
        private val mPosition = position
        private var mInit = false
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            Log.d("RecyclerFragment","onCreateView mPosition $mPosition")
            mInit = false
            return RecyclerView(context!!)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            Log.d("RecyclerFragment","onViewCreated mPosition $mPosition")
            super.onViewCreated(view, savedInstanceState)

        }

        override fun onResume() {
            Log.d("RecyclerFragment","onResume mPosition $mPosition")
            super.onResume()
            if(view is RecyclerView && !mInit) {
                mInit = true
                val recyclerView = view as RecyclerView
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = ArticleDetailAdapter(context!!, RecyclerViewActivity.getImages())
            }
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            Log.d("RecyclerFragment","onActivityCreated mPosition $mPosition")
            super.onActivityCreated(savedInstanceState)
        }

        override fun onStart() {
            Log.d("RecyclerFragment","onStart mPosition $mPosition")
            super.onStart()
        }

        override fun onStop() {
            Log.d("RecyclerFragment","onStop mPosition $mPosition")
            super.onStop()
        }

        override fun onDestroyView() {
            Log.d("RecyclerFragment","onDestroyView mPosition $mPosition")
            super.onDestroyView()
        }
    }


}