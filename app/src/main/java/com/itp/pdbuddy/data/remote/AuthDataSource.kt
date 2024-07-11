package com.itp.pdbuddy.data.remote

import com.itp.pdbuddy.utils.Result

interface AuthDataSource {
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun logout(): Result<Boolean>
    suspend fun isLoggedIn(): Result<Boolean>
    suspend fun getUsername(): Result<String?>
    suspend fun updateDisplayName(displayName: String): Result<Boolean>
    suspend fun getIdToken(): Result<String?>
}