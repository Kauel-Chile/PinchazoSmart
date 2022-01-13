package com.example.volumen.api

import com.example.volumen.api.portico.ListPortico
import com.example.volumen.api.portico.PorticoStatus
import com.example.volumen.api.volumen.*
import retrofit2.http.*

interface ApiService {

    @POST()
    suspend fun getPorticoStatus(
        @Url url: String,
        @Body body: Any = Object()
    ): PorticoStatus

    @POST()
    suspend fun getVolumen(
        @Url url: String,
        @Body body: CodeRequest
    ): Volumen

    @POST()
    suspend fun sendDataVolumen(
        @Url ur: String,
        @Body body: SendDataVolumen
    ): ResponseSendDataVolumen

    @POST()
    suspend fun getListPortico(
        @Url ur: String,
        @Body body: Any = Object()
    ): ListPortico

}