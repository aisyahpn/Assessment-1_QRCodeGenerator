// Package lokasi file
package com.aisyahpn0033.qrcodegenerator.ui.theme.home

// Import library dan komponen yang dibutuhkan
import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aisyahpn0033.qrcodegenerator.QRCodeGenerator
import com.aisyahpn0033.qrcodegenerator.Screen
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppTheme
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppThemeOption
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.QrViewModel
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.QrViewModelFactory
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.SettingsViewModel

// Fungsi utama yang menampilkan HomeScreen, menerima NavController untuk navigasi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: QrViewModel = viewModel(
        factory = QrViewModelFactory(context.applicationContext as Application)
    )
    var inputText by remember { mutableStateOf("") } // Menyimpan input teks dari user
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) } // Menyimpan hasil bitmap QR
    var expanded by remember { mutableStateOf(false) } // Menentukan apakah menu dropdown terbuka
    val themeViewModel: SettingsViewModel = viewModel() // Tambahkan ViewModel tema
    val theme by themeViewModel.themeFlow.collectAsState(initial = AppThemeOption.LIGHT)
    var themeExpanded by remember { mutableStateOf(false) } // Untuk drop internal tema


    // Fungsi untuk menghasilkan QR Code dari inputText
    fun generateQR() {
        qrBitmap = QRCodeGenerator.generateQRCode(inputText)
        viewModel.addQr(inputText)

    }

    // Struktur Scaffold untuk topAppBar dan konten utama
    Scaffold(
        topBar = {
            // App Bar bagian atas layar
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    // Tombol menu dropdown
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                    }
                    // Menu dropdown untuk navigasi dan share
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("About") },
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.About.route)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Recycle Bin") },
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.RecycleBin.route)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Share") },
                            onClick = {
                                expanded = false
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Coba aplikasi ini! 🚀")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Bagikan dengan"))
                            }
                        )

                        // Menu Tema
                        DropdownMenuItem(
                            text = { Text("Tema: ${theme.displayName}") },
                            onClick = {
                                themeExpanded = !themeExpanded // buka/tutup subtema
                            }
                        )

                        // Submenu tema (dropdown di dalam dropdown)
                        if (themeExpanded) {
                            AppThemeOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            RadioButton(
                                                selected = theme == option,
                                                onClick = {
                                                    themeViewModel.setTheme(option)
                                                    themeExpanded = false
                                                    expanded = false
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(option.displayName)
                                        }
                                    },
                                    onClick = {
                                        themeViewModel.setTheme(option)
                                        themeExpanded = false
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // Konten utama layar
        HomeScreenContent(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            inputText = inputText,
            onInputChange = { inputText = it },
            qrBitmap = qrBitmap,
            onGenerateClick = {
                generateQR()
            },

            onViewListClick = {
                navController.navigate(Screen.QrList.route) // Navigasi ke QRListScreen
            }
        )
    }
}

// Fungsi utama untuk menampilkan isi layar HomeScreen
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    inputText: String,
    onInputChange: (String) -> Unit,
    qrBitmap: Bitmap?,
    onGenerateClick: () -> Unit,
    onViewListClick: () -> Unit // Tambahkan parameter untuk view list

) {
    var selectedFeedback by remember { mutableStateOf("") } // Menyimpan pilihan feedback
    val scrollState = rememberScrollState() // Mengatur scroll jika konten panjang

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Judul halaman
            Text(
                text = "Generate QR Code",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Card untuk input teks dan tombol generate
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Input teks dari user
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = onInputChange,
                        label = { Text("Masukkan teks") },
                        placeholder = { Text("Ketik sesuatu...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Tombol untuk generate QR Code
                    Button(
                        onClick = onGenerateClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Generate QR Code", style = MaterialTheme.typography.titleMedium)
                    }

                    Button(
                        onClick = onViewListClick, // Memanggil fungsi navigasi
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Lihat Daftar QR", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // Card untuk menampilkan QR Code yang sudah di-generate
            Card(
                modifier = Modifier
                    .width(220.dp)
                    .height(220.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    qrBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(170.dp)
                        )
                    } ?: Text(
                        text = "QR belum dibuat",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Form feedback dari pengguna
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pertanyaan feedback
                Text(
                    text = "Menurut Anda, apakah aplikasi ini bagus?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                // Baris pilihan feedback dengan ikon dan radio button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Pilihan "Bagus" dengan ikon jempol 👍
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFeedback == "Bagus",
                            onClick = { selectedFeedback = "Bagus" }
                        )
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Bagus",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Bagus", color = MaterialTheme.colorScheme.onSurface)
                    }

                    // Pilihan "Perlu Perbaikan" dengan ikon jempol turun 👎
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFeedback == "Perlu Perbaikan",
                            onClick = { selectedFeedback = "Perlu Perbaikan" }
                        )
                        Icon(
                            imageVector = Icons.Filled.ThumbDown,
                            contentDescription = "Perlu Perbaikan",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Perlu Perbaikan", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

        }
    }
}

// Preview tampilan HomeScreen di Android Studio
@Preview(showSystemUi = true, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme(themeOption = AppThemeOption.LIGHT) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreenContent(
                inputText = "Contoh QR",
                onInputChange = {},
                qrBitmap = null,
                onGenerateClick = {},
                onViewListClick = {}
            )
        }
    }
}

// Sumber https://composables.com/material3/dropdownmenu
// https://youtu.be/9eIhMFTs1Q8?si=8FXF0d4sN3J1EVed
// https://youtu.be/5h737wNN-qM?si=SwTeE1oD8lXmpVUo
// https://youtu.be/qbtlrGHOVjg?si=UGTM3FjI0AUJ_ID5
// https://youtu.be/YFS2EfGJBJk?si=PsKZQ4EnR7QSGnQh
// https://youtu.be/bg0AOzV4Nl4?si=8IJIbwbcvfNKs8L5