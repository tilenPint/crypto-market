package com.tilenpint.cryptomarket.presentation.test

import com.tilenpint.cryptomarket.data.TradingPairSymbol

val btcUsdTest = TradingPairSymbol(
    symbol = "tBTCUSD",
    lastPrice = 60000f,
    dailyChange = 100f,
    dailyChangeRelative = 0.018f
)

val ethUsdTest = TradingPairSymbol(
    symbol = "tETHUSD",
    lastPrice = 5000f,
    dailyChange = 50f,
    dailyChangeRelative = 0.03f
)