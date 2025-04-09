package com.aisyahpn0033.qrcodegenerator

// Import Composable & Navigasi dari Jetpack Compose
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Import screen-screen dari folder UI
import com.aisyahpn0033.qrcodegenerator.ui.splash.SplashScreen

// Fungsi utama navigasi aplikasi
@Composable
fun AppNavigation() {
    val navController = rememberNavController() // Membuat NavController untuk mengatur navigasi

    // NavHost: tempat semua route didefinisikan, dimulai dari splash screen
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        // Rute ke SplashScreen
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
    }
}

// Sealed class berisi semua screen dan route-nya
sealed class Screen(val route: String) {

    // Splash screen route
    data object Splash : Screen("splash")
}