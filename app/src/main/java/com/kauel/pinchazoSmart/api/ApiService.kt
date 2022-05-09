package com.kauel.pinchazoSmart.api

import com.kauel.pinchazoSmart.api.login.Login
import com.kauel.pinchazoSmart.api.login.ResponseDataLogin
import com.kauel.pinchazoSmart.api.login.ResponseLogin
import com.kauel.pinchazoSmart.api.portico.ListPortico
import com.kauel.pinchazoSmart.api.portico.Portico
import com.kauel.pinchazoSmart.api.portico.PorticoStatus
import com.kauel.pinchazoSmart.api.volumen.*
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

    @GET()
    suspend fun getListPortico(
        @Url ur: String
    ): ListPortico

    @POST()
    suspend fun getLogin(
        @Url ur: String,
        @Body user: Login
    ): ResponseDataLogin
}