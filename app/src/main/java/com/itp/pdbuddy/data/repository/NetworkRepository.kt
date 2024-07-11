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

    suspend fun fetchGraph(data: DataPayload): Result<Map<String, Any>> {
        return try {
            val response = apiService.getGraph(data)
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