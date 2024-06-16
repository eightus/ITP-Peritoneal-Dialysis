package com.itp.pdbuddy.data.remote.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.itp.pdbuddy.data.remote.AuthDataSource
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor() : AuthDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val messaging: FirebaseMessaging = FirebaseMessaging.getInstance()


    override suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.currentUser?.let {
                updateUsernameAndFCMToken(it.uid)
            }
            Result.Success(true)
        } catch (e: Exception) {
            Log.d("Login", "Error")
            e.message?.let { Log.d("Login", it) }
            Result.Failure(e)
        }
    }

    override suspend fun logout(): Result<Boolean> {
        return try {
            auth.signOut()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun isLoggedIn(): Result<Boolean> {
        return try {
            val isLoggedIn = auth.currentUser != null
            Result.Success(isLoggedIn)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getUsername(): Result<String?> {
        return try {
            val user = auth.currentUser
            val displayName = user?.displayName
            if (displayName != null) {
                Log.d("getUsername", displayName)
            }
            Result.Success(displayName)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun updateDisplayName(displayName: String): Result<Boolean> {
        return try {
            val user = auth.currentUser
            user?.let {
                val profileUpdates = userProfileChangeRequest {
                    this.displayName = displayName
                }
                it.updateProfile(profileUpdates).await()
                Result.Success(true)
            } ?: Result.Failure(Exception("User not logged in"))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    private suspend fun updateUsernameAndFCMToken(uid: String) {
        try {
            val userDoc = firestore.collection("users").document(uid).get().await()
            val username = userDoc.getString("username")

            if (username != null) {
                updateDisplayName(username)
            }

            messaging.token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    token?.let {
                        firestore.collection("users").document(uid).update("fcmToken", it)
                    }
                } else {
                    Log.d("FCM", "Fetching FCM token failed", task.exception)
                }
            }
        } catch (e: Exception) {
            Log.d("Firestore", "Error retrieving user data", e)
        }
    }
}