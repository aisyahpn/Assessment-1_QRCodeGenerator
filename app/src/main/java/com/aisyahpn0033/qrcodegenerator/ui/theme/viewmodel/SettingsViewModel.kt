package com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    val themeFlow: Flow<AppThemeOption> = ThemePreferenceManager.getThemeFlow(application)

    fun setTheme(option: AppThemeOption) {
        viewModelScope.launch {
            ThemePreferenceManager.saveTheme(getApplication(), option)
        }
    }
}
