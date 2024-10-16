package com.itp.pdbuddy.data.remote

import com.itp.pdbuddy.utils.Result

interface RecordDataSource {

    suspend fun submitRecord(name: String, data: List<Any>, base64Image: String?): Result<Boolean>

    suspend fun getRecords(name: String): Result<List<Map<String, Any>>>
}