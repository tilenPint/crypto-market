package com.tilenpint.cryptomarket.presentation

import app.cash.turbine.test
import com.tilenpint.cryptomarket.base.NoNetwork
import com.tilenpint.cryptomarket.domain.repository.TickersRepository
import com.tilenpint.cryptomarket.network.NetworkConnectionObserver
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.tilenpint.cryptomarket.base.Result
import com.tilenpint.cryptomarket.fake.btc
import com.tilenpint.cryptomarket.fake.ltc
import com.tilenpint.cryptomarket.network.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class TickersViewModelTest {

    private lateinit var viewModel: TickersViewModel
    private val tickersRepository: TickersRepository = mockk(relaxed = true)
    private val networkConnectionObserver: NetworkConnectionObserver = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TickersViewModel(tickersRepository, networkConnectionObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state is loading`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.resultTickers is Result.Progress)
        }
    }

    @Test
    fun `load data`() = runTest {
        val result = Result.Success(listOf(btc))

        coEvery {
            networkConnectionObserver.observeConnectivityAsFlow()
        } returns flowOf(NetworkState.Available)

        coEvery { tickersRepository.tickers } returns flowOf(result)

        viewModel.state.test {
            // Progress
            awaitItem()
            awaitItem()

            assertEquals(result, awaitItem().resultTickers)
        }
    }

    @Test
    fun `No network error - Success empty data`() = runTest {
        coEvery {
            networkConnectionObserver.observeConnectivityAsFlow()
        } returns flowOf(NetworkState.Unavailable)

        coEvery { tickersRepository.tickers } returns flowOf(
            Result.Success(emptyList())
        )

        viewModel.state.test {
            // Progress
            awaitItem()
            awaitItem()

            val data = awaitItem()
            assertTrue((data.resultTickers as Result.Error).exception is NoNetwork)
            assertTrue((data.resultTickers as Result.Error).data.isNullOrEmpty())
        }
    }

    @Test
    fun `No network error - Progress empty data`() = runTest {
        coEvery {
            networkConnectionObserver.observeConnectivityAsFlow()
        } returns flowOf(NetworkState.Unavailable)

        coEvery { tickersRepository.tickers } returns flowOf(
            Result.Progress()
        )

        viewModel.state.test {
            // Progress
            awaitItem()
            awaitItem()

            val data = awaitItem()
            assertTrue((data.resultTickers as Result.Error).exception is NoNetwork)
            assertTrue((data.resultTickers as Result.Error).data.isNullOrEmpty())
        }
    }

    @Test
    fun `No network error - Success with data`() = runTest {
        val result = Result.Success(listOf(btc))

        coEvery {
            networkConnectionObserver.observeConnectivityAsFlow()
        } returns flowOf(NetworkState.Unavailable)

        coEvery { tickersRepository.tickers } returns flowOf(
            result
        )

        viewModel.state.test {
            // Progress
            awaitItem()
            awaitItem()

            val data = awaitItem()
            assertTrue((data.resultTickers as Result.Error).exception is NoNetwork)
            assertEquals((data.resultTickers as Result.Error).data, result.data)
        }
    }

    @Test
    fun `No network error - Progress with data`() = runTest {
        val progress = Result.Progress(listOf(btc))

        coEvery {
            networkConnectionObserver.observeConnectivityAsFlow()
        } returns flowOf(NetworkState.Unavailable)

        coEvery { tickersRepository.tickers } returns flowOf(progress)

        viewModel.state.test {
            // Progress
            awaitItem()
            awaitItem()

            val data = awaitItem()
            assertTrue((data.resultTickers as Result.Error).exception is NoNetwork)
            assertEquals((data.resultTickers as Result.Error).data, progress.data)
        }
    }

    @Test
    fun `Unknown error - Empty data`() = runTest {
        coEvery {
            networkConnectionObserver.observeConnectivityAsFlow()
        } returns flowOf(NetworkState.Available)

        coEvery { tickersRepository.tickers } returns flowOf(
            Result.Error(UnknownError())
        )

        viewModel.state.test {
            // Progress
            awaitItem()
            awaitItem()

            val data = awaitItem()
            assertTrue((data.resultTickers as Result.Error).exception is UnknownError)
            assertTrue((data.resultTickers as Result.Error).data.isNullOrEmpty())
        }
    }

    @Test
    fun `Unknown error - With data`() = runTest {
        val error = Result.Error(UnknownError(), listOf(btc))

        coEvery {
            networkConnectionObserver.observeConnectivityAsFlow()
        } returns flowOf(NetworkState.Available)

        coEvery { tickersRepository.tickers } returns flowOf(error)

        viewModel.state.test {
            // Progress
            awaitItem()
            awaitItem()

            val data = awaitItem()
            assertTrue((data.resultTickers as Result.Error).exception is UnknownError)
            assertEquals((data.resultTickers as Result.Error).data, error.data)
        }
    }

    @Test
    fun `onAction SearchChange updates searchText in state`() = runTest {
        viewModel.onAction(TickersAction.SearchChange("search_query"))
        viewModel.state.test {
            // Progress
            awaitItem()

            assertEquals("search_query", awaitItem().searchText)
        }
    }

    @Test
    fun `onAction SearchChange and ClearSearch clears searchText in state`() = runTest {
        viewModel.state.test {
            // Progress
            awaitItem()

            viewModel.onAction(TickersAction.SearchChange("search_query"))

            assertEquals("search_query", awaitItem().searchText)

            viewModel.onAction(TickersAction.ClearSearch)

            assertEquals("", awaitItem().searchText)
        }
    }

    @Test
    fun `filtered data by search`() = runTest {
        val result = Result.Success(listOf(btc, ltc))

        coEvery { tickersRepository.tickers } returns flowOf(result)

        viewModel.state.test {
            // Progress
            awaitItem()

            viewModel.onAction(TickersAction.SearchChange("b"))

            val item = awaitItem()

            assertEquals(listOf(btc), item.filteredTickers)
            assertEquals("b", item.searchText)

            viewModel.onAction(TickersAction.ClearSearch)

            val itemClear = awaitItem()

            assertEquals(listOf(btc, ltc), itemClear.filteredTickers)
            assertEquals("", itemClear.searchText)
        }
    }

    @Test
    fun `do not filter data by search because both contains both query`() = runTest {
        val result = Result.Success(listOf(btc, ltc))

        coEvery { tickersRepository.tickers } returns flowOf(result)

        viewModel.state.test {
            // Progress
            awaitItem()

            viewModel.onAction(TickersAction.SearchChange("t"))

            val item = awaitItem()

            assertEquals(listOf(btc, ltc), item.filteredTickers)
            assertEquals("t", item.searchText)

            viewModel.onAction(TickersAction.ClearSearch)

            val itemClear = awaitItem()

            assertEquals(listOf(btc, ltc), itemClear.filteredTickers)
            assertEquals("", itemClear.searchText)
        }
    }
}