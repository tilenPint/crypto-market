package com.tilenpint.cryptomarket.presentation

import android.accounts.NetworkErrorException
import androidx.lifecycle.viewModelScope
import com.tilenpint.cryptomarket.base.BaseViewModel
import com.tilenpint.cryptomarket.base.EmptyError
import com.tilenpint.cryptomarket.domain.repository.conf.ConfRepository
import com.tilenpint.cryptomarket.domain.repository.tickers.TickersRepository
import com.tilenpint.cryptomarket.domain.result.LoadingStyle
import kotlinx.coroutines.flow.onStart
import com.tilenpint.cryptomarket.domain.result.Result
import com.tilenpint.cryptomarket.network.NetworkConnectionObserver
import com.tilenpint.cryptomarket.network.NetworkState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TickersViewModel(
    private val tickersRepository: TickersRepository,
    private val confRepository: ConfRepository,
    private val networkConnectionObserver: NetworkConnectionObserver
) : BaseViewModel<TickersState, TickersAction>() {

    private var autoRefreshingTickersJob: Job? = null

    private var _state = MutableStateFlow(TickersState(Result.Progress()))
    override val state: Flow<TickersState> = _state.onStart {
        onStart()
    }.onCompletion {
        cancelAutoLoad()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(RELOAD_DATA_IN_MS),
        initialValue = TickersState(Result.Progress())
    ).map {
        it.copy(
            resultTickers = if (it.resultTickers is Result.Success &&
                it.resultTickers.data.isEmpty()
            ) {
                Result.Error(EmptyError())
            } else {
                it.resultTickers
            }
        )
    }

    override fun onAction(action: TickersAction) {
        when (action) {
            TickersAction.ForceRefresh -> {
                loadTickers()
            }

            is TickersAction.SelectTab -> {
                _state.update { it.copy(selectedTab = action.value) }
            }

            is TickersAction.SearchChange -> {
                _state.update { it.copy(searchText = action.value) }
            }

            TickersAction.ClearSearch -> {
                _state.update { it.copy(searchText = "") }
            }
        }
    }

    private fun onStart() {
        viewModelScope.launch {
            tickersRepository.tickers.collect { tickers ->
                _state.update { it.copy(resultTickers = tickers) }
            }
        }

        viewModelScope.launch {
            networkConnectionObserver.observeConnectivityAsFlow().collect { networkState ->
                when (networkState) {
                    NetworkState.Available -> {
                        loadConf()
                        loadTickers()
                        autoLoadTickers()
                    }

                    NetworkState.Unavailable -> {
                        cancelAutoLoad()
                    }
                }

                _state.update {
                    it.copy(
                        resultTickers = if (networkState == NetworkState.Unavailable) {
                            println("dsdsds: err")
                            Result.Error(
                                exception = NetworkErrorException(),
                                data = it.resultTickers?.data
                            )
                        } else {
                            it.resultTickers
                        }
                    )
                }
            }
        }
    }

    private fun loadConf() {
        viewModelScope.launch {
            confRepository.loadSymbols()
        }
    }

    private fun loadTickers() {
        viewModelScope.launch {
            tickersRepository.loadTickers()
        }
    }

    private fun autoLoadTickers() {
        viewModelScope.launch {
            while (isActive) {
                delay(RELOAD_DATA_IN_MS)
                tickersRepository.loadTickers(LoadingStyle.SILENT)
            }
        }.apply {
            autoRefreshingTickersJob = this
        }
    }

    private fun cancelAutoLoad() {
        autoRefreshingTickersJob?.cancel()
        autoRefreshingTickersJob = null
    }


    companion object {
        private const val RELOAD_DATA_IN_MS = 5000L
    }
}