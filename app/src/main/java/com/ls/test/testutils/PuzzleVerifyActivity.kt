package com.ls.test.testutils

import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ls.comm_util_library.IStateListener
import com.ls.comm_util_library.toast
import com.ls.custom_view_library.puzzle_verify.CaptchaView
import kotlinx.android.synthetic.main.activity_puzzle_verify.*
import java.lang.Exception
import kotlin.random.Random

class PuzzleVerifyActivity : AppCompatActivity() {

    private val mVerifyCodes by lazy {
        val codeTypedArray = resources.obtainTypedArray(R.array.verify_codes)
        Array(codeTypedArray.length()){ i -> codeTypedArray.getResourceId(i,-1)}
    }

    private val mRandom = Random(47)

    private val mCaptchaPopup by lazy {
        CaptchaPopup(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_verify)
        captcha.puzzleView.setImageResource(mVerifyCodes[mRandom.nextInt(mVerifyCodes.size)])
        switchImage.setOnClickListener {
//            captcha.setMode(CaptchaView.Mode.MODE_NONBAR)
//            captcha.reload()
//            captcha.puzzleView.setImageResource(mVerifyCodes[mRandom.nextInt(mVerifyCodes.size)])
//            mCaptchaPopup.show(mVerifyCodes[mRandom.nextInt(mVerifyCodes.size)],rootView)
        }
    }


    private fun switchImage(){
        val image = captcha.puzzleView
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
