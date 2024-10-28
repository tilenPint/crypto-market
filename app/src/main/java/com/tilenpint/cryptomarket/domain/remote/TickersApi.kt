package com.tilenpint.cryptomarket.domain.remote

import com.tilenpint.cryptomarket.domain.data.TradingPairSymbolDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TickersApi {
    @GET("tickers")
    suspend fun getTickers(
        @Query("symbols")
        symbols: String = USD + EUR + BTC
    ): List<TradingPairSymbolDto>

    companion object {
        private const val USD =
            "tBORG:USD,tBTCUSD,tETHUSD,tCHSB:USD,tLTCUSD,tXRPUSD,tDSHUSD,tRRTUSD," +
                    "tEOSUSD,tSANUSD,tDATUSD,tSNTUSD,tDOGE:USD,tLUNA:USD," +
                    "tMATIC:USD,tNEXO:USD,tOCEAN:USD,tBEST:USD,tAAVE:USD,tPLUUSD,tFILUSD"
        private const val EUR =
            "tBORG:EUR,tBTCEUR,tETHEUR,tCHSB:EUR,tLTCEUR,tXRPEUR,tDSHEUR,tRRTEUR," +
                "tEOSEUR,tSANEUR,tDATEUR,tSNTEUR,tDOGE:EUR,tLUNA:EUR," +
                "tMATIC:EUR,tNEXO:EUR,tOCEAN:EUR,tBEST:EUR,tAAVE:EUR,tPLUEUR,tFILEUR"
        private const val BTC =
            "tBORG:BTC,tETHBTC,tCHSB:BTC,tLTCBTC,tXRPBTC,tDSHBTC,tRRTBTC," +
                    "tEOSBTC,tSANBTC,tDATBTC,tSNTBTC,tDOGE:BTC,tLUNA:BTC," +
                    "tMATIC:BTC,tNEXO:BTC,tOCEAN:BTC,tBEST:BTC,tAAVE:BTC,tPLUBTC,tFILBTC"
    }
}