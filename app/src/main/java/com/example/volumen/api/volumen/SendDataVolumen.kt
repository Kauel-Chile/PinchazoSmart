package com.example.volumen.api.volumen

import com.google.gson.annotations.SerializedName
import java.util.*

data class SendDataVolumen(
    @SerializedName("id_phone")
    val idPhone: String,
    @SerializedName("high")
    val high: Double? = 0.0,
    @SerializedName("long")
    val long: Double? = 0.0,
    @SerializedName("width")
    val width: Double? = 0.0,
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
    val status: String
)