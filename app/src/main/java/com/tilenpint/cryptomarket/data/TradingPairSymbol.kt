package com.tilenpint.cryptomarket.data

data class TradingPairSymbol(
    val symbol: String,
    val lastPrice: Float,
    val dailyChange: Float,
    val dailyChangeRelative: Float,
    val secondSymbol: CurrencySymbol = usdSymbol
) {
    private val shortSymbol = symbol
        .removePrefix("t")
        .removeSuffix(":USD")
        .removeSuffix("USD")

    val mainSymbol = CurrencySymbol(shortSymbol)

    val dailyChangePercent = dailyChangeRelative * 100
    val dailyChangeIsPositive = dailyChangePercent >= 0
}

val usdSymbol = CurrencySymbol("USD", "US Dollar")