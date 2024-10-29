package com.tilenpint.cryptomarket.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.tilenpint.cryptomarket.base.NoNetwork
import com.tilenpint.cryptomarket.base.SearchEmptyError
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
            imageVector = when (e) {
                is SearchEmptyError -> Icons.Default.Search
                else -> Icons.Default.Warning
            },
            contentDescription = stringResource(
                when (e) {
                    is SearchEmptyError -> R.string.search
                    else -> R.string.warning
                }
            )
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            style = Typography.titleLarge,
            text = stringResource(
                when (e) {
                    is NoNetwork -> R.string.error_no_network_title
                    is SearchEmptyError -> R.string.search_empty_data_title
                    else -> R.string.error_title
                }
            )
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            style = Typography.bodyLarge,
            text = stringResource(
                when (e) {
                    is NoNetwork -> R.string.error_no_network
                    is EmptyError -> R.string.error_empty
                    is SearchEmptyError -> R.string.search_empty_data_desc
                    else -> R.string.error_unknown
                }
            ),
            textAlign = TextAlign.Center
        )

        onAction?.let {
            Spacer(modifier = Modifier.size(32.dp))

            Button(onClick = it) {
                Text(
                    stringResource(
                        when (e) {
                            is SearchEmptyError -> R.string.clear_search
                            else -> R.string.try_again
                        }
                    )
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ErrorNoNetworkComponentPreview() {
    CryptoMarketTheme {
        ErrorComponent(e = NoNetwork())
    }
}

@ComponentPreview
@Composable
private fun ErrorSearchEmptyComponentPreview() {
    CryptoMarketTheme {
        ErrorComponent(e = SearchEmptyError())
    }
}

@ComponentPreview
@Composable
private fun ErrorComponentButtonPreview() {
    CryptoMarketTheme {
        ErrorComponent(e = UnknownError(), onAction = {})
    }
}
