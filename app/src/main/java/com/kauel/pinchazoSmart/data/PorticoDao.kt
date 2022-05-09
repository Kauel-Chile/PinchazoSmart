package com.kauel.pinchazoSmart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kauel.pinchazoSmart.api.portico.PorticoStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PorticoDao {

    @Query("SELECT * FROM portico_status")
    fun getAllPortico(): Flow<PorticoStatus>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortico(portico: PorticoStatus)

    @Query("DELETE FROM portico_status")
    suspend fun deleteAllPortico()
}