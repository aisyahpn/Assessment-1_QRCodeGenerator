package com.aisyahpn0033.qrcodegenerator.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit // ✅ This line was missing
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ✅ Diletakkan di luar object
val Context.dataStore by preferencesDataStore(name = "settings")

object ThemePreferenceManager {
    private val THEME_KEY = stringPreferencesKey("theme_option")

    suspend fun saveTheme(context: Context, option: AppThemeOption) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = option.name
        }
    }

    fun getThemeFlow(context: Context): Flow<AppThemeOption> {
        return context.dataStore.data.map { preferences ->
            val name = preferences[THEME_KEY] ?: AppThemeOption.SYSTEM.name
            AppThemeOption.valueOf(name)
        }
    }
}