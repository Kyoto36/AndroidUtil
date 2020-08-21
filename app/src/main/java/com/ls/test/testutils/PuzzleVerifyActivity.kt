package com.ls.test.testutils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ls.comm_util_library.IStateListener
import com.ls.comm_util_library.toast
import kotlinx.android.synthetic.main.activity_puzzle_verify.*
import java.lang.Exception

class PuzzleVerifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_verify)
        image.setAccessListener(object : IStateListener<Void>{
            override fun onSuccess(t: Void?) {
                toast("success")
                seekBar.isEnabled = false
            }

            override fun onFailed(e: Exception?) {
                toast("failed")
                seekBar.isEnabled = false
            }
        })
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                image.move(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                image.start()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                image.stop()
            }
        })
        switchImage.setOnClickListener {
            image.reset()
            seekBar.isEnabled = true
            seekBar.progress = 0
            switchImage()
        }
    }

    private fun switchImage(){
        val tempBitmap = Bitmap.createBitmap(
            image.getWidth(),
            image.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val temp = Canvas(tempBitmap)
        temp.concat(image.getImageMatrix())
        image.getDrawable()!!.draw(temp)
        image.setImageBitmap(tempBitmap)
    }
}
