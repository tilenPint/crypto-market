package com.tilenpint.cryptomarket.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {
    fun format(value: Float, setCurrency: Currency = usdCurrency): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            currency = setCurrency
        }

        return format.format(value)
    }

    private val usdCurrency = Currency.getInstance("USD")
}
