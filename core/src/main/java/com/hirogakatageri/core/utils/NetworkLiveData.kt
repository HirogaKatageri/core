package com.hirogakatageri.core.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkLiveData(private val connectivity: ConnectivityManager) : LiveData<Boolean>() {

    override fun onActive() {
        super.onActive()
        getDetails()
    }

    private fun getDetails() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivity.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(true)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    postValue(false)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(false)
                }
            }
        )
    }
}