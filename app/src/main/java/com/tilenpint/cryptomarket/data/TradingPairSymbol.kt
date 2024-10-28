package com.tilenpint.cryptomarket.data

data class TradingPairSymbol(
    val symbol: String,
    val lastPrice: Float,
    val dailyChange: Float,
    val dailyChangeRelative: Float,
    val mainSymbol: CurrencySymbol? = null,
    val secondSymbol: CurrencySymbol? = null
) {
    val dailyChangePercent = dailyChangeRelative * 100
    val dailyChangeIsPositive = dailyChangePercent >= 0
}