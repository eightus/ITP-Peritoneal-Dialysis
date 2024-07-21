package com.itp.pdbuddy.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.itp.pdbuddy.data.remote.UserDataSource
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(): UserDataSource {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getQOTD(): Result<String> {
        return try {
            val query = db.collection("qotd")
                .limit(1)
                .get()
                .await()
            if (query.documents.isNotEmpty()) {
                val document = query.documents.first()
                val qotd = document.getString("quote")
                if (qotd != null) {
                    Result.Success(qotd)
                } else {
                    Result.Success("Stay hydrated and follow your dietary guidelines for optimal health.")
                }
            } else {
                Result.Success("Stay hydrated and follow your dietary guidelines for optimal health.")
            }
        } catch (e: Exception) {
            Result.Success("Stay hydrated and follow your dietary guidelines for optimal health.")
        }
    }
    override suspend fun getAnnouncement(): Result<String> {
        return try {
            val query = db.collection("announcement")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
            if (query.documents.isNotEmpty()) {
                val document = query.documents.first()
                val announcementText = document.getString("text")
                if (announcementText != null) {
                    Result.Success(announcementText)
                } else {
                    Result.Success("No announcement.")
                }
            } else {
                Result.Success("No announcement.")
            }
        } catch (e: Exception) {
            Result.Success("No announcement")
        }
    }

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
        gender: String,
        dryWeight: Float
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
                userData["dryWeight"] = dryWeight

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
