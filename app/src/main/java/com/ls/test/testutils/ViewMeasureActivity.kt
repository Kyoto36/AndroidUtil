package com.ls.test.testutils

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ls.comm_util_library.IResultListener
import com.ls.comm_util_library.ISingleResultListener
import com.ls.comm_util_library.LogUtils
import com.ls.comm_util_library.TxtUtils
import com.ls.comm_util_library.Util
import kotlinx.android.synthetic.main.activity_view_measure.*
import org.w3c.dom.Text
import kotlin.random.Random

class ViewMeasureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_measure)

        addView.setOnClickListener {
            var i = 0
            autoLine.fillView(IResultListener {
                generateView()
            }, ISingleResultListener {
                (it as TextView).text = TxtUtils.randomString(Random.nextInt(20))
                i++ < 100
            })
        }
    }

    private fun generateView(): TextView{
        val textVew = TextView(this)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.leftMargin = Util.dp2px(5F).toInt()
        lp.rightMargin = Util.dp2px(5F).toInt()
        lp.topMargin = Util.dp2px(5F).toInt()
        lp.bottomMargin = Util.dp2px(5F).toInt()
        textVew.layoutParams = lp
        textVew.text = "123456"
        textVew.setTextColor(Color.WHITE)
        return textVew
    }
}