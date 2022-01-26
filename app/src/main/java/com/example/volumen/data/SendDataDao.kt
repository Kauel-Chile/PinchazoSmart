package com.example.volumen.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.volumen.api.volumen.SendDataVolumen

@Dao
interface SendDataDao {

    @Query("SELECT * FROM send_data")
    fun getAllSendData(): List<SendDataVolumen>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSendData(data: SendDataVolumen)

    @Query("DELETE FROM send_data")
    suspend fun deleteAllSendData()

    @Query("DELETE FROM send_data WHERE id = :id")
    suspend fun deleteSendData(id: Int)
}