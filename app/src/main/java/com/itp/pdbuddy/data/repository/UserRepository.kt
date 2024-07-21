package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.remote.UserDataSource
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {
    suspend fun getQOTD(): Result<String> {
        return userDataSource.getQOTD()
    }

    suspend fun getAnnouncement(): Result<String> {
        return userDataSource.getAnnouncement()
    }

    suspend fun getUserInfo(name: String): Result<List<Map<String, Any>>> {
        return userDataSource.getUserInfo(name)
    }

    suspend fun updateUserInfo(
        name: String,
        address: String,
        phone: String,
        email: String,
        birthdate: String,
        gender: String,
        dryWeight: Float
    ): Result<Unit> {
        return userDataSource.updateUserInfo(name, address, phone, email, birthdate, gender, dryWeight)
    }
}
