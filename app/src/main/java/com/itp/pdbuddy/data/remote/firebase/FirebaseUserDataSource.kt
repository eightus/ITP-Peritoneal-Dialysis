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
                .get()
                .await()
            val records = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data ?: emptyMap()
            }

            Result.Success(records)
        } catch (e: Exception) {
            Log.e("FirebaseRecord", "Error fetching all record", e)
            Result.Failure(e)
        }
    }
}