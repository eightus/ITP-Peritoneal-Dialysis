package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.remote.UserDataSource
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {

    suspend fun getUserInfo(name: String): Result<List<Map<String, Any>>> {
        return userDataSource.getUserInfo(name)
    }

    suspend fun updateUserInfo(
        name: String,
        address: String,
        phone: String,
        email: String,
        birthdate: String,
        gender: String
    ): Result<Unit> {
        return userDataSource.updateUserInfo(name, address, phone, email, birthdate, gender)
    }
}
