package com.tilenpint.cryptomarket.presentation

sealed interface TickersAction {
    data object ForceRefresh : TickersAction
    data class SearchChange(val value: String) : TickersAction
    data object ClearSearch : TickersAction

    data object StartAutoCollecting: TickersAction
    data object StopAutoCollecting: TickersAction
}