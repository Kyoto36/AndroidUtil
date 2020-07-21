package com.ls.video_player_library

import android.media.MediaPlayer
import com.ls.comm_util_library.ThreadUtils

/**
 * @ClassName: VideoThread
 * @Description:
 * @Author: ls
 * @Date: 2019/9/4 11:57
 */
class VideoThread(mediaPlayer: MediaPlayer,listener: ((Int) -> Unit)): Thread() {
    private var mMediaPlayer = mediaPlayer
    private var mListener = listener
    override fun run() {
        while(!isInterrupted){
            if (mMediaPlayer.currentPosition >= mMediaPlayer.duration) {
                mListener.invoke(mMediaPlayer.duration)
                return
            }
            if(updateProgress) {
                ThreadUtils.execMain(Runnable {
                    mListener.invoke(mMediaPlayer.currentPosition)
                })
            }

            try {
                sleep(1000)
            } catch (e: InterruptedException) {
                interrupt()
            }
        }
    }

    fun stopListener(){
        mListener.invoke(mMediaPlayer.duration)
        interrupt()
    }

    private var updateProgress = true

    fun updateProgress(isUpdate: Boolean){
        updateProgress = isUpdate
    }


}