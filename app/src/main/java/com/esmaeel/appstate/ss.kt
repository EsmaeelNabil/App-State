package com.esmaeel.appstate

import android.app.Application
import com.esmaeel.statelib.initNetworkStateHandler
import com.esmaeel.statelib.registerActivityTracker

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()

        initNetworkStateHandler()

        registerActivityTracker()
    }
}



