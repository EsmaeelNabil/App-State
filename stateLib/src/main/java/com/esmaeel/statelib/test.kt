package com.esmaeel.statelib

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

var hasInternet = false

fun Application.register() {
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).also {
        it.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    hasInternet = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    hasInternet = false
                }
            })
    }

}