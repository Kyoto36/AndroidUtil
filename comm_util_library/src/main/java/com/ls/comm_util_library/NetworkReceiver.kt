package com.ls.comm_util_library

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkReceiver: BroadcastReceiver() {
    private val mListeners = ArrayList<NetworkListener>()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent?.action){
            for (listener in mListeners){
                listener.onChange(NetworkUtils.getNetworkTypes(context))
            }
        }
    }

    fun addListener(listener: NetworkListener){
        if(!mListeners.contains(listener)){
            mListeners.add(listener)
        }
    }

    fun removeListener(listener: NetworkListener){
        if(mListeners.contains(listener)){
            mListeners.remove(listener)
        }
    }


}