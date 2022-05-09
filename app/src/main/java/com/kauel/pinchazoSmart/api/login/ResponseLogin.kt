package com.kauel.pinchazoSmart.api.login

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "response_login")
data class ResponseLogin(
    @SerializedName("token")
    @PrimaryKey val token: String
)
