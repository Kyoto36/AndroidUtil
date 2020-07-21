package com.ls.comm_util_library


import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

@RequiresApi(api = Build.VERSION_CODES.O)
class NotificationUtils(base: Context) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    private val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    init {
        createChannels()
    }

    private fun createChannels() {

        // create android channel
        val androidChannel = NotificationChannel(
            ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(false)
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(false)
        // Sets the notification light color for notifications posted to this channel
//        androidChannel.lightColor = Color.GREEN
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        androidChannel.setSound(null, null)

        manager!!.createNotificationChannel(androidChannel)

    }

    fun getAndroidChannelNotification(title: String, body: String): Notification.Builder {
        return Notification.Builder(applicationContext, ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setAutoCancel(true)
    }


    companion object {
        val ANDROID_CHANNEL_ID = "com.baidu.baidulocationdemo"
        val ANDROID_CHANNEL_NAME = "ANDROID CHANNEL"

        fun configForegroundService(context: Context,clazz: Class<out Activity>): Notification {
            var notification: Notification? = null
            //设置后台定位
            //android8.0及以上使用NotificationUtils
            if (Build.VERSION.SDK_INT >= 26) {
                val builder = NotificationUtils(context).getAndroidChannelNotification("后台定位功能", "正在后台定位")
                notification = builder.build()
            } else {
                //获取一个Notification构造器
                val builder = Notification.Builder(context)
                val nfIntent = Intent(context, clazz)

                builder.setContentIntent(PendingIntent.getActivity(context, 0, nfIntent, 0)) // 设置PendingIntent
                        .setContentTitle("后台定位功能") // 设置下拉列表里的标题
                        .setSmallIcon(android.R.drawable.ic_menu_mylocation) // 设置状态栏内的小图标
                        .setContentText("正在后台定位") // 设置上下文内容
                        .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间
                        .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                        .setVibrate(LongArray(1) { 0 })
                        .setSound(null)

                notification = builder.build() // 获取构建好的Notification
            }
        notification?.defaults = Notification.DEFAULT_SOUND //设置为默认的声音
            return notification
        }
    }
}