package com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.aisyahpn0033.qrcodegenerator.ui.theme.data.RemoteQr
import com.aisyahpn0033.qrcodegenerator.ui.theme.database.AppDatabase
import com.aisyahpn0033.qrcodegenerator.ui.theme.database.QrEntity
import com.aisyahpn0033.qrcodegenerator.ui.theme.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QrViewModel(application: Application) : AndroidViewModel(application) {

    val isLoading = MutableLiveData<Boolean>(false)
    val errorMessage = MutableLiveData<String?>(null)
    val successMessage = MutableLiveData<String?>(null)

    private val _remoteQrList = MutableLiveData<List<RemoteQr>>()
    private val qrDao = AppDatabase.getDatabase(application).qrDao()

    val qrList: LiveData<List<QrEntity>> = qrDao.getAllQrs().asLiveData()
    val deletedQrList: LiveData<List<QrEntity>> = qrDao.getDeletedQrs().asLiveData()

    init {
        fetchRemoteQrs()
    }

    fun fetchRemoteQrs() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null
            try {
                val data = RetrofitClient.api.getUserQrs()
                _remoteQrList.value = data
                saveRemoteToLocal(data)
                successMessage.value = "${data.size} data berhasil diambil dari internet."
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Tidak dapat mengambil data. Periksa koneksi internet Anda."
            } finally {
                isLoading.value = false
            }
        }
    }

    fun saveRemoteToLocal(remoteData: List<RemoteQr>) {
        viewModelScope.launch {
            remoteData.forEach { remoteQr ->
                val existingQr = qrDao.getQrById(remoteQr.id)
                if (existingQr == null) {
                    val qrEntity = QrEntity(
                        id = remoteQr.id,
                        text = remoteQr.text,
                        timestamp = remoteQr.timestamp,
                        imagePath = remoteQr.imageUrl
                    )
                    qrDao.insert(qrEntity)
                } else {
                    val updatedQr = existingQr.copy(
                        text = remoteQr.text,
                        timestamp = remoteQr.timestamp,
                        imagePath = remoteQr.imageUrl
                    )
                    qrDao.update(updatedQr)
                }
            }
        }
    }

    fun addQr(text: String, imagePath: String) {
        val qr = QrEntity(text = text, timestamp = System.currentTimeMillis(), imagePath = imagePath)
        viewModelScope.launch {
            qrDao.insert(qr)
        }
    }

    fun updateQr(qr: QrEntity) {
        viewModelScope.launch {
            qrDao.update(qr)
        }
    }

    fun softDelete(qr: QrEntity) = viewModelScope.launch {
        qrDao.update(qr.copy(isDeleted = true))
    }

    fun restore(qr: QrEntity) = viewModelScope.launch {
        qrDao.update(qr.copy(isDeleted = false))
    }

    fun clearError() {
        errorMessage.value = null
    }

    fun loadData() {
        // Data sudah otomatis di-observe melalui LiveData qrList
        // Tapi bisa digunakan untuk logika lain jika perlu
    }
    fun loadWithDelay() {
        viewModelScope.launch {
            isLoading.value = true
            delay(2000)
            loadData()
            isLoading.value = false
        }
    }
}
