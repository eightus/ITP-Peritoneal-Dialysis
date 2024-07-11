package com.itp.pdbuddy.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("authenticate")
    suspend fun sendData(@Body data: DataPayload): Response<Map<String, Any>>

    @POST("weight_graph")
    suspend fun getGraph(@Body data: DataPayload): Response<Map<String, Any>>
}

data class DataPayload(
    val token: String
)
