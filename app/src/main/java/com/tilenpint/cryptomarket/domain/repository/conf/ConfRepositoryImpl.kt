package com.tilenpint.cryptomarket.domain.repository.conf

import com.tilenpint.cryptomarket.domain.data.CurrencySymbolDto
import com.tilenpint.cryptomarket.domain.remote.ConfApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import com.tilenpint.cryptomarket.domain.result.Result

class ConfRepositoryImpl(private val confApi: ConfApi) : ConfRepository {
    private val _conf = MutableStateFlow<Result<List<CurrencySymbolDto>>>(Result.Progress())
    override val conf: Flow<Result<List<CurrencySymbolDto>>> = _conf.filterNotNull()

    override suspend fun loadSymbols() {
            try {
                _conf.emit(Result.Progress(data = _conf.value.data))

                val data = confApi.getConf()

                _conf.emit(Result.Success(data))
            } catch (e: Exception) {
                _conf.emit(Result.Error(e, _conf.value.data))
            }
    }
}