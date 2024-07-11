package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.remote.AuthDataSource
import com.itp.pdbuddy.data.remote.firebase.FirebaseAuthDataSource
import javax.inject.Inject
import com.itp.pdbuddy.utils.Result

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource
) {
    suspend fun login(email: String, password: String): Result<Boolean> {
        return authDataSource.login(email, password)
    }

    suspend fun logout(): Result<Boolean> {
        return authDataSource.logout()
    }

    suspend fun isLoggedIn(): Result<Boolean> {
        return authDataSource.isLoggedIn()
    }

    suspend fun getUsername(): Result<String?> {
        return authDataSource.getUsername()
    }

    suspend fun updateDisplayName(displayName: String): Result<Boolean> {
        return authDataSource.updateDisplayName(displayName)
    }

    suspend fun getIdToken(): Result<String?> {
        return authDataSource.getIdToken()
    }
}