package com.itp.pdbuddy.data.remote

import com.itp.pdbuddy.utils.Result

interface UserDataSource {

    suspend fun getUserInfo(name: String): Result<List<Map<String, Any>>>

    suspend fun updateUserInfo(
        name: String,
        address: String,
        phone: String,
        email: String,
        birthdate: String,
        gender: String
    ): Result<Unit>
}