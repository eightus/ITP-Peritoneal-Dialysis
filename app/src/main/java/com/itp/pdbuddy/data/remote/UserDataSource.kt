package com.itp.pdbuddy.data.remote

import com.itp.pdbuddy.utils.Result

interface UserDataSource {

    suspend fun getQOTD(): Result<String>
    suspend fun getAnnouncement(): Result<String>
    suspend fun getUserInfo(name: String): Result<List<Map<String, Any>>>
    suspend fun updateUserInfo(
        name: String,
        address: String,
        phone: String,
        email: String,
        birthdate: String,
        gender: String,
        dryWeight: Float
    ): Result<Unit>
}