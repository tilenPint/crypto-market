package com.tilenpint.cryptomarket.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tilenpint.cryptomarket.R
import com.tilenpint.cryptomarket.base.ComponentPreview
import com.tilenpint.cryptomarket.ui.theme.CryptoMarketTheme

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    clearSearch: () -> Unit,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier.padding(20.dp),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search)
            )
        },
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(clearSearch) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear)
                    )
                }
            }
        } else null,
        singleLine = singleLine,
        placeholder = {
            Text(stringResource(R.string.search))
        },
        shape = CircleShape
    )
}

@ComponentPreview
@Composable
private fun SearchFieldEmptyPreview() {
    CryptoMarketTheme {
        SearchField(value = "", onValueChange = {}, clearSearch = {})
    }
}

@ComponentPreview
@Composable
private fun SearchFieldPreview() {
    CryptoMarketTheme {
        SearchField(value = "Test", onValueChange = {}, clearSearch = {})
    }
}
