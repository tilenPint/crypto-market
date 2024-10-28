package com.tilenpint.cryptomarket.domain.repository.tickers

import com.tilenpint.cryptomarket.data.CurrencySymbol
import com.tilenpint.cryptomarket.data.TradingPairSymbol
import com.tilenpint.cryptomarket.domain.data.CurrencySymbolDto
import com.tilenpint.cryptomarket.domain.data.TradingPairSymbolDto
import com.tilenpint.cryptomarket.domain.result.LoadingStyle
import com.tilenpint.cryptomarket.domain.result.Result
import com.tilenpint.cryptomarket.domain.remote.TickersApi
import com.tilenpint.cryptomarket.domain.repository.conf.ConfRepository
import com.tilenpint.cryptomarket.domain.result.mapData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

class TickersRepositoryImpl(
    private val tickersApi: TickersApi,
    confRepository: ConfRepository,
) : TickersRepository {

    private val _tickers = MutableStateFlow<Result<List<TradingPairSymbolDto>>>(Result.Progress())
    override val tickers: Flow<Result<List<TradingPairSymbol>>> = combine(
        _tickers.filterNotNull(),
        confRepository.conf
    ) { tickers, conf ->
        tickers.mapData { data ->
            mergeSymbolsWithTradingPairs(conf.data, data)
        }
    }.distinctUntilChanged()

    override suspend fun loadTickers(loadingStyle: LoadingStyle) {
        try {
            _tickers.emit(Result.Progress(loadingStyle = loadingStyle, data = _tickers.value.data))

            val data = tickersApi.getTickers()

            _tickers.emit(Result.Success(data))
        } catch (e: Exception) {
            _tickers.emit(Result.Error(exception = e, data = _tickers.value.data))
        }
    }


    private fun mergeSymbolsWithTradingPairs(
        symbols: List<CurrencySymbolDto>?,
        tradingPairs: List<TradingPairSymbolDto>?
    ): List<TradingPairSymbol> {
        if (symbols == null || tradingPairs == null) return emptyList()

        val symbolMap = symbols.associateBy { it.shortName }

        return tradingPairs.map { tradingPair ->
            // Remove the prefix 't' and split by ":" if it exists
            val trimmedSymbol = tradingPair.symbol.removePrefix("t")
            val parts = trimmedSymbol.split(":")

            val mainSymbolCode: String
            val secondSymbolCode: String

            if (parts.size == 2) {
                mainSymbolCode = parts[0]
                secondSymbolCode = parts[1]
            } else {
                mainSymbolCode = parts[0].substring(0, parts[0].length - 3)
                secondSymbolCode = parts[0].substring(parts[0].length - 3)
            }

            val mainSymbol = symbolMap[mainSymbolCode]
            val secondSymbol = symbolMap[secondSymbolCode]

            TradingPairSymbol(
                symbol = tradingPair.symbol,
                lastPrice = tradingPair.lastPrice,
                dailyChange = tradingPair.dailyChange,
                dailyChangeRelative = tradingPair.dailyChangeRelative,
                mainSymbol = mainSymbol?.let {
                    CurrencySymbol(mainSymbol.shortName, mainSymbol.fullName)
                },
                secondSymbol = secondSymbol?.let {
                    CurrencySymbol(secondSymbol.shortName, secondSymbol.fullName)
                }
            )
        }
    }
}