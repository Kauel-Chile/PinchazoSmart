package com.example.volumen.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.volumen.api.volumen.ResponseSendDataVolumen
import kotlinx.coroutines.flow.Flow

@Dao
interface DataVolumenDao {
    @Query("SELECT * FROM response_data_volumen")
    fun getAllDataVolumen(): Flow<ResponseSendDataVolumen>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataVolumen(sendData: ResponseSendDataVolumen)

    @Query("DELETE FROM response_data_volumen")
    suspend fun deleteAllDataVolumen()
}