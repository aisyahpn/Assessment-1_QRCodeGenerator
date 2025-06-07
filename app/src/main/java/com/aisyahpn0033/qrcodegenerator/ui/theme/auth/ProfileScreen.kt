package com.aisyahpn0033.qrcodegenerator.ui.theme.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.core.net.toUri
import kotlinx.coroutines.launch
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import com.aisyahpn0033.qrcodegenerator.ui.theme.data.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    // Ambil data nama & email dari UserPreferences, dengan default string
    val name by userPrefs.getUserName().collectAsState(initial = "Nama tidak tersedia")
    val email by userPrefs.getUserEmail().collectAsState(initial = "Email tidak tersedia")

    // Ambil foto profil tersimpan (string Uri), lalu konversi ke Uri
    val savedPhoto by userPrefs.getUserPhoto().collectAsState(initial = null)

    // Saat savedPhoto berubah, update imageUri
    LaunchedEffect(savedPhoto) {
        imageUri = savedPhoto?.toUri()
    }

    // Launcher pilih gambar dari galeri
    val launcher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            scope.launch {
                userPrefs.saveUserPhoto(it.toString())
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profil") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .size(120.dp)
                    .clickable {
                        launcher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    },
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Foto Profil",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto Profil",
                        modifier = Modifier.padding(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = name.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = email.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
