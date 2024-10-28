package com.tilenpint.cryptomarket.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<S, A> : ViewModel() {
    abstract val state: Flow<S>
    abstract fun onAction(action: A)
}