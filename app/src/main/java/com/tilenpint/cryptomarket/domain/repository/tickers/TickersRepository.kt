package com.tilenpint.cryptomarket.domain.repository.tickers

import com.tilenpint.cryptomarket.data.TradingPairSymbol
import com.tilenpint.cryptomarket.domain.result.LoadingStyle
import com.tilenpint.cryptomarket.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface TickersRepository {
    val tickers: Flow<Result<List<TradingPairSymbol>>>

    suspend fun loadTickers(loadingStyle: LoadingStyle = LoadingStyle.NORMAL)
}