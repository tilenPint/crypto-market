package com.tilenpint.cryptomarket.domain.repository

import com.tilenpint.cryptomarket.data.TradingPairSymbol
import com.tilenpint.cryptomarket.base.LoadingStyle
import com.tilenpint.cryptomarket.base.Result
import kotlinx.coroutines.flow.Flow

interface TickersRepository {
    val tickers: Flow<Result<List<TradingPairSymbol>>>

    suspend fun loadTickers(loadingStyle: LoadingStyle = LoadingStyle.NORMAL)
}