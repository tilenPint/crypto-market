package com.tilenpint.cryptomarket.ui

import android.accounts.NetworkErrorException
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tilenpint.cryptomarket.R
import com.tilenpint.cryptomarket.base.ComponentPreview
import com.tilenpint.cryptomarket.base.EmptyError
import com.tilenpint.cryptomarket.ui.theme.CryptoMarketTheme
import com.tilenpint.cryptomarket.ui.theme.Typography

@Composable
fun ErrorComponent(e: Throwable, onAction: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(128.dp),
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(R.string.warning)
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            style = Typography.titleLarge,
            text = stringResource(
                when (e) {
                    is NetworkErrorException -> R.string.error_no_network_title
                    else -> R.string.error_title
                }
            )
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            style = Typography.bodyLarge,
            text = stringResource(
                when (e) {
                    is NetworkErrorException -> R.string.error_no_network
                    is EmptyError -> R.string.error_empty
                    else -> R.string.error_unknown
                }
            ),
            textAlign = TextAlign.Center
        )

        onAction?.let {
            Spacer(modifier = Modifier.size(32.dp))

            Button(onClick = it) {
                Text(stringResource(R.string.try_again))
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ErrorComponentPreview() {
    CryptoMarketTheme {
        ErrorComponent(e = NetworkErrorException())
    }
}

@ComponentPreview
@Composable
private fun ErrorComponentButtonPreview() {
    CryptoMarketTheme {
        ErrorComponent(e = UnknownError(), onAction = {})
    }
}
