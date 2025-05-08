package com.aisyahpn0033.assessment_1.ui.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(qr: QrEntity)

    @Query("SELECT * FROM qr_table WHERE isDeleted = 0 ORDER BY timestamp DESC")
    fun getAllQrs(): Flow<List<QrEntity>>

    @Update
    suspend fun update(qr: QrEntity)

    @Delete
    suspend fun delete(qr: QrEntity)

    @Query("SELECT * FROM qr_table WHERE isDeleted = 1 ORDER BY timestamp DESC")
    fun getDeletedQrs(): Flow<List<QrEntity>>

}