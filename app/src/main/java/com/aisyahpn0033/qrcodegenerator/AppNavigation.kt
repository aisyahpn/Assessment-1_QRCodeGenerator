package com.aisyahpn0033.qrcodegenerator

// Import Composable & Navigasi dari Jetpack Compose
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Import screen-screen dari folder UI
import com.aisyahpn0033.qrcodegenerator.ui.theme.splash.SplashScreen
import com.aisyahpn0033.qrcodegenerator.ui.theme.home.HomeScreen
import com.aisyahpn0033.qrcodegenerator.ui.theme.about.AboutScreen
import com.aisyahpn0033.qrcodegenerator.ui.theme.home.QRListScreen
import com.aisyahpn0033.qrcodegenerator.ui.theme.recycle.RecycleBinScreen
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.QrViewModel
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.QrViewModelFactory
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.SettingsViewModel

// Fungsi utama navigasi aplikasi
@Composable
fun AppNavigation(
    qrViewModel: QrViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.About.route) {
            AboutScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.QrList.route) {
            QRListScreen(viewModel = qrViewModel)
        }
        composable(Screen.RecycleBin.route) {
            val viewModel: QrViewModel = viewModel(
                factory = QrViewModelFactory(LocalContext.current.applicationContext as Application)
            )
            RecycleBinScreen(viewModel)
        }

    }
}


// Sealed class berisi semua screen dan route-nya
sealed class Screen(val route: String) {

    // Splash screen route
    data object Splash : Screen("splash")

    // Home screen route, dengan parameter userName (opsional di sini)
    data object Home : Screen("home/{userName}")

    // About screen route
    data object About : Screen("about")

    data object QrList : Screen("qr_list")

    data object RecycleBin : Screen("recycle_bin")


}