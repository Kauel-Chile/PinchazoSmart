package com.example.volumen.api.volumen

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "send_data")
data class SendDataVolumen(
    @SerializedName("id_phone")
    val idPhone: String,
    @SerializedName("high")
    val highh: Double? = 0.0,
    @SerializedName("long")
    val longg: Double? = 0.0,
    @SerializedName("width")
    val widthh: Double? = 0.0,
    @SerializedName("volume")
    val volumen: Double? = 0.0,
    @SerializedName("rut_operator")
    val rut: String,
    @SerializedName("date")
    val date: Date,
    @SerializedName("portico")
    val portico: String,
    @SerializedName("bar_code")
    val bardCode: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("name_image")
    val img_name: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

//Agregar campo img_name