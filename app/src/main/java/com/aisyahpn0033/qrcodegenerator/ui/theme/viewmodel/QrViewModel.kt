package com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.aisyahpn0033.qrcodegenerator.ui.theme.database.AppDatabase
import com.aisyahpn0033.qrcodegenerator.ui.theme.database.QrEntity
import kotlinx.coroutines.launch

class QrViewModel(application: Application) : AndroidViewModel(application) {

    private val qrDao = AppDatabase.getDatabase(application).qrDao()

    val qrList: LiveData<List<QrEntity>> = qrDao.getAllQrs().asLiveData()
    val deletedQrList: LiveData<List<QrEntity>> = qrDao.getDeletedQrs().asLiveData()

    fun addQr(text: String) = viewModelScope.launch {
        if (text.isNotBlank()) {
            qrDao.insert(QrEntity(text = text))
        }
    }

    fun deleteQr(qr: QrEntity) = viewModelScope.launch {
        qrDao.delete(qr)
    }

    fun updateQr(qr: QrEntity) = viewModelScope.launch {
        qrDao.update(qr)
    }

    fun softDelete(qr: QrEntity) = viewModelScope.launch {
        qrDao.update(qr.copy(isDeleted = true))
    }

    fun restore(qr: QrEntity) = viewModelScope.launch {
        qrDao.update(qr.copy(isDeleted = false))
    }
}
