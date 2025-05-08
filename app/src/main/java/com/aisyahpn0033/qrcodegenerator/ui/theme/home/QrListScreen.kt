package com.aisyahpn0033.assessment_1.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aisyahputrinuraini.assessment_1.ui.database.QrEntity
import com.aisyahputrinuraini.assessment_1.ui.theme.AppTheme
import com.aisyahputrinuraini.assessment_1.ui.theme.AppThemeOption
import com.aisyahputrinuraini.assessment_1.ui.viewmodel.QrViewModel
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRListScreen(viewModel: QrViewModel) {
    val qrList by viewModel.qrList.observeAsState(emptyList())
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedQr by remember { mutableStateOf<QrEntity?>(null) }
    var editedText by remember { mutableStateOf("") }
    var isGridMode by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()



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
        snackbarHost = { SnackbarHost(snackbarHostState) } // ⬅️ ini penting untuk menampilkan Undo
    ) { padding ->
        QRListContent(
                qrList = qrList,
                modifier = Modifier.padding(padding),
                isGrid = isGridMode,
                onEdit = {
                    selectedQr = it
                    editedText = it.text
                    showEditDialog = true
                },
            onDelete = { qr ->
                selectedQr = qr
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
        )

        // 🔴 Dialog Konfirmasi Hapus
        if (showDeleteDialog && selectedQr != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Yakin ingin menghapus data ini?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteQr(selectedQr!!)
                        showDeleteDialog = false
                        selectedQr = null
                    }) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        selectedQr = null
                    }) {
                        Text("Batal")
                    }
                }
            )
        }

        // ✏️ Dialog Edit QR
        if (showEditDialog && selectedQr != null) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit QR Code") },
                text = {
                    OutlinedTextField(
                        value = editedText,
                        onValueChange = { editedText = it },
                        label = { Text("Teks Baru") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val updatedQr = selectedQr!!.copy(text = editedText)
                        viewModel.updateQr(updatedQr)
                        showEditDialog = false
                        selectedQr = null
                    }) {
                        Text("Simpan")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showEditDialog = false
                        selectedQr = null
                    }) {
                        Text("Batal")
                    }
                }
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
fun QrCard(qr: QrEntity, isGrid: Boolean, onEdit: (QrEntity) -> Unit, onDelete: (QrEntity) -> Unit)
 {
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

