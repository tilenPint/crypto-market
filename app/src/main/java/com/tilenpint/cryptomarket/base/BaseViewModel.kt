package com.tilenpint.cryptomarket.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<S, A> : ViewModel() {
    abstract val state: StateFlow<S>
    abstract fun onAction(action: A)
}