package com.ls.test.testutils

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import com.ls.comm_util_library.toast
import com.ls.glide_library.GlideUtils
import com.ls.permission.Permissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testPermission.setOnClickListener {
            Permissions.with(this).requestStorage().callback {
                if(it){
                    toast("获取存储权限成功")
                }
                else{
                    toast("获取存储权限失败")
                }
            }
        }

        testGlide.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.diaolog_test_glide_layout)
            dialog.show()
            val image = dialog.findViewById<ImageView>(R.id.image)
            GlideUtils.load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595317293718&di=9eb80e9b2287d932d7467f3774186904&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg"
            ,image,R.mipmap.ic_launcher,R.mipmap.ic_launcher)
        }

        testCircular.setOnClickListener {
            startActivity(Intent(this,CircularActivity::class.java))
        }

        testFile.setOnClickListener {
            startActivity(Intent(this,FileActivity::class.java))
        }
    }
}
