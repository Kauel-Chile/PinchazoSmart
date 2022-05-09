package com.kauel.pinchazoSmart.api.volumen

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "send_data")
data class SendDataVolumen(
    @SerializedName("id_phone")
    val id_phone: String,
    @SerializedName("height")
    val height: Double? = 0.0,
    @SerializedName("length")
    val length: Double? = 0.0,
    @SerializedName("width")
    val width: Double? = 0.0,
    @SerializedName("volume")
    val volume: Double? = 0.0,
    @SerializedName("rut_operator")
    val rut: String,
    @SerializedName("date")
    val date: Date,
    @SerializedName("gate")
    val gate: String,
    @SerializedName("bar_code")
    val bardCode: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("name_image")
    val img_name: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)