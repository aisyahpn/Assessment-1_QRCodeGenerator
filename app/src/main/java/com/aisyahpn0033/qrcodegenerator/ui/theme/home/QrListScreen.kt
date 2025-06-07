package com.aisyahpn0033.qrcodegenerator.ui.theme.home

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aisyahpn0033.qrcodegenerator.R
import com.aisyahpn0033.qrcodegenerator.ui.theme.database.QrEntity
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppTheme
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppThemeOption
import com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel.QrViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import com.aisyahpn0033.qrcodegenerator.ui.theme.network.isNetworkAvailable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRListScreen(viewModel: QrViewModel) {
    val qrList by viewModel.qrList.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(true) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedQr by remember { mutableStateOf<QrEntity?>(null) }
    var editedText by remember { mutableStateOf("") }
    var isGridMode by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isConnected = isNetworkAvailable(context)
        if (isConnected) {
            viewModel.loadWithDelay()
        }
    }

    // Tampilkan Snackbar saat error terjadi
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearError() // buat fungsi clear error di viewmodel
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar QR Code") },
                actions = {
                    IconButton(onClick = { isGridMode = !isGridMode }) {
                        Icon(
                            imageVector = if (isGridMode) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = "Ganti tampilan"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                !isConnected -> {
                    // Tidak ada koneksi, tampilkan loading terus-menerus
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Menunggu koneksi internet...")
                        }
                    }
                }

                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    QRListContent(
                        qrList = qrList,
                        modifier = Modifier.fillMaxSize(),
                        isGrid = isGridMode,
                        onEdit = {
                            selectedQr = it
                            editedText = it.text
                            showEditDialog = true
                        },
                        onDelete = { qr ->
                            selectedQr = qr
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        // Dialog Hapus
        if (showDeleteDialog && selectedQr != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Yakin ingin menghapus data ini?") },
                confirmButton = {
                    TextButton(onClick = {
                        selectedQr?.let { qr ->
                            coroutineScope.launch {
                                viewModel.softDelete(qr)
                                val result = snackbarHostState.showSnackbar(
                                    message = "QR Code dihapus",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.restore(qr)
                                }
                            }
                        }
                        showDeleteDialog = false
                        selectedQr = null
                    }) {
                        Text("Ya")
                    }
                }
            )
        }

        // Dialog Edit
        if (showEditDialog && selectedQr != null) {
            EditQrDialog(
                qr = selectedQr!!,
                onEdit = { updatedQr ->
                    viewModel.updateQr(updatedQr)
                    showEditDialog = false
                },
                onDismiss = { showEditDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QRListContent(
    qrList: List<QrEntity>,
    modifier: Modifier = Modifier,
    isGrid: Boolean,
    onEdit: (QrEntity) -> Unit,
    onDelete: (QrEntity) -> Unit
) {
    if (isGrid) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // ✅ Gunakan items(count) dan akses list pakai index
            items(qrList.size) { index ->
                val qr = qrList[index]
                QrCard(qr = qr, isGrid = isGrid, onEdit = onEdit, onDelete = onDelete)

            }
        }
    } else {
        LazyColumn(modifier = modifier) {
            // ✅ Gunakan items dari LazyColumn secara langsung
            items(qrList.size) { index ->
                val qr = qrList[index]
                QrCard(qr = qr, isGrid = isGrid, onEdit = onEdit, onDelete = onDelete)

            }
        }
    }
}

@Composable
fun EditQrDialog(
    qr: QrEntity,
    onEdit: (QrEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var editedText by remember { mutableStateOf(qr.text) }
    var selectedImagePath by remember { mutableStateOf(qr.imagePath) }
    var tempCameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && tempCameraImageUri != null) {
                val path = copyUriToInternalStorage(context, tempCameraImageUri!!)
                if (path != null) {
                    selectedImagePath = path
                }
            }
        }
    )

    // Launcher permission kamera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            val uri = createImageUri(context)
            if (uri != null) {
                tempCameraImageUri = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Gagal membuat URI untuk kamera", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Launcher galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val path = copyUriToInternalStorage(context, it)
                if (path != null) {
                    selectedImagePath = path
                }
            }
        }
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit QR Code") },
        text = {
            Column {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    label = { Text("Teks Baru") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tombol Ambil Foto + Pilih dari Galeri
                Row {
                    Button(onClick = {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }) {
                        Text("Ambil Foto")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        galleryLauncher.launch("image/*")
                    }) {
                        Text("Pilih dari Galeri")
                    }
                }

                selectedImagePath?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = File(it),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                        error = painterResource(id = R.drawable.ic_broken_image)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedQr = qr.copy(text = editedText, imagePath = selectedImagePath)
                onEdit(updatedQr)
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Batal")
            }
        }
    )
}



@Composable
fun QrCard(

    qr: QrEntity,
    isGrid: Boolean,
    onEdit: (QrEntity) -> Unit,
    onDelete: (QrEntity) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Teks: ${qr.text}")
            Text("Waktu: ${Date(qr.timestamp)}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))

            qr.imagePath?.let { path ->
                val model = if (path.startsWith("http")) path else File(path)

                AsyncImage(
                    model = model,
                    contentDescription = "QR Code Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                    error = painterResource(id = R.drawable.ic_broken_image)
                )

            }


            if (isGrid) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(onClick = { onEdit(qr) }) {
                        Text("Edit")
                    }
                    Button(
                        onClick = { onDelete(qr) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Hapus")
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onEdit(qr) }) {
                        Text("Edit")
                    }
                    Button(
                        onClick = { onDelete(qr) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Hapus")
                    }
                }
            }
        }
    }
}
fun createImageUri(context: Context): Uri? {
    val contentValues = android.content.ContentValues().apply {
        put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "camera_image_${System.currentTimeMillis()}.jpg")
        put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    return context.contentResolver.insert(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
}

fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): String? {
    val fileName = "qr_${System.currentTimeMillis()}.png"
    return try {
        val file = File(context.filesDir, fileName)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun copyUriToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "qr_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        outputStream.flush()
        outputStream.close()
        inputStream?.close()
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }

}


@Preview(showSystemUi = true, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun QRListContentPreview() {
    val dummyList = listOf(
        QrEntity(id = 1, text = "Contoh QR 1", timestamp = System.currentTimeMillis()),
        QrEntity(id = 2, text = "Contoh QR 2", timestamp = System.currentTimeMillis())
    )

    AppTheme(themeOption = AppThemeOption.LIGHT) {
        Surface(modifier = Modifier.fillMaxSize()) {
            QRListContent(
                qrList = dummyList,
                isGrid = false,
                modifier = Modifier.padding(8.dp),
                onEdit = {},
                onDelete = {}
            )
        }
    }
}
