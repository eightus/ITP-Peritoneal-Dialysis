package com.itp.pdbuddy.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("authenticate")
    suspend fun sendData(@Body data: DataPayload): Response<Map<String, Any>>

    @POST("weight_graph")
    suspend fun getWeightGraph(@Body data: DataPayload): Response<Map<String, Any>>

    @POST("heart_rate_graph")
    suspend fun getHeartRateGraph(@Body data: DataPayload): Response<Map<String, Any>>

    @POST("blood_pressure_graph")
    suspend fun getBloodPressureGraph(@Body data: DataPayload): Response<Map<String, Any>>

    @POST("trend_analysis_graph")
    suspend fun getTrendAnalysisGraph(@Body data: DataPayload): Response<Map<String, Any>>
}

data class DataPayload(
    val token: String
)
