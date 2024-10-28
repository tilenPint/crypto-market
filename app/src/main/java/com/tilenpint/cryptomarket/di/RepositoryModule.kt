package com.tilenpint.cryptomarket.di

import com.tilenpint.cryptomarket.domain.repository.TickersRepository
import com.tilenpint.cryptomarket.domain.repository.TickersRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<TickersRepository> { TickersRepositoryImpl(get()) }
}