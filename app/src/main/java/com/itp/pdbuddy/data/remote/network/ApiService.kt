package com.itp.pdbuddy.data.remote.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("posts")
    suspend fun sendData(@Body data: DataPayload): Response<Map<String, Any>>
}

data class DataPayload(
    val title: String,
    val body: String,
    val userId: Int
)