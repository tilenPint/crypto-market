package com.tilenpint.cryptomarket.domain.repository.conf

import com.tilenpint.cryptomarket.domain.data.CurrencySymbolDto
import com.tilenpint.cryptomarket.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface ConfRepository {
    val conf: Flow<Result<List<CurrencySymbolDto>>>

    suspend fun loadSymbols()
}