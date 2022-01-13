package com.example.volumen.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.volumen.api.portico.ListPortico
import kotlinx.coroutines.flow.Flow

@Dao
interface ListPorticoDao {

    @Query("SELECT * FROM list_portico")
    fun getAllListPortico(): Flow<ListPortico>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListPortico(listPortico: ListPortico)

    @Query("DELETE FROM list_portico")
    suspend fun deleteAllListPortico()
}