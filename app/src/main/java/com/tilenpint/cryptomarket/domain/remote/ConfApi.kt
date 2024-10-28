package com.tilenpint.cryptomarket.domain.remote

import com.tilenpint.cryptomarket.domain.data.CurrencySymbolDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ConfApi {
    @GET("conf/pub:{Action}:{Object}:{Details}")
    suspend fun getConf(
        @Path("Action")
        action: String = "map",
        @Path("Object")
        obj: String = "currency",
        @Path("Details")
        details: String = "label"
    ): List<CurrencySymbolDto>
}
