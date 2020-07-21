package com.ls.comm_util_library

import android.hardware.Camera
import android.media.MediaRecorder
import java.io.File

class MediaRecordHelper(private val mCamera: Camera, private val mSavePath: String) {
    private val mRecorder: MediaRecorder = MediaRecorder()

    private var isRecorder = false

    fun start(width: Int,height: Int) {
        stop()
        mRecorder.apply {
            setCamera(mCamera)
            // 设置音频源与视频源 这两项需要放在setOutputFormat之前
            setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
            setVideoSource(MediaRecorder.VideoSource.CAMERA)
            //设置输出格式
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            //这两项需要放在setOutputFormat之后 IOS必须使用ACC
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)  //音频编码格式
            //使用MPEG_4_SP格式在华为P20 pro上停止录制时会出现
            //MediaRecorder: stop failed: -1007
            //java.lang.RuntimeException: stop failed.
            // at android.media.MediaRecorder.stop(Native Method)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)  //视频编码格式
            //设置最终出片分辨率
            setVideoSize(width, height)
            setVideoFrameRate(30)
            setVideoEncodingBitRate(3 * 1024 * 1024)
            setOrientationHint(90)
            //设置记录会话的最大持续时间（毫秒）
            setMaxDuration(30 * 1000)
        }
        var saveFile = File(mSavePath)
        if(!saveFile.parentFile.exists()){
            saveFile.parentFile.mkdirs()
        }
        LogUtils.d("save file path = ", mSavePath)
        mRecorder.apply {
            setOutputFile(mSavePath)
            prepare()
            start()
        }
        isRecorder = true
    }

    fun stop() {
        if (isRecorder) {
            mRecorder.apply {
                stop()
                reset()
            }
            isRecorder = false
        }
    }

    fun release() {
        stop()
        mRecorder.release()
    }

}
