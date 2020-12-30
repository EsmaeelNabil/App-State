package com.esmaeel.statelib


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest


/**
 * Ktx function to init the NetworkStateHandler in application Class only once
 * and to limit the access to the application class.
 * Returns : A NetworkCallback to be able to unregister in the future using the KTX fun unregisterNetworkStateHandlerCallback.
 *
 * if initNetworkStateHandler already initialized the callback,
 * the initialized callback will be returned and no overriding will take place.
 */
fun Application.initNetworkStateHandler(): ConnectivityManager.NetworkCallback? {
    return NetworkStateHandler(this).registerCallbackUpdater()
}

/**
 * Unregister the provided NetworkState Callback if it's not null
 */
fun Application.unregisterNetworkStateHandlerCallback(callback: ConnectivityManager.NetworkCallback? = null) {
    callback?.let {
        (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).also {
            it.unregisterNetworkCallback(callback)
        }
    }
}


/**
 * This changes when appHasNetwork setter gets called to it some credibility.
 * and to make sure it's a valid value without making it nullable.
 */
private var NETWORK_STATE_HANDLER_IS_INITIALIZED: Boolean = false

/**
 *  A Global Network state variable to access from anywhere without
 *  the hassle of Injecting the Class in every consumer class.
 *
 *  set(value) -> updates the value and makes NETWORK_STATE_HANDLER_IS_INITIALIZED true
 *                in order to Allow access to the Getter.
 *                the setter is private to prevent changes from outside this module
 *
 *  get ()     -> checks first for the `NetworkStateHandler` being initialized in the Application class
 *                in order to make it easy to access the global value without injecting in all consumers.
 *
 *  default value is False, cause it will be updated instantly if NetworkStateHandler has been initialized
 */
var appHasNetwork: Boolean = false
    private set(value) {
        NETWORK_STATE_HANDLER_IS_INITIALIZED = true
        field = value
    }
    get() {
        if (NETWORK_STATE_HANDLER_IS_INITIALIZED.not())
            throw IllegalStateException(
                """
                   
                    Caused by: 
                        You should call initNetworkStateHandler()
                        inside the Application class in order to access this value globally.
                        Also you must not access this value in Application Class's onCreate,
                        as it won't be accessible until the register starts and the value gets updated.
                          
                    """
            )
//            Log.e(
//                TAG, """
//
//                    Caused by:
//                        You should call initNetworkStateHandler()
//                        inside the Application class in order to access this value globally.
//                        Also you must not access this value in Application Class's onCreate,
//                        as it won't be accessible until the register starts and the value gets updated.
//
//                    """
//            )
        return field
    }


private const val TAG = "NetworkStateHandler"

/**
 *  Provides a realtime global Network state variable.
 */
private class NetworkStateHandler(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    /**
     * an Inner value to have it in place.
     * also the setter handles the Global appHasNetwork updates.
     */
    var hasNetwork: Boolean = false
        set(value) {
            field = value
            appHasNetwork = value
        }


    /**
     *  Call it in Application onCreate Class to have the updates globally.
     *  to start updating the appHasNetwork value in realtime
     */
    fun registerCallbackUpdater(): ConnectivityManager.NetworkCallback? {
        // only register the callback once
        if (NETWORK_STATE_HANDLER_IS_INITIALIZED.not()) {

            networkCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    hasNetwork = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    hasNetwork = false
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    hasNetwork = false
                }
            }

            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder()
                    .build(),
                networkCallback!!
            )

            // set the network availability first time
            hasNetwork = isNetworkInterfaceActive(connectivityManager)


            // connectivity callback is registered successfully.
            return networkCallback


        } else {
            // callback is already initialized and emits values to the appHasNetwork var
            return networkCallback
        }
    }

    /**
     *    A workaround for the first initialization of the network state callback
     *    as it doesn't update the state if the first state is
     *    "No network interface is active" -> WIFI or Cellular
     *    returns true if the network interfaces has at least on true
     */
    private fun isNetworkInterfaceActive(connectivityManager: ConnectivityManager): Boolean {

        connectivityManager.allNetworks.forEach {
            if (connectivityManager.getLinkProperties(it)?.interfaceName.isNullOrEmpty().not())
                return true
        }

        return false
    }
}