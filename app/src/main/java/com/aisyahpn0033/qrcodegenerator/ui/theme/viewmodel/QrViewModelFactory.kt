package com.aisyahpn0033.qrcodegenerator.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QrViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QrViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QrViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
