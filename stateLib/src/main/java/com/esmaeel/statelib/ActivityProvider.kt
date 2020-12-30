package com.esmaeel.statelib

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * This changes from the NetworkStateHandler to give appHasNetwork
 * some credibility and to make sure it's a valid value without making it nullable
 */
var ACTIVITY_TRACKER_REGISTERED: Boolean = false

/**
 *  A Global Network state variable to access from anywhere without
 *  the hassle of Injecting the Class in every consumer class.
 *
 *  set(value) -> updates the value and makes ACTIVITY_TRACKER_REGISTERED true
 *                in order to Allow access to the Getter.
 *                the setter is private to prevent changes from outside this module
 *
 *  get ()     -> checks first for the `registerActivityTracker` being called in the Application class onCreate.
 *                in order to make it easy to access the global value.
 *
 *  default value is Null, cause it will be updated instantly if registerActivityTracker has been registered in application's onCreate
 */
var currentActivity: Activity? = null
    private set(value) {
        ACTIVITY_TRACKER_REGISTERED = true
        field = value
    }
    get() {
        if (ACTIVITY_TRACKER_REGISTERED.not())
            throw IllegalStateException(
                """
                    
                    Caused by: 
                        You should call registerActivityTracker()
                        inside the Application class in order to access this value globally.
                        Also you must not access currentActivity value in Application Class's onCreate,
                        as it won't be accessible until the Tracker starts, an activity is being presented -
                        and the value gets updated.
                          
                    """
            )
        return field
    }

fun Application.registerActivityTracker(
    onActivityCreatedCallback: ((activity: Activity, savedInstanceState: Bundle?) -> Unit?)? = null,
    onActivityStartedCallback: ((activity: Activity) -> Unit?)? = null,
    onActivityResumedCallback: ((activity: Activity) -> Unit?)? = null,
    onActivityPausedCallback: ((activity: Activity) -> Unit?)? = null,
    onActivityStoppedCallback: ((activity: Activity) -> Unit?)? = null,
    onActivitySaveInstanceStateCallback: ((activity: Activity, outState: Bundle) -> Unit?)? = null,
    onActivityDestroyedCallback: ((activity: Activity) -> Unit?)? = null,
    onStateChanged: ((activity: Activity) -> Unit?)? = null,
) {
    registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            currentActivity = activity
            onStateChanged?.invoke(activity)
            onActivityCreatedCallback?.invoke(activity, savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            currentActivity = activity
            onStateChanged?.invoke(activity)
            onActivityStartedCallback?.invoke(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            currentActivity = activity
            onStateChanged?.invoke(activity)
            onActivityResumedCallback?.invoke(activity)
        }

        /**
         *  we could make the currentActivity Null again in these cases, but in case of
         *  the consumer started an activity then finished the previous one
         *  it will make the real currentActivity null due to the lifecycle calling order.
         */
        override fun onActivityPaused(activity: Activity) {
            onStateChanged?.invoke(activity)
            onActivityPausedCallback?.invoke(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            onStateChanged?.invoke(activity)
            onActivityStoppedCallback?.invoke(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            onStateChanged?.invoke(activity)
            onActivitySaveInstanceStateCallback?.invoke(activity, outState)
        }

        override fun onActivityDestroyed(activity: Activity) {
            onStateChanged?.invoke(activity)
            onActivityDestroyedCallback?.invoke(activity)
        }

    })
}
