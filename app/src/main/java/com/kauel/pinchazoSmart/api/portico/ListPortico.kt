package com.kauel.pinchazoSmart.api.portico

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "list_portico")
data class ListPortico(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("message")
    val message: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)