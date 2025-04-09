package com.aisyahpn0033.qrcodegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme { // Pastikan AppTheme ada di package ui.theme
                AppNavigation() // Tidak perlu navController sebagai parameter
            }
        }
    }
}