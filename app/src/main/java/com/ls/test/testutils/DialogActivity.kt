package com.ls.test.testutils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.ls.comm_util_library.changeStatusBarFontDark
import com.ls.comm_util_library.setTranslucentStatusBar
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        fullDialog.setOnClickListener {
            showFullDialog()
        }
    }

    private fun showFullDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.full_dialog)
        dialog.window.decorView.setPadding(0,0,0,0)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = dialog.window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window.attributes = lp
        dialog.window.setTranslucentStatusBar()
        dialog.window.changeStatusBarFontDark(true)
        dialog.show()
    }
}