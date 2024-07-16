package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.remote.RecordDataSource
import com.itp.pdbuddy.data.remote.UserDataSource
import javax.inject.Inject

import com.itp.pdbuddy.utils.Result

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource

) {
    suspend fun getUserInfo(name: String): Result<List<Map<String, Any>>> {
        return userDataSource.getUserInfo(name)
    }
}