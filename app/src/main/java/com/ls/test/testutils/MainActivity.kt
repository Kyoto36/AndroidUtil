package com.ls.test.testutils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ls.comm_util_library.*
import com.ls.comm_util_library.thumbnails.ImageBean
import com.ls.glide_library.GlideApp
import com.ls.glide_library.GlideUtils
import com.ls.permission.Permissions
import com.ls.test.testutils.intensify.IntensifyActivity
import com.ls.test.testutils.intensify.TestActivity
import com.ls.test.testutils.room_test.RoomTestDatabase
import com.ls.test.testutils.room_test.TestRoom
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

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
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.diaolog_test_glide_layout)
            dialog.show()
            val image = dialog.findViewById<ImageView>(R.id.image)
            val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602935991529&di=5931fccbbcf6459073846914c54f7fc4&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201407%2F27%2F20140727021208_rPEVK.jpeg"
//            GlideUtils.load(
//                url
//                , image, R.mipmap.ic_launcher, R.mipmap.ic_launcher
//            )
            GlideApp.with(this).load(url).topCropRoundCorners(dp2px(5F).toInt()).to(image)
//            Glide.with(this).load(url).into(image)
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
//            cache()
//            threadDispatch()
//            mModelChangeSubject.onNext("123")

        }

        coordinatorTest.setOnClickListener {
            startActivity(Intent(this, CoordinatorActivity::class.java))
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
        }

        editTest.setOnClickListener {
            startActivity(Intent(this,TextActivity::class.java))
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
            Observable.create<String> {
                it.onNext(string)
                while (true){
//                    Thread.sleep(200)
                    Log.d("123123","string = $string")
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            toast(it)
        }

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
