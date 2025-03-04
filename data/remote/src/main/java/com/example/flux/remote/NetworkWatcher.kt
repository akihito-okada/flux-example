package com.example.flux.remote

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

/**
 * Reference : https://stackoverflow.com/a/58960879/3813078
 */
class NetworkWatcher
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
constructor(
    context: Context,
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // general availability of Internet over any type
    var isOnline = false
        get() {
            updateFields()
            return field
        }

    var isOverWifi = false
        get() {
            updateFields()
            return field
        }

    var isOverCellular = false
        get() {
            updateFields()
            return field
        }

    var isOverEthernet = false
        get() {
            updateFields()
            return field
        }

    companion object {
        @Volatile
        private var INSTANCE: NetworkWatcher? = null

        fun getInstance(application: Application): NetworkWatcher {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = NetworkWatcher(application)
                }
                return INSTANCE!!
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun updateFields() {
        val networkAvailability =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        when {
            networkAvailability != null &&
                networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> {
                // has network
                isOnline = true

                // wifi
                isOverWifi =
                    networkAvailability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

                // cellular
                isOverCellular =
                    networkAvailability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

                // ethernet
                isOverEthernet =
                    networkAvailability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }

            else -> {
                isOnline = false
                isOverWifi = false
                isOverCellular = false
                isOverEthernet = false
            }
        }
    }

    fun watchNetwork(): Flow<Boolean> = watchWifi()
        .combine(watchCellular()) { wifi, cellular -> wifi || cellular }
        .combine(watchEthernet()) { wifiAndCellular, ethernet -> wifiAndCellular || ethernet }

    fun watchWifi(): Flow<Boolean> = callbackFlowForType(NetworkCapabilities.TRANSPORT_WIFI)

    fun watchCellular(): Flow<Boolean> = callbackFlowForType(NetworkCapabilities.TRANSPORT_CELLULAR)

    fun watchEthernet(): Flow<Boolean> = callbackFlowForType(NetworkCapabilities.TRANSPORT_ETHERNET)

    private fun callbackFlowForType(type: Int) = callbackFlow {
        trySend(false)

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(type)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onUnavailable() {
                trySend(false)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                // do nothing
            }

            override fun onAvailable(network: Network) {
                trySend(true)
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
}
