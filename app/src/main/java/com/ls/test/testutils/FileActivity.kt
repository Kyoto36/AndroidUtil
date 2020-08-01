package com.ls.test.testutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.ls.comm_util_library.*
import com.ls.permission.Permissions
import com.ls.retrofit_library.download.Download
import kotlinx.android.synthetic.main.activity_file.*
import kotlinx.android.synthetic.main.dialog_test_file_copy_layout.*
import java.io.File
import java.lang.Exception

class FileActivity : AppCompatActivity() {

    private val mTempFileName = "temp"

    private val mSrcPath by lazy {
        AndroidFileUtils.getCachePath(this) + "/"
    }

    private val mDestPath by lazy {
        AndroidFileUtils.getCachePath(this) + "/dest/"
    }
    private val mNioDestPath by lazy {
        AndroidFileUtils.getCachePath(this) + "/destNIO/"
    }
    private val mMappedDestPath by lazy {
        AndroidFileUtils.getCachePath(this) + "/destMapped/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        generate.setOnClickListener {
            mWaitDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.createFixFile(mSrcPath + mTempFileName, FileUtils.MB * 200)
                FileUtils.createDir(mDestPath)
                FileUtils.createDir(mNioDestPath)
                FileUtils.createDir(mMappedDestPath)
                mWaitDialog.dismiss()
            })
        }

        streamCopy.setOnClickListener {
            val temp = File(mSrcPath + mTempFileName)
            mProgressDialog.title.text = "流复制文件"
            mProgressDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.copy(mSrcPath + mTempFileName, mDestPath + mTempFileName, object : FileUtils.IWriteListener {
                    override fun onSuccess() {
                        mProgressDialog.dismiss()
                    }

                    override fun onError(e: Exception?) {
                        mProgressDialog.dismiss()
                    }

                    override fun onWrite(length: Long) {
                        ThreadUtils.execMain(Runnable {
                            setProgress(length, temp.length())
                        })
                    }

                })
            })
        }

        nioCopy.setOnClickListener {
            val temp = File(mSrcPath + mTempFileName)
            mProgressDialog.title.text = "NIO复制文件"
            setProgress(0, 0)
            mProgressDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.nioCopy(mSrcPath + mTempFileName, mNioDestPath + mTempFileName, object : FileUtils.IWriteListener {
                    override fun onSuccess() {
                        mProgressDialog.dismiss()
                    }

                    override fun onError(e: Exception?) {
                        mProgressDialog.dismiss()
                    }

                    override fun onWrite(length: Long) {
                        ThreadUtils.execMain(Runnable {
                            setProgress(length, temp.length())
                        })
                    }

                })
            })
        }

        mappedCopy.setOnClickListener {
            val temp = File(mSrcPath + mTempFileName)
            mProgressDialog.title.text = "内存映射复制文件"
            mProgressDialog.show()
            ThreadUtils.execIO(Runnable {
                FileUtils.mappedCopy(mSrcPath + mTempFileName, mMappedDestPath + mTempFileName, object : FileUtils.IWriteListener {
                    override fun onSuccess() {
                        mProgressDialog.dismiss()
                    }

                    override fun onError(e: Exception?) {
                        mProgressDialog.dismiss()
                    }

                    override fun onWrite(length: Long) {
                        ThreadUtils.execMain(Runnable {
                            setProgress(length, temp.length())
                        })
                    }

                })
            })
        }

        Permissions.with(this).requestStorage().callback { }

        download.setOnClickListener {
            mProgressDialog.title.text = "下载文件"
            mProgressDialog.show()
            val savePath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/yushu.apk"
            mDownload.start("http://www.ximalaya.com/down?tag=web&client=android", savePath,
                object : IProgressListener<String> {
                    override fun onProgress(progress: Long, total: Long) {
                        setProgress(progress, total)
                    }

                    override fun onFinish(t: String?) {
                        toast("下载成功")
                        IntentUtils.startApkInstall(
                            this@FileActivity,
                            File(savePath),
                            this@FileActivity.packageName + ".fileprovider"
                        )
                        mProgressDialog.dismiss()
                        mRunnable = null
                    }

                    override fun onFailed(e: Exception?) {
                        toast("下载失败")
                        mProgressDialog.dismiss()
                        mRunnable = null
                    }
                })
            cancel(Runnable { mDownload.stop(savePath) })
        }

        mProgressDialog.cancel.setOnClickListener {
            if (mRunnable != null) {
                mRunnable!!.run()
                mRunnable = null
            } else {
                mProgressDialog.dismiss()
            }
        }
    }

    private val mDownload by lazy {
        Download.Builder()
            .build(this)
    }

    private val mWaitDialog by lazy {
        Util.customDialog(R.layout.dialog_loading_layout, this)
    }

    private val mProgressDialog by lazy {
        Util.customDialog(R.layout.dialog_test_file_copy_layout, this)
    }

    private var mRunnable: Runnable? = null

    private fun cancel(run: Runnable) {
        mRunnable = run
    }

    private fun setProgress(length: Long, total: Long) {
        mProgressDialog.progress.progress = NumberUtils.getPercent(length, total)
        val percent = NumberUtils.toFixed(NumberUtils.getPercentFloat(length, total), 2)
        mProgressDialog.percent.text = "$percent%"
        val current = FileUtils.formatSize(length)
        val totalSize = FileUtils.formatSize(total)
        mProgressDialog.size.text = "$current/$totalSize"
    }
}
