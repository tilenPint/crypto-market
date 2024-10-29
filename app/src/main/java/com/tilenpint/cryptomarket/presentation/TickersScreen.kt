package com.tilenpint.cryptomarket.presentation

import android.accounts.NetworkErrorException
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tilenpint.cryptomarket.R
import com.tilenpint.cryptomarket.base.EmptyError
import com.tilenpint.cryptomarket.base.FullScreenPreview
import com.tilenpint.cryptomarket.data.TradingPairSymbol
import com.tilenpint.cryptomarket.base.LoadingStyle
import com.tilenpint.cryptomarket.base.Result
import com.tilenpint.cryptomarket.presentation.test.btcUsdTest
import com.tilenpint.cryptomarket.presentation.test.ethUsdTest
import com.tilenpint.cryptomarket.ui.ErrorBannerSwipeToDismissComponent
import com.tilenpint.cryptomarket.ui.ErrorComponent
import com.tilenpint.cryptomarket.ui.SearchField
import com.tilenpint.cryptomarket.ui.theme.AppColor
import com.tilenpint.cryptomarket.ui.theme.CryptoMarketTheme
import com.tilenpint.cryptomarket.ui.theme.Typography
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

@Composable
fun TickersScreen(
    viewModel: TickersViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle(null).value ?: return

    TickersContent(
        state = state,
        onActionSearchChange = {
            viewModel.onAction(TickersAction.SearchChange(it))
        },
        onActionSearchClear = {
            viewModel.onAction(TickersAction.ClearSearch)
        },
        onActionForceRefresh = {
            viewModel.onAction(TickersAction.ForceRefresh)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TickersContent(
    modifier: Modifier = Modifier,
    state: TickersState,
    onActionSearchChange: (String) -> Unit,
    onActionSearchClear: () -> Unit,
    onActionForceRefresh: () -> Unit
) {
    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchText,
                onValueChange = onActionSearchChange,
                clearSearch = onActionSearchClear
            )

            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                isRefreshing = (state.resultTickers as? Result.Progress)
                    ?.loadingStyle == LoadingStyle.NORMAL,
                onRefresh = onActionForceRefresh
            ) {
                when {
                    !state.filteredTickers.isNullOrEmpty() -> {
                        TickersStateContent(state = state)
                    }

                    state.resultTickers is Result.Error -> {
                        ErrorComponent(
                            e = state.resultTickers.exception,
                            onAction = onActionForceRefresh,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                        )
                    }
                }
            }
        }

        SwipeError(
            modifier = Modifier.padding(paddingValues),
            result = state.resultTickers
        )
    }
}

@Composable
private fun TickersStateContent(state: TickersState) {
    Column {
        Spacer(Modifier.size(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.filteredTickers ?: emptyList()) {
                TickerCard(it)
            }
        }
    }
}

@Composable
private fun TickerCard(symbol: TradingPairSymbol) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                symbol.mainSymbol.img?.let {
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(it),
                        contentDescription = symbol.mainSymbol.shortName
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    style = Typography.titleMedium,
                    text = symbol.mainSymbol.shortName
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Row {
                    Text(
                        style = Typography.titleMedium,
                        text = symbol.secondSymbol.symbol?.getSymbol(Locale.getDefault())
                            ?: symbol.secondSymbol.shortName
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        style = Typography.titleMedium,
                        text = stringResource(R.string.two_decimals, symbol.lastPrice)
                    )
                }
                Text(
                    style = Typography.bodyMedium,
                    text = stringResource(R.string.percentage_return, symbol.dailyChangePercent),
                    color = if (symbol.dailyChangeIsPositive) {
                        AppColor.Green
                    } else {
                        Color.Red
                    }
                )
            }
        }
    }
}


@Composable
private fun SwipeError(
    result: Result<List<TradingPairSymbol>>?,
    modifier: Modifier = Modifier
) {
    if (result !is Result.Error || result.data.isNullOrEmpty()) return
    ErrorBannerSwipeToDismissComponent(
        throwable = result.exception,
        modifier = modifier
    )
}

@Serializable
data object TickersScreenKey

@FullScreenPreview
@Composable
fun TickersContentSuccessPreview() {
    CryptoMarketTheme {
        TickersContent(
            state = TickersState(
                resultTickers = Result.Success(listOf(btcUsdTest, ethUsdTest))
            ),
            onActionSearchChange = {},
            onActionSearchClear = {},
            onActionForceRefresh = {}
        )
    }
}

@FullScreenPreview
@Composable
fun TickersContentProgressWithDataPreview() {
    CryptoMarketTheme {
        TickersContent(
            state = TickersState(
                resultTickers = Result.Progress(listOf(btcUsdTest, ethUsdTest))
            ),
            onActionSearchChange = {},
            onActionSearchClear = {},
            onActionForceRefresh = {}
        )
    }
}

@FullScreenPreview
@Composable
fun TickersContentErrorWithDataPreview() {
    CryptoMarketTheme {
        TickersContent(
            state = TickersState(
                resultTickers = Result.Error(
                    NetworkErrorException(),
                    listOf(btcUsdTest, ethUsdTest)
                )
            ),
            onActionSearchChange = {},
            onActionSearchClear = {},
            onActionForceRefresh = {}
        )
    }
}

@FullScreenPreview
@Composable
fun TickersContentProgressPreview() {
    CryptoMarketTheme {
        TickersContent(
            state = TickersState(
                resultTickers = Result.Progress()
            ),
            onActionSearchChange = {},
            onActionSearchClear = {},
            onActionForceRefresh = {}
        )
    }
}

@FullScreenPreview
@Composable
fun TickersContentErrorPreview() {
    CryptoMarketTheme {
        TickersContent(
            state = TickersState(
                resultTickers = Result.Error(EmptyError())
            ),
            onActionSearchChange = {},
            onActionSearchClear = {},
            onActionForceRefresh = {}
        )
    }
}

@FullScreenPreview
@Composable
fun TickersContentWithSearchPreview() {
    CryptoMarketTheme {
        TickersContent(
            state = TickersState(
                resultTickers = Result.Success(listOf(btcUsdTest, ethUsdTest)),
                searchText = "eth"
            ),
            onActionSearchChange = {},
            onActionSearchClear = {},
            onActionForceRefresh = {}
        )
    }
}

@FullScreenPreview
@Composable
fun TickersContentNoNetworkWithSearchPreview() {
    CryptoMarketTheme {
        TickersContent(
            state = TickersState(
                resultTickers = Result.Error(
                    NetworkErrorException(),
                    listOf(btcUsdTest, ethUsdTest)
                ),
                searchText = "eth",
            ),
            onActionSearchChange = {},
            onActionSearchClear = {},
            onActionForceRefresh = {}
        )
    }
}
