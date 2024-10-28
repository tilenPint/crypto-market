package com.tilenpint.cryptomarket.network

import kotlinx.coroutines.flow.Flow

interface NetworkConnectionObserver {
    fun observeConnectivityAsFlow(): Flow<NetworkState>
}

sealed interface NetworkState {
    data object Available : NetworkState
    data object Unavailable : NetworkState
}
