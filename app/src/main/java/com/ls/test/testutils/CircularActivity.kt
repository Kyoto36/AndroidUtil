package com.ls.test.testutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.ls.glide_library.GlideUtils
import kotlinx.android.synthetic.main.activity_circular.*

class CircularActivity : AppCompatActivity() {

    private val mHandler = object : Handler(){
        override fun handleMessage(msg: Message) {
            if(msg.arg1 <= 100) {
                progress.setProgress(msg.arg1)
                progress1.setProgress(msg.arg1)
                sendMessage(msg.arg1 + 10)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular)

        GlideUtils.loadCircle("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595317293718&di=9eb80e9b2287d932d7467f3774186904&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg"
        ,image,0F,-1,-1,-1)
        GlideUtils.loadCircle("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595317293718&di=9eb80e9b2287d932d7467f3774186904&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg"
            ,image1,0F,-1,-1,-1)
        sendMessage(10)


    }

    private fun sendMessage(progress: Int){
        mHandler.sendMessageDelayed(mHandler.obtainMessage().also {
            it.arg1 = progress
        },500)
    }
}
