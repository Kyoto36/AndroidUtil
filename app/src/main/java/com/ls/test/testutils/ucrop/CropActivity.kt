package com.ls.test.testutils.ucrop

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ls.comm_util_library.FragmentUtils
import com.ls.test.testutils.R
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import java.io.File
import java.util.*

class CropActivity : AppCompatActivity(), UCropFragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        var fragment = supportFragmentManager.findFragmentByTag(UCropFragment::class.java.name)
        if(fragment == null){
            val source = File("/sdcard/Pictures/Screenshots/Screenshot_20201208_213402_com.qhooplay.YuShu.jpg")
            val dest = File(cacheDir,source.name)
            val options = UCrop.Options()
            options.setHideBottomControls(true)
            options.setShowCropGrid(false)
//            options.setShowCropFrame(false)
            options.setAllowedGestures(UCropActivity.SCALE,UCropActivity.NONE,UCropActivity.ALL)
//            options.setCircleDimmedLayer(true)
            options.withAspectRatio(239F,343F)
            fragment = UCrop.of(Uri.fromFile(source), Uri.fromFile(dest)).withOptions(options).fragment
        }
        FragmentUtils.replaceFragment(R.id.fragmentContainer,fragment!!,supportFragmentManager){}

    }

    override fun onCropFinish(result: UCropFragment.UCropResult?) {

    }

    override fun loadingProgress(showLoader: Boolean) {

    }
}