package com.tilenpint.cryptomarket.ui

import android.accounts.NetworkErrorException
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tilenpint.cryptomarket.R
import com.tilenpint.cryptomarket.base.ComponentPreview
import com.tilenpint.cryptomarket.ui.theme.CryptoMarketTheme

@Composable
fun ErrorBannerSwipeToDismissComponent(
    throwable: Throwable,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        backgroundContent = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Red)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.warning)
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(
                    if (throwable is NetworkErrorException) {
                        R.string.error_no_network_data
                    } else {
                        R.string.error_with_data
                    }
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ErrorBannerSwipeToDismissComponentPreview() {
    CryptoMarketTheme {
        ErrorBannerSwipeToDismissComponent(throwable = NetworkErrorException())
    }
}

@ComponentPreview
@Composable
private fun UnknownErrorBannerSwipeToDismissComponentPreview() {
    CryptoMarketTheme {
        ErrorBannerSwipeToDismissComponent(throwable = UnknownError())
    }
}