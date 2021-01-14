package com.ls.test.testutils

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ls.comm_util_library.*
import com.ls.glide_library.GlideApp
import com.ls.permission.Permissions
import com.ls.test.testutils.db_helper.DBHelperActivity
import com.ls.test.testutils.glideprogress.GlideProgressActivity
import com.ls.test.testutils.intensify.TestActivity
import com.ls.test.testutils.room_test.RoomTestDatabase
import com.ls.test.testutils.room_test.TestRoom
import com.ls.test.testutils.ucrop.CropActivity
import com.yalantis.ucrop.UCrop
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InterruptedIOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testPermission.setOnClickListener {
            Permissions.with(this).requestStorage().callback {
                if (it) {
                    toast("获取存储权限成功")
                } else {
                    toast("获取存储权限失败")
                }
            }
        }

        testGlide.setOnClickListener {
//            val dialog = Dialog(this)
//            dialog.setContentView(R.layout.diaolog_test_glide_layout)
//            dialog.show()
//            val image = dialog.findViewById<ImageView>(R.id.image)
//            val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602935991529&di=5931fccbbcf6459073846914c54f7fc4&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201407%2F27%2F20140727021208_rPEVK.jpeg"
////            GlideUtils.load(
////                url
////                , image, R.mipmap.ic_launcher, R.mipmap.ic_launcher
////            )
//            GlideApp.with(this).load(url).topCropRoundCorners(dp2px(5F).toInt()).to(image)
//            Glide.with(this).load(url).into(image)
            startActivity(Intent(this,GlideProgressActivity::class.java))
        }

        testCircular.setOnClickListener {
            startActivity(Intent(this, CircularActivity::class.java))
        }

        testFile.setOnClickListener {
            startActivity(Intent(this, FileActivity::class.java))
        }

        articleDetail.setOnClickListener {
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }

        thumbnails.setOnClickListener {
            startActivity(Intent(this, PictureActivity::class.java))
        }

        scaleImage.setOnClickListener {
//            startActivity(Intent(this, IntensifyActivity::class.java))
//            ScaleActivity.start(this, ImageBean())
            startActivity(Intent(this, TestActivity::class.java))
        }

        puzzleVerify.setOnClickListener {
            startActivity(Intent(this, PuzzleVerifyActivity::class.java))
        }

        pathView.setOnClickListener {
            startActivity(Intent(this, PathViewActivity::class.java))
        }

        measureView.setOnClickListener {
            startActivity(Intent(this, ViewMeasureActivity::class.java))
        }

        clipChildren.setOnClickListener {
            startActivity(Intent(this, ClipChildrenActivity::class.java))
        }
        rxjavaTest.setOnClickListener {
            cache()
//            threadDispatch()
//            mModelChangeSubject.onNext("123")

        }
        dialogTest.setOnClickListener {
            startActivity(Intent(this, DialogActivity::class.java))
        }

        ucopTest.setOnClickListener {
            startActivity(Intent(this, CropActivity::class.java))
        }

        var isTrue = false
        val clickListener = object : ISingleResultListener<Int,Boolean>{
            override fun onResult(p: Int): Boolean {
                if(!isTrue){
                    isTrue = true
                    return true
                }
                return false
            }
        }
        var lastTime = 0L
        val count = AtomicInteger(0)
        val click = Observable.create<View> {emitter ->
            coordinatorTest.setOnClickListener {view ->
//            startActivity(Intent(this, NestedScrollActivity::class.java))
            startActivity(Intent(this, CoordinatorActivity::class.java))
//                if(!emitter.isDisposed){
//                    emitter.onNext(view)
//                }
            }
        }
        click.map {
            val now = System.currentTimeMillis()
            if(now - lastTime > 300){
                count.set(0)
            }
            lastTime = now
            count.addAndGet(1)
        }.subscribe {
            val result = clickListener.onResult(it)
            if(result){
                count.set(0)
            }
            LogUtils.e("456456","coordinatorTest click " + it)
        }

        roomTest.setOnClickListener {
            val test = TestRoom()
            test.userId = "u1001"
            test.type = 1
            test.typeContentId = "a1001"

            ThreadUtils.execIO(Runnable {
                RoomTestDatabase.get(this).testRoomDao().insert(test)
                var all = RoomTestDatabase.get(this).testRoomDao().listAll()
                Log.e("147258","all $all")
                RoomTestDatabase.get(this).testRoomDao().delete(test)
                all = RoomTestDatabase.get(this).testRoomDao().listAll()
                Log.e("147258","all $all")
                RoomTestDatabase.get(this).testRoomDao().insert(test)
                all = RoomTestDatabase.get(this).testRoomDao().listAll()
                Log.e("147258","all $all")
                val test1 = RoomTestDatabase.get(this).testRoomDao().query(test.userId,test.type,test.typeContentId)
                Log.e("147258","test1 $test1")
            })

        }

        textTest.setOnClickListener {
            startActivity(Intent(this,TextViewActivity::class.java))
//            startActivity(Intent(this,FontFamilyActivity::class.java))
        }

        editTest.setOnClickListener {
            startActivity(Intent(this,TextActivity::class.java))
        }

        verticalTest.setOnClickListener {
            startActivity(Intent(this,VerticalActivity::class.java))
        }

        mModelChangeSubject
            .map {
                Log.d("123123 map1", Thread.currentThread().toString())
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("123123 subscribe1", Thread.currentThread().toString())
                toast(it)
            }

        mModelChangeSubject
            .map {
                Log.d("123123 map2", Thread.currentThread().toString())
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("123123 subscribe2", Thread.currentThread().toString())
                toast(it)
            }

        dbHelperTest.setOnClickListener {
            startActivity(Intent(this,DBHelperActivity::class.java))
        }

        webViewTest.setOnClickListener {
            startActivity(Intent(this,WebActivity::class.java))
        }

        val buildId =
            Build.BOARD + Build.SERIAL + Build.PRODUCT + Build.DEVICE + Build.ID + Build.VERSION.INCREMENTAL
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        LogUtils.d("aaaaa", "buildId = $buildId androidId = $androidId")

    }


    private fun cache(){
        val dispose = Observable.create<String>{
            it.onNext("123")
        }.subscribeOn(Schedulers.io())
            .flatMap {string ->
                try {
//                    while (true){
////                    Thread.sleep(200)
//                        Log.d("456456","string = $string")
//                    }
                }catch (e: InterruptedIOException){
                    Log.d("456456","InterruptedIOException = $e")

                }
                Observable.just("45+6")
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
//                Log.d("456456","onNext = $it")
            },{
                Log.d("456456","onError = $it")
            },{
                Log.d("456456","onComplate")
            })

        Handler().postDelayed({
            Log.d("123123","dispose = ${dispose.isDisposed}")
            dispose.dispose()
            Log.d("123123","dispose = ${dispose.isDisposed}")
        },1000)
    }

    private fun threadDispatch(){
        Observable.create<String> {
            Log.d("123123 create", Thread.currentThread().toString())
            it.onNext("123")
        }
            .flatMap {
                Log.d("123123 flatMap", Thread.currentThread().toString())
                Observable.create<String> {
                    Log.d("123123 flatMap create", Thread.currentThread().toString())
                    it.onNext("456")
                }.subscribeOn(Schedulers.io())
            }.subscribeOn(Schedulers.io())
            .flatMap {
                Log.d("123123 flatMap", Thread.currentThread().toString())
                Observable.create<String> {
                    Log.d("123123 flatMap create", Thread.currentThread().toString())
                    it.onNext("456")
                }.subscribeOn(Schedulers.io())
            }.subscribeOn(Schedulers.io())
            .flatMap {
                Log.d("123123 flatMap", Thread.currentThread().toString())
                Observable.create<String> {
                    Log.d("123123 flatMap create", Thread.currentThread().toString())
                    it.onNext("456")
                }.subscribeOn(Schedulers.io())
            }.subscribeOn(Schedulers.io())
            .flatMap {
                Log.d("123123 flatMap", Thread.currentThread().toString())
                Observable.create<String> {
                    Log.d("123123 flatMap create", Thread.currentThread().toString())
                    it.onNext("456")
                }.subscribeOn(Schedulers.io())
            }.subscribeOn(Schedulers.io())
            .map {
                Log.d("123123 map", Thread.currentThread().toString())
                it
            }
            .flatMap {
                Log.d("123123 flatMap", Thread.currentThread().toString())
                Observable.create<String> {
                    Log.d("123123 flatMap create", Thread.currentThread().toString())
                    it.onNext("456")
                }.subscribeOn(Schedulers.io())
            }.subscribeOn(Schedulers.io())
            .flatMap {
                Log.d("123123 flatMap", Thread.currentThread().toString())
                Observable.create<String> {
                    Log.d("123123 flatMap create", Thread.currentThread().toString())
                    it.onNext("456")
                }.subscribeOn(Schedulers.io())
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                toast(it)
            }
    }

    private val a: String? = null

    val mModelChangeSubject by lazy {
        val temp = PublishSubject.create<String>().toSerialized()
        temp
    }
}
