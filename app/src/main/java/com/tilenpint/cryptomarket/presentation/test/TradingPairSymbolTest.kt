package com.tilenpint.cryptomarket.presentation.test

import com.tilenpint.cryptomarket.data.CurrencySymbol
import com.tilenpint.cryptomarket.data.TradingPairSymbol

val btcSymbol = CurrencySymbol("BTC", "Bitcoin")
val ethSymbol = CurrencySymbol("ETH", "Ethereum")
val usdSymbol = CurrencySymbol("USD", "USD")

val btcUsdTest = TradingPairSymbol(
    symbol = "tBTCUSD",
    mainSymbol = btcSymbol,
    secondSymbol = usdSymbol,
    lastPrice = 60000f,
    dailyChange = 100f,
    dailyChangeRelative = 0.018f
)

val ethUsdTest = TradingPairSymbol(
    symbol = "tETHUSD",
    mainSymbol = ethSymbol,
    secondSymbol = usdSymbol,
    lastPrice = 5000f,
    dailyChange = 50f,
    dailyChangeRelative = 0.03f
)