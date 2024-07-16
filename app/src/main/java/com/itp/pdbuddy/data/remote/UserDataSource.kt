package com.itp.pdbuddy.data.remote

import com.itp.pdbuddy.utils.Result

interface UserDataSource {

    suspend fun getUserInfo(name: String): Result<List<Map<String, Any>>>

}