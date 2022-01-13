package com.example.volumen.api.volumen

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "response_data_volumen")
data class ResponseSendDataVolumen(
    @SerializedName("item")
    val item: Item,
    @SerializedName("result")
    @PrimaryKey val result: Boolean
)