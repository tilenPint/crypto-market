package com.tilenpint.cryptomarket.data

import androidx.annotation.DrawableRes
import com.tilenpint.cryptomarket.R
import java.util.Currency

data class CurrencySymbol(
    val shortName: String,
    val fullName: String? = null
) {
    val symbol: Currency? = try {
        Currency.getInstance(shortName)
    } catch (e: Exception) {
        null
    }

    @DrawableRes
    val img: Int? = when (shortName.lowercase()) {
        "borg" -> R.drawable.img_borg
        "btc" -> R.drawable.img_btc
        "eth" -> R.drawable.img_eth
        "xrp" -> R.drawable.img_xrp
        else -> null
    }
}