package com.ls.test.testutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ls.comm_util_library.*
import kotlinx.android.synthetic.main.activity_file.*
import kotlinx.android.synthetic.main.dialog_test_file_copy_layout.*
import java.io.File
import java.lang.Exception

class FileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        generate.setOnClickListener {
            mWaitDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.createFixFile(AndroidFileUtils.getCachePath(this) + "/temp",FileUtils.MB * 200)
                mWaitDialog.dismiss()
            })
        }

        streamCopy.setOnClickListener {
            val temp = File(AndroidFileUtils.getCachePath(this) + "/temp")
            mProgressDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.copy(AndroidFileUtils.getCachePath(this) + "/temp",AndroidFileUtils.getCachePath(this) + "/dest/temp",object : FileUtils.IWriteListener{
                    override fun onSuccess() {
                        mProgressDialog.dismiss()
                    }

                    override fun onError(e: Exception?) {
                        mProgressDialog.dismiss()
                    }

                    override fun onWrite(length: Long) {
                        ThreadUtils.execMain(Runnable {
                            setProgress(length,temp.length())
                        })
                    }

                })
            })
        }

        nioCopy.setOnClickListener {
            val temp = File(AndroidFileUtils.getCachePath(this) + "/temp")
            mProgressDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.nioCopy(AndroidFileUtils.getCachePath(this) + "/temp",AndroidFileUtils.getCachePath(this) + "/destNIO/temp",object : FileUtils.IWriteListener{
                    override fun onSuccess() {
                        mProgressDialog.dismiss()
                    }

                    override fun onError(e: Exception?) {
                        mProgressDialog.dismiss()
                    }

                    override fun onWrite(length: Long) {
                        ThreadUtils.execMain(Runnable {
                            setProgress(length,temp.length())
                        })
                    }

                })
            })
        }

        mappedCopy.setOnClickListener {
            val temp = File(AndroidFileUtils.getCachePath(this) + "/temp")
            mProgressDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.mappedCopy(AndroidFileUtils.getCachePath(this) + "/temp",AndroidFileUtils.getCachePath(this) + "/destMapped/temp",object : FileUtils.IWriteListener{
                    override fun onSuccess() {
                        mProgressDialog.dismiss()
                    }

                    override fun onError(e: Exception?) {
                        mProgressDialog.dismiss()
                    }

                    override fun onWrite(length: Long) {
                        ThreadUtils.execMain(Runnable {
                            setProgress(length,temp.length())
                        })
                    }

                })
            })
        }
    }

    private val mWaitDialog by lazy {
        Util.customDialog(R.layout.dialog_loading_layout,this)
    }

    private val mProgressDialog by lazy {
        Util.customDialog(R.layout.dialog_test_file_copy_layout,this)
    }

    private fun setProgress(length: Long,total: Long){
        mProgressDialog.progress.progress = NumberUtils.getPercent(length,total)
    }
}
