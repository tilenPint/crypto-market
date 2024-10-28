package com.tilenpint.cryptomarket.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged


class NetworkConnectionObserverImpl(private val context: Context): NetworkConnectionObserver {
    override fun observeConnectivityAsFlow(): Flow<NetworkState> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = networkCallback { connectivityState ->
            trySend(connectivityState)
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        val currentState = getCurrentConnectivityState(connectivityManager)
        trySend(currentState)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    private fun networkCallback(callback: (NetworkState) -> Unit): ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                callback(NetworkState.Available)
            }

            override fun onLost(network: Network) {
                callback(NetworkState.Unavailable)
            }
        }

    private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): NetworkState {
        val network = connectivityManager.activeNetwork

        val isConnected = connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

        return if (isConnected) {
            NetworkState.Available
        } else {
            NetworkState.Unavailable
        }
    }
}