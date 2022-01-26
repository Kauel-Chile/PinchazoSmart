package com.example.volumen.api.volumen

import com.google.gson.annotations.SerializedName

data class CodeRequest(
    @SerializedName("codigo")
    var code: String,
    @SerializedName("operator_code")
    val operatorCode: String,
    @SerializedName("portico_name")
    val porticoName: String,
    @SerializedName("id_phone")
    val idPhone: String
)

//agregar estos campos para la request
//codigo_operador, nombre_portico, id_telefono