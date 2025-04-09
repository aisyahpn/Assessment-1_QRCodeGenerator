package com.aisyahpn0033.qrcodegenerator.ui.theme

// Import MaterialTheme dari Material3 dan anotasi Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// Fungsi AppTheme adalah fungsi composable yang digunakan untuk membungkus seluruh UI dengan tema Material3
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    // MaterialTheme menyediakan tema warna, tipografi, dan bentuk default dari Material Design
    MaterialTheme {
        // content() adalah composable content yang akan diberi tema
        content()
    }
}