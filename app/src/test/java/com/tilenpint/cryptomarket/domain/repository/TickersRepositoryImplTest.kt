package com.tilenpint.cryptomarket.domain.repository

import app.cash.turbine.test
import com.tilenpint.cryptomarket.domain.remote.TickersApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.tilenpint.cryptomarket.base.Result
import com.tilenpint.cryptomarket.fake.tBTCUSD
import com.tilenpint.cryptomarket.fake.tLTCUSD
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class TickersRepositoryImplTest {

    private lateinit var tickersRepository: TickersRepositoryImpl
    private val tickersApi: TickersApi = mockk()

    @BeforeEach
    fun setUp() {
        tickersRepository = TickersRepositoryImpl(tickersApi)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test successful loadTickers emits Success result`() = runTest {
        val mockData = listOf(
            tBTCUSD, tLTCUSD
        )
        coEvery { tickersApi.getTickers() } returns mockData

        tickersRepository.tickers.test {
            val result = awaitItem()
            assert(result is Result.Progress)
        }

        tickersRepository.loadTickers()

        tickersRepository.tickers.test {
            val result = awaitItem()
            assert(result is Result.Success)
            val successData = (result as Result.Success).data
            assertEquals(2, successData.size)
            assertEquals(tBTCUSD.symbol, successData[0].symbol)
            assertEquals(tLTCUSD.symbol, successData[1].symbol)
        }
    }

    @Test
    fun `test loadTickers emits Error result on exception`() = runTest {
        val exception = Exception("API error")
        coEvery { tickersApi.getTickers() } throws exception

        tickersRepository.loadTickers()

        tickersRepository.tickers.test {
            val result = awaitItem()
            assert(result is Result.Error)
            val error = result as Result.Error
            assertEquals(exception, error.exception)
        }
    }

    @Test
    fun `test loadTickers emits Progress result initially`() = runTest {
        coEvery { tickersApi.getTickers() } returns emptyList()

        tickersRepository.tickers.test {
            tickersRepository.loadTickers()
            assertTrue(awaitItem() is Result.Progress)

            cancelAndConsumeRemainingEvents()
        }
    }
}