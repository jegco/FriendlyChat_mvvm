package com.google.firebase.udacity.friendlychat.helpes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE



/**
 * Created by jorge_caro on 2/9/18.
 */
class InternetHelper(private var context: Context) {

    fun checkInternet(): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return mWifi.isConnected
    }
}