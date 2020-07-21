package com.ls.comm_util_library

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class NetworkReceiver2 {
    private val mListeners = ArrayList<NetworkListener>()

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun init(context: Context){
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connManager?.requestNetwork(NetworkRequest.Builder().build(),object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network?) {
                super.onAvailable(network)
                notify(network,connManager)
            }

            override fun onLost(network: Network?) {
                super.onLost(network)
                notify(network,connManager)
            }
        })
    }

    private fun notify(network: Network?,connManager: ConnectivityManager?){
        for (listener in mListeners){
            listener.onChange(NetworkUtils.getNetworkTypes(network, connManager))
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