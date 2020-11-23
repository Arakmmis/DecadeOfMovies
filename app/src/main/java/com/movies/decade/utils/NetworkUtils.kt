package com.movies.decade.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.koin.java.KoinJavaComponent.inject

private val context: Context by inject(Context::class.java)

fun isDeviceOnline(): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    return activeNetwork?.isConnectedOrConnecting == true
}