package com.tilenpint.cryptomarket.di

import com.tilenpint.cryptomarket.domain.repository.conf.ConfRepository
import com.tilenpint.cryptomarket.domain.repository.conf.ConfRepositoryImpl
import com.tilenpint.cryptomarket.domain.repository.tickers.TickersRepository
import com.tilenpint.cryptomarket.domain.repository.tickers.TickersRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<TickersRepository> { TickersRepositoryImpl(get(), get()) }
    single<ConfRepository> { ConfRepositoryImpl(get()) }
}