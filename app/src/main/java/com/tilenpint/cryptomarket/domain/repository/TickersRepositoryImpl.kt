package com.tilenpint.cryptomarket.domain.repository

import com.tilenpint.cryptomarket.data.TradingPairSymbol
import com.tilenpint.cryptomarket.domain.data.TradingPairSymbolDto
import com.tilenpint.cryptomarket.base.LoadingStyle
import com.tilenpint.cryptomarket.base.Result
import com.tilenpint.cryptomarket.domain.remote.TickersApi
import com.tilenpint.cryptomarket.base.mapData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class TickersRepositoryImpl(private val tickersApi: TickersApi) : TickersRepository {

    private val _tickers = MutableStateFlow<Result<List<TradingPairSymbolDto>>>(Result.Progress())
    override val tickers: Flow<Result<List<TradingPairSymbol>>> =
        _tickers.filterNotNull().map { result ->
            result.mapData { data ->
                data.map {
                    TradingPairSymbol(
                        symbol = it.symbol,
                        lastPrice = it.lastPrice,
                        dailyChange = it.dailyChange,
                        dailyChangeRelative = it.dailyChangeRelative
                    )
                }
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
}