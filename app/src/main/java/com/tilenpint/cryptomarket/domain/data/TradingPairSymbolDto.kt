package com.tilenpint.cryptomarket.domain.data

data class TradingPairSymbolDto(
    val symbol: String,
    val bid: Float,
    val bidSize: Float,
    val ask: Float,
    val askSize: Float,
    val dailyChange: Float,
    val dailyChangeRelative: Float,
    val lastPrice: Float,
    val volume: Float,
    val high: Float,
    val low: Float
)
