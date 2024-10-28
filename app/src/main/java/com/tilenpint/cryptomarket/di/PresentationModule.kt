package com.tilenpint.cryptomarket.di

import com.tilenpint.cryptomarket.presentation.TickersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::TickersViewModel)
}