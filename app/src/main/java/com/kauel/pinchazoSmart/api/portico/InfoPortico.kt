package com.kauel.pinchazoSmart.api.portico

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "info_portico")
data class InfoPortico(
    @SerializedName("IPv4")
    val IPv4: String,
    @SerializedName("codigo")
    val codigo: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("status")
    val status: String
)