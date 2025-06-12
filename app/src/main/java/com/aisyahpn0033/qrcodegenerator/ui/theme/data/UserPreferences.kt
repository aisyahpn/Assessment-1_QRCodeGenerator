package com.aisyahpn0033.qrcodegenerator.ui.theme.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension untuk membuat dataStore instance
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PHOTO = stringPreferencesKey("user_photo")
        private val USER_PASSWORD = stringPreferencesKey("user_password")   // Tambahkan ini
    }

    // ---------------------------
    // Login Status
    // ---------------------------
    suspend fun setLoginStatus(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
        }
    }

    fun getLoginStatus(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[IS_LOGGED_IN] ?: false
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }

    // ---------------------------
    // User Data
    // ---------------------------
    suspend fun saveUser(name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
        }
    }

    fun getUserName(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_NAME]
        }
    }

    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_EMAIL]
        }
    }

    // Tambahkan fungsi simpan password
    suspend fun saveUserPassword(password: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_PASSWORD] = password
        }
    }

    // Tambahkan fungsi ambil password
    fun getUserPassword(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_PASSWORD]
        }
    }

    // ---------------------------
    // Profile Photo
    // ---------------------------
    suspend fun saveUserPhoto(photoUri: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_PHOTO] = photoUri
        }
    }

    fun getUserPhoto(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_PHOTO]
        }
    }
}