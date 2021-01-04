package com.esmaeel.appstate

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.esmaeel.appstate.SafeAssert.assertFalseSafely
import com.esmaeel.appstate.SafeAssert.assertTrueSafely
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import org.junit.Assert
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


fun BaseTestContext.isDataConnected(): Boolean =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) isDataConnectedInLowAndroid() else isDataConnectedInHighAndroid()

 fun BaseTestContext.isDataConnectedInHighAndroid(): Boolean {
    val manager =
        device.context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    val cdl = CountDownLatch(1)
    var isConnected = false

    val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    manager.requestNetwork(request, object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            manager.unregisterNetworkCallback(this)
            isConnected = false
            cdl.countDown()
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            manager.unregisterNetworkCallback(this)
            isConnected = true
            cdl.countDown()
        }
    })

    cdl.await(10L, TimeUnit.SECONDS)
    return isConnected
}

 fun BaseTestContext.isDataConnectedInLowAndroid(): Boolean {
    val telephonyManager: TelephonyManager? =
        device.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
    if (telephonyManager?.simState != TelephonyManager.SIM_STATE_READY) {
        return false
    }
    return Settings.Global.getInt(device.context.contentResolver, "mobile_data", 0) == 1
}

fun BaseTestContext.checkWifi(desiredState: Boolean) {
    try {
        if (desiredState) assertTrueSafely { isWiFiEnabled() } else assertFalseSafely { isWiFiEnabled() }
    } catch (assertionError: AssertionError) {
        // There is no mind to check wi-fi in Android emulators before Android 7.1 because
        // wi-fi doesn't have a simulated Wi-Fi access point on such emulators
        // that's why we just skip the wi-fi check on Android below 7.1
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
        else throw assertionError
    }
}

 fun BaseTestContext.isWiFiEnabled(): Boolean =
    (device.context.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.isWifiEnabled
        ?: throw IllegalStateException("WifiManager is unavailable")


object SafeAssert {

    fun BaseTestContext.assertTrueSafely(
        failureMessage: String? = null,
        timeoutMs: Long? = null,
        intervalMs: Long? = null,
        condition: () -> Boolean
    ) {
        flakySafely(
            timeoutMs = timeoutMs,
            intervalMs = intervalMs,
            failureMessage = failureMessage
        ) {
            Assert.assertTrue(condition.invoke())
        }
    }

    fun BaseTestContext.assertFalseSafely(
        failureMessage: String? = null,
        timeoutMs: Long? = null,
        intervalMs: Long? = null,
        condition: () -> Boolean
    ) {
        flakySafely(
            timeoutMs = timeoutMs,
            intervalMs = intervalMs,
            failureMessage = failureMessage
        ) {
            Assert.assertFalse(condition.invoke())
        }
    }
}

fun <T : Activity> goto(to: Class<T>): ActivityScenario<T> {
    return ActivityScenario.launch(to)
}