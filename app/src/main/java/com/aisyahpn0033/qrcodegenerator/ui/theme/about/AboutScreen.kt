package com.aisyahpn0033.qrcodegenerator.ui.about

// Import komponen UI dari Jetpack Compose
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aisyahpn0033.qrcodegenerator.R // Import resource drawable (logo app)
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppTheme

// Fungsi utama AboutScreen, menerima NavController untuk navigasi
@Composable
fun AboutScreen(navController: NavController) {
    // Memanggil tampilan konten dengan fungsi navigasi kembali
    AboutScreenContent(
        onBackClick = { navController.popBackStack() } // Navigasi kembali ke layar sebelumnya
    )
}

// Fungsi composable utama untuk menampilkan isi layar "About"
@Composable
fun AboutScreenContent(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize() // Mengisi seluruh layar
            .padding(24.dp), // Padding sekitar konten
        contentAlignment = Alignment.Center // Konten di tengah layar
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), // Lebar penuh
            verticalArrangement = Arrangement.spacedBy(24.dp), // Spasi antar elemen
            horizontalAlignment = Alignment.CenterHorizontally // Pusat horizontal
        ) {

            // Judul Halaman
            Text(
                text = "Tentang Aplikasi", // Judul
                style = MaterialTheme.typography.headlineSmall, // Gaya teks besar
                color = MaterialTheme.colorScheme.primary, // Warna dari tema utama
                textAlign = TextAlign.Center
            )

            // Card untuk isi deskripsi aplikasi
            Card(
                modifier = Modifier.fillMaxWidth(), // Card selebar layar
                shape = MaterialTheme.shapes.extraLarge, // Sudut bulat besar
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Elevasi
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp), // Padding isi card
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Logo aplikasi
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(120.dp)
                    )

                    // Nama aplikasi
                    Text(
                        text = "QRCode Generator",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Deskripsi, versi, dan developer
                    Text(
                        text = "Aplikasi ini dibuat untuk keperluan assessment.\n\n" +
                                "Versi: 1.0.0\n" +
                                "Developer: Aisyah Putri Nuraini",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Tombol kembali ke Home
            ElevatedButton(
                onClick = onBackClick, // Navigasi kembali ke halaman sebelumnya
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFFFF8A80), // Warna merah pastel
                    contentColor = Color.White // Warna teks putih
                )
            ) {
                Text("Kembali ke Home", fontSize = 16.sp)
            }
        }
    }
}

// Fungsi Preview untuk melihat tampilan AboutScreen di Android Studio Preview
@Preview(showSystemUi = true, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AboutScreenPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AboutScreenContent(onBackClick = {})
        }
    }
}