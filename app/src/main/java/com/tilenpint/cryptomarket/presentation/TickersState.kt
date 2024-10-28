package com.tilenpint.cryptomarket.presentation

import com.tilenpint.cryptomarket.data.CurrencySymbol
import com.tilenpint.cryptomarket.data.TradingPairSymbol
import com.tilenpint.cryptomarket.domain.result.Result

data class TickersState(
    val resultTickers: Result<List<TradingPairSymbol>>?,
    val selectedTab: CurrencySymbol? = null,
    val searchText: String = ""
) {
    val filteredTickers = resultTickers?.data?.filterByTab(selectedTab)?.filterBySearch(searchText)

    val allTabs: List<CurrencySymbol?> = listOf(null) + (resultTickers?.data?.distinctBy {
        it.secondSymbol
    }?.map {
        it.secondSymbol
    } ?: emptyList())
}


private fun List<TradingPairSymbol>.filterByTab(tab: CurrencySymbol?): List<TradingPairSymbol> {
    return if (tab == null) {
        this
    } else {
        filter {
            it.secondSymbol == tab
        }
    }
}

private fun List<TradingPairSymbol>.filterBySearch(text: String): List<TradingPairSymbol> {
    return if (text.isEmpty()) {
        this
    } else {
        filter {
            it.symbol.contains(text, ignoreCase = true) ||
                it.mainSymbol?.shortName?.contains(text, ignoreCase = true) == true ||
                it.mainSymbol?.fullName?.contains(text, ignoreCase = true) == true ||
                it.secondSymbol?.shortName?.contains(text, ignoreCase = true) == true ||
                it.secondSymbol?.fullName?.contains(text, ignoreCase = true) == true
        }
    }
}