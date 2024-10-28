package com.tilenpint.cryptomarket.domain.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.tilenpint.cryptomarket.domain.data.CurrencySymbolDto

class CurrencySymbolAdapter {

    @FromJson
    fun fromJson(data: List<List<List<String>>>): List<CurrencySymbolDto> {
        val pairsList = data.firstOrNull() ?: emptyList()

        return pairsList.map { pair ->
            CurrencySymbolDto(
                shortName = pair[0],
                fullName = pair[1]
            )
        }
    }

    @ToJson
    fun toJson(symbols: List<CurrencySymbolDto>): List<List<List<String>>> {
        return listOf(
            symbols.map { symbol ->
                listOf(symbol.shortName, symbol.fullName)
            }
        )
    }
}