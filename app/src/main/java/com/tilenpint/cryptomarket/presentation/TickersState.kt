package com.tilenpint.cryptomarket.presentation

import com.tilenpint.cryptomarket.data.TradingPairSymbol
import com.tilenpint.cryptomarket.base.Result

data class TickersState(
    val resultTickers: Result<List<TradingPairSymbol>>?,
    val searchText: String = ""
) {
    val filteredTickers = resultTickers?.data?.filterBySearch(searchText)
}

private fun List<TradingPairSymbol>.filterBySearch(text: String): List<TradingPairSymbol> {
    return if (text.isEmpty()) {
        this
    } else {
        filter {
            it.symbol.contains(text, ignoreCase = true) ||
                it.mainSymbol.shortName.contains(text, ignoreCase = true) ||
                it.mainSymbol.fullName?.contains(text, ignoreCase = true) == true
        }
    }
}