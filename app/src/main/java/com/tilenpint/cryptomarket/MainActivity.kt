package com.tilenpint.cryptomarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tilenpint.cryptomarket.di.apiModule
import com.tilenpint.cryptomarket.di.presentationModule
import com.tilenpint.cryptomarket.di.networkModule
import com.tilenpint.cryptomarket.di.repositoryModule
import com.tilenpint.cryptomarket.ui.navigation.CryptoNavHost
import com.tilenpint.cryptomarket.ui.theme.CryptoMarketTheme
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            KoinApplication(
                {
                    modules(
                        presentationModule,
                        networkModule,
                        apiModule,
                        repositoryModule
                    )
                    androidContext(this@MainActivity)
                }
            ) {
                CryptoMarketTheme {
                    CryptoNavHost()
                }
            }
        }
    }
}
