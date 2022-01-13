package com.example.volumen.api.portico

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "list_portico")
data class ListPortico(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("result")
    val result: Boolean,
    @SerializedName("total")
    val total: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)