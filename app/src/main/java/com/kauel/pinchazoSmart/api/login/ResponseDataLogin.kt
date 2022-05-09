package com.kauel.pinchazoSmart.api.login

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "response_login")
data class ResponseDataLogin(
    @SerializedName("data")
    val data: DataLogin,
    @SerializedName("message")
    val message: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)