package com.ls.retrofit_library.download

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import retrofit2.Retrofit

/**
 * @ClassName: DownloadService
 * @Description:
 * @Author: ls
 * @Date: 2020/3/30 11:52
 */
open class DownloadService: Service() {

    private var mDownload: Download? = null

    protected var mBuilder: NotificationCompat.Builder? = null

    protected open fun getRetrofit(): Retrofit?{ return null}

    override fun onCreate() {
        super.onCreate()
        mDownload = Download.Builder().setRetrofit(getRetrofit()).build(this)
        createChannel()
        setForeground()
    }

    private fun createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN)
            notificationChannel.setSound(null, null)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    protected open fun setForeground(){
        if(mBuilder == null) {
            mBuilder = generateBaseBuilder("下载服务")
        }
        startForeground(SERVICE_ID,mBuilder!!.build())
    }

    private fun getLauncherIntent(): Intent? {
        val i = Intent(Intent.ACTION_MAIN)
        i.component = packageManager.getLaunchIntentForPackage(packageName)!!.component
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        i.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
        return i
    }

    protected fun generateBaseBuilder(content: String): NotificationCompat.Builder {
        val title = packageManager.getApplicationLabel(applicationInfo).toString()
        val i = getLauncherIntent()
        val pendingIntent = PendingIntent.getActivity(this, SERVICE_ID, i, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(applicationInfo.icon)
                .setContentTitle(title)
                .setTicker(content)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)//统一消除声音和震动
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
         return DownloadBinder()
    }

    override fun onDestroy() {
        mDownload?.destroy()
        stopForeground(true)
        super.onDestroy()
    }

    open inner class DownloadBinder: Binder(){
        open fun start(url: String,savePath: String,listener: ProgressListener){
            mDownload?.start(url,savePath, listener)
        }

        // 同一个url可能会下载多个存在不同的地方，所以停止需要用savePath
        open fun stop(savePath: String){
            mDownload?.stop(savePath)
        }

        open fun stopService(){
            mDownload?.stopAll()
            stopSelf()
        }
    }

    companion object{
        val SERVICE_ID = 10001
        val CHANNEL_ID = "retrofit_download_channel"
        val CHANNEL_NAME = "download_service"
    }
}