package com.itp.pdbuddy.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.data.remote.UserDataSource
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(): UserDataSource {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUserInfo(name: String): Result<List<Map<String, Any>>> {
        return try {
            val querySnapshot = db.collection("users")
                .whereEqualTo("username", name) // Assuming username is unique
                .get()
                .await()

            val records = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data ?: emptyMap()
            }

            Result.Success(records)
        } catch (e: Exception) {
            Log.e("FirebaseUserDataSource", "Error fetching user info", e)
            Result.Failure(e)
        }
    }

    override suspend fun updateUserInfo(
        name: String,
        address: String,
        phone: String,
        email: String,
        birthdate: String,
        gender: String
    ): Result<Unit> {
        return try {
            val userQuerySnapshot = db.collection("users")
                .whereEqualTo("username", name) // Assuming username is unique
                .get()
                .await()

            for (document in userQuerySnapshot.documents) {
                val userData = document.data ?: hashMapOf()

                // Update specific fields
                userData["address"] = address
                userData["phone"] = phone
                userData["email"] = email
                userData["birthdate"] = birthdate
                userData["gender"] = gender

                // Perform update
                db.collection("users").document(document.id)
                    .set(userData)
                    .await()
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseUserDataSource", "Error updating user info", e)
            Result.Failure(e)
        }
    }
}
