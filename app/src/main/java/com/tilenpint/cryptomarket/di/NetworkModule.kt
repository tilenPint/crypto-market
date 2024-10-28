package com.tilenpint.cryptomarket.di

import com.squareup.moshi.Moshi
import com.tilenpint.cryptomarket.BuildConfig
import com.tilenpint.cryptomarket.domain.adapter.TradingPairSymbolAdapter
import com.tilenpint.cryptomarket.domain.remote.TickersApi
import com.tilenpint.cryptomarket.network.NetworkConnectionObserver
import com.tilenpint.cryptomarket.network.NetworkConnectionObserverImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {
    single { get<Retrofit>().create(TickersApi::class.java) }
}

val networkModule = module {
    single<NetworkConnectionObserver> { NetworkConnectionObserverImpl(get()) }

    single {
        val moshi = Moshi.Builder()
            .add(TradingPairSymbolAdapter())
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BITFINEX_API)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}