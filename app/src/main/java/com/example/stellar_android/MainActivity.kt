package com.example.stellar_android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import com.example.stellar_android.navigation.StellarNavigation
import com.example.stellar_android.ui.theme.Stellar_AndroidTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan SplashScreen
        val splashScreen = installSplashScreen()

        // Menyesuaikan status bar dan navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, true)

        // Menyembunyikan ActionBar
        supportActionBar?.hide()

        // Tunda untuk beberapa detik sebelum melanjutkan ke aktivitas utama
        splashScreen.setKeepOnScreenCondition { false }
        Handler(Looper.getMainLooper()).postDelayed({
            setContent {
                Stellar_AndroidTheme {
                    Scaffold(
                        containerColor = Color.Black,
                        contentWindowInsets = WindowInsets.systemBars, // Perbaikan padding
                    ) { innerPadding ->
                        StellarNavigation(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding) // Sesuaikan padding
                        )
                    }
                }
            }
            window.statusBarColor = android.graphics.Color.BLACK // Status bar hitam
            window.navigationBarColor = android.graphics.Color.BLACK
        }, 2000) // Tunda selama 2 detik untuk menampilkan splash screen
    }
}
