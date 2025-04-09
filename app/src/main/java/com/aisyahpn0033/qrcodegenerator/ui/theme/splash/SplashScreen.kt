package com.aisyahpn0033.qrcodegenerator.ui.splash

// Import elemen-elemen Compose yang dibutuhkan
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aisyahpn0033.qrcodegenerator.R
import com.aisyahpn0033.qrcodegenerator.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, isPreview: Boolean = false) {
    // Jika ini bukan tampilan Preview, maka akan ada efek delay untuk pindah ke layar Login
    if (!isPreview) {
        LaunchedEffect(Unit) {
            delay(3000) // Menunda selama 3 detik
            navController.navigate(Screen.Splash.route) {
                // Hapus Splash dari back stack agar tidak bisa kembali ke sini
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    // Tampilan latar belakang utama dari SplashScreen
    Surface(
        modifier = Modifier.fillMaxSize(), // Mengisi seluruh layar
        color = MaterialTheme.colorScheme.primary // Menggunakan warna utama dari tema
    ) {
        // Konten splash screen ditampilkan dalam kolom dan berada di tengah
        Column(
            modifier = Modifier.fillMaxSize(), // Mengisi ruang secara vertikal
            verticalArrangement = Arrangement.Center, // Posisikan item secara vertikal ke tengah
            horizontalAlignment = Alignment.CenterHorizontally // Posisikan item secara horizontal ke tengah
        ) {
            // Gambar logo aplikasi
            Image(
                painter = painterResource(id = R.drawable.splash_logo), // Mengambil gambar dari resource
                contentDescription = "Logo", // Deskripsi gambar untuk aksesibilitas
                modifier = Modifier.size(150.dp) // Ukuran gambar 150dp
            )

            // Teks nama aplikasi
            Text(
                modifier = Modifier
                    .padding(15.dp), // Memberi jarak di sekeliling teks
                text = "QRCode Generator", // Isi teks
                style = MaterialTheme.typography.titleLarge, // Gaya teks besar sesuai Material3
                color = MaterialTheme.colorScheme.onPrimary // Warna teks disesuaikan dengan latar belakang
            )
        }
    }
}

// Preview SplashScreen di Android Studio tanpa men-trigger navigasi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashScreenPreview() {
    val navController = rememberNavController() // NavController dummy untuk preview
    SplashScreen(navController = navController, isPreview = true) // Panggil splash dengan mode preview
}