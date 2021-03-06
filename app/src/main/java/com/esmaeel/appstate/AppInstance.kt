package com.esmaeel.appstate

import android.app.Application
import com.esmaeel.statelib.initNetworkStateHandler
import com.esmaeel.statelib.registerActivityTracker

class AppInstance : Application() {

    override fun onCreate() {
        super.onCreate()

        //---------------------------- current Activity tracker ---------------------//
        registerActivityTracker()

        //---------------------------- NetworkState ---------------------//
        initNetworkStateHandler()

    }
}