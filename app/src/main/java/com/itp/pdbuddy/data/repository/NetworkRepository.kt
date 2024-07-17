package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.remote.api.ApiService
import com.itp.pdbuddy.data.remote.api.DataPayload
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun sendData(data: DataPayload): Result<Map<String, Any>> {
        return try {
            val response = apiService.sendData(data)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Failure(Exception("Response body is null"))
                }
            } else {
                Result.Failure(Exception("Failed to send data: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun fetchGraph(data: DataPayload, graphType: String): Result<Map<String, Any>> {
        return try {

            val response = when (graphType) {
                "My Weight" -> apiService.getWeightGraph(data)
                "My Heart Rate" -> apiService.getHeartRateGraph(data)
                "My Blood Pressure" -> apiService.getBloodPressureGraph(data)
                "Summary" -> apiService.getTrendAnalysisGraph(data)
                else -> throw IllegalArgumentException("Unknown graph type: $graphType")
            }
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Failure(Exception("Response body is null"))
                }
            } else {
                Result.Failure(Exception("Failed to send data: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

}