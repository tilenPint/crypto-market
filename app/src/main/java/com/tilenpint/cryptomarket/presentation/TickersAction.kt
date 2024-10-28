package com.tilenpint.cryptomarket.presentation

import com.tilenpint.cryptomarket.data.CurrencySymbol

sealed interface TickersAction {
    data object ForceRefresh : TickersAction
    data class SelectTab(val value: CurrencySymbol?) : TickersAction
    data class SearchChange(val value: String) : TickersAction
    data object ClearSearch : TickersAction
}