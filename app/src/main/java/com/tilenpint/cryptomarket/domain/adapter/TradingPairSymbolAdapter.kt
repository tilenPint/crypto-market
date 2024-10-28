package com.tilenpint.cryptomarket.domain.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.tilenpint.cryptomarket.domain.data.TradingPairSymbolDto

class TradingPairSymbolAdapter {

    @FromJson
    fun fromJson(data: List<Any>): TradingPairSymbolDto {
        return TradingPairSymbolDto(
            symbol = data[0] as String,
            bid = (data[1] as Number).toFloat(),
            bidSize = (data[2] as Number).toFloat(),
            ask = (data[3] as Number).toFloat(),
            askSize = (data[4] as Number).toFloat(),
            dailyChange = (data[5] as Number).toFloat(),
            dailyChangeRelative = (data[6] as Number).toFloat(),
            lastPrice = (data[7] as Number).toFloat(),
            volume = (data[8] as Number).toFloat(),
            high = (data[9] as Number).toFloat(),
            low = (data[10] as Number).toFloat()
        )
    }

    @ToJson
    fun toJson(symbol: TradingPairSymbolDto): List<Any> {
        return listOf(
            symbol.symbol,
            symbol.bid,
            symbol.bidSize,
            symbol.ask,
            symbol.askSize,
            symbol.dailyChange,
            symbol.dailyChangeRelative,
            symbol.lastPrice,
            symbol.volume,
            symbol.high,
            symbol.low
        )
    }
}