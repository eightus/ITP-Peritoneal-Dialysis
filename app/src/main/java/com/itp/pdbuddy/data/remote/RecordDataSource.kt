package com.itp.pdbuddy.data.remote

import com.itp.pdbuddy.utils.Result

interface RecordDataSource {

    suspend fun submitRecord(name: String, data: List<Any>): Result<Boolean>
}