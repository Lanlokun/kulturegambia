package com.example.kulturgambia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.kulturgambia.ui.KulturGambiaApp
import com.example.kulturgambia.ui.theme.KulturGambiaTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // keep this before super.onCreate
        super.onCreate(savedInstanceState)
        setContent {
            KulturGambiaTheme {
                KulturGambiaApp()
            }
        }

    }
}
