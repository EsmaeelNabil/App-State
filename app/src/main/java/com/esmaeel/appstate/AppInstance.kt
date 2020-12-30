package com.esmaeel.appstate

import android.app.Application
import com.esmaeel.statelib.initNetworkStateHandler
import com.esmaeel.statelib.register
import com.esmaeel.statelib.registerActivityTracker

class AppInstance : Application() {

    override fun onCreate() {
        super.onCreate()

        //---------------------------- current Activity tracker ---------------------//
        registerActivityTracker()

        initNetworkStateHandler()
//
//        register()

        // for unregistering the callback, if initNetworkStateHandler already initialized the callback,
        // the initialized callback will be returned and no overriding will take place.
//        unregisterNetworkStateHandlerCallback(initNetworkStateHandler())

//        registerActivityTracker {
//             onStateChanged gives you an instance of the current activity
//             on every state `lifecycle` change.
//        }

//        // optional Callbacks
//        registerActivityTracker(
//            onActivityCreatedCallback = { activity, savedInstanceState -> },
//
//            onActivityStartedCallback = { activity -> },
//
//            onActivityPausedCallback = { activity -> },
//
//            onActivityResumedCallback = { activity -> },
//
//            onActivityStoppedCallback = { activity -> },
//
//            onActivitySaveInstanceStateCallback = { activity, outState -> },
//
//            onActivityDestroyedCallback = { activity -> },
//
//            onStateChanged = {
//                // gives you an instance of the current activity on every state `lifecycle` change
//            },
//        )

        // Now you can Access the global realtime updated value,
        // anywhere in your app.
//        val current = currentActivity


        //---------------------------- NetworkState ---------------------//

        // use the Ktx function.
//        initNetworkStateHandler()

        // Now you can Access the global realtime updated value,
        // anywhere in your app.
//        val hasInternet = appHasNetwork


    }
}