package com.kauel.pinchazoSmart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kauel.pinchazoSmart.api.login.ResponseDataLogin
import com.kauel.pinchazoSmart.api.login.ResponseLogin
import kotlinx.coroutines.flow.Flow

@Dao
interface LoginDao {
    @Query("SELECT * FROM response_login")
    fun getAllResponseLogin(): Flow<ResponseDataLogin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataResponseLogin(sendData: ResponseDataLogin)

    @Query("DELETE FROM response_login")
    suspend fun deleteAllDataResponseLogin()
}