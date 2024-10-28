package com.tilenpint.cryptomarket.domain.remote

import com.tilenpint.cryptomarket.domain.data.TradingPairSymbolDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TickersApi {
    @GET("tickers")
    suspend fun getTickers(
        @Query("symbols")
        symbols: String = USD
    ): List<TradingPairSymbolDto>

    companion object {
        private const val USD =
            "tBORG:USD,tBTCUSD,tETHUSD,tCHSB:USD,tLTCUSD,tXRPUSD,tDSHUSD,tRRTUSD," +
                    "tEOSUSD,tSANUSD,tDATUSD,tSNTUSD,tDOGE:USD,tLUNA:USD," +
                    "tMATIC:USD,tNEXO:USD,tOCEAN:USD,tBEST:USD,tAAVE:USD,tPLUUSD,tFILUSD"
    }
}