package com.tilenpint.cryptomarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tilenpint.cryptomarket.presentation.TickersScreen
import com.tilenpint.cryptomarket.presentation.TickersScreenKey

@Composable
fun CryptoNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TickersScreenKey
    ) {
        composable<TickersScreenKey>{
            TickersScreen()
        }
    }
}
