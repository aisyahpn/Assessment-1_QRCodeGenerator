package com.aisyahpn0033.qrcodegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppTheme
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.QrViewModel
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.QrViewModelFactory
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.SettingsViewModel
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppThemeOption

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ViewModel untuk QR data
            val qrViewModel: QrViewModel = viewModel(
                factory = QrViewModelFactory(application)
            )

            // ViewModel untuk tema
            val settingsViewModel: SettingsViewModel = viewModel()

            // Dapatkan tema saat ini dari DataStore
            val themeOption by settingsViewModel.themeFlow.collectAsState(initial = AppThemeOption.LIGHT)

            // Bungkus dengan tema yang dipilih
            AppTheme(themeOption = themeOption) {
                AppNavigation(qrViewModel = qrViewModel, settingsViewModel = settingsViewModel)
            }
        }
    }
}