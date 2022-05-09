package com.kauel.pinchazoSmart.api.portico

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Portico(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("message")
    val message: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
