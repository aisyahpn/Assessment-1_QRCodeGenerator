package com.aisyahpn0033.qrcodegenerator.ui.theme.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_table")
data class QrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imagePath: String? = null,
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false

)

