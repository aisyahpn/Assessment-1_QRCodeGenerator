package com.aisyahpn0033.qrcodegenerator.ui.theme.auth

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.aisyahpn0033.qrcodegenerator.ui.theme.data.UserPreferences
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    val photoUri = remember { mutableStateOf<Uri?>(null) }

    val name by userPrefs.getUserName().collectAsState(initial = "Nama tidak tersedia")
    val email by userPrefs.getUserEmail().collectAsState(initial = "Email tidak tersedia")
    val savedPhoto by userPrefs.getUserPhoto().collectAsState(initial = null)

    LaunchedEffect(savedPhoto) {
        imageUri = savedPhoto?.toUri()
    }

    val galleryLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            imageUri = it
            cameraBitmap = null
            scope.launch {
                userPrefs.saveUserPhoto(it.toString())
            }
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri.value?.let {
                imageUri = it
                cameraBitmap = null
                scope.launch {
                    userPrefs.saveUserPhoto(it.toString())
                }
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val photoFile = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    photoFile
                )
                photoUri.value = uri
                takePictureLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { showDialog = true },
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    when {
                        cameraBitmap != null -> {
                            Image(
                                bitmap = cameraBitmap!!.asImageBitmap(),
                                contentDescription = "Foto Kamera",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        imageUri != null -> {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Foto Galeri",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        else -> {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Foto Profil",
                                modifier = Modifier.padding(32.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
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
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tutup")
                }
                OutlinedButton(
                    onClick = { onLogout() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Logout")
                }
            }
        },
        dismissButton = {}
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Pilih Sumber Foto") },
            text = {
                Column {
                    Text("Ambil dari Galeri", modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            galleryLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                            showDialog = false
                        }
                        .padding(8.dp))
                    Text("Ambil dari Kamera", modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPermissionDialog = true
                            showDialog = false
                        }
                        .padding(8.dp))
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Izin Kamera") },
            text = { Text("Aplikasi memerlukan izin untuk menggunakan kamera. Lanjutkan?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        showPermissionDialog = false
                    }
                ) {
                    Text("Lanjutkan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}