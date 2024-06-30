package com.itp.pdbuddy.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.data.remote.PrescriptionDataSource
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePrescriptionDataSource @Inject constructor() : PrescriptionDataSource {
    private val firestore = FirebaseFirestore.getInstance()
    private val prescriptionCollection = firestore.collection("prescriptions")

    override suspend fun getLatestPrescription(username: String): Result<Prescription> {
        return try {
            val querySnapshot = prescriptionCollection
                .whereEqualTo("username", username)
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            val prescription = querySnapshot.documents.firstOrNull()?.toObject(Prescription::class.java)
            if (prescription != null) {
                Result.Success(prescription)
            } else {
                Result.Failure(Exception("No latest prescription found"))
            }

        } catch (e: Exception) {
            Log.e("FirebasePrescription", "Error fetching latest prescription", e)
            Result.Failure(e)
        }
    }

    override suspend fun getAllPrescriptions(username: String): Result<List<Prescription>> {
        return try {
            val querySnapshot = prescriptionCollection
                .whereEqualTo("username", username)
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .await()

            val prescriptions = querySnapshot.toObjects(Prescription::class.java)
            Result.Success(prescriptions)
        } catch (e: Exception) {
            Log.e("FirebasePrescription", "Error fetching all prescriptions", e)
            Result.Failure(e)
        }
    }

    override suspend fun savePrescription(username: String, prescription: Prescription): Result<Unit> {
        return try {
            prescriptionCollection.add(prescription).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("FirebasePrescription", "Error saving prescription", e)
            Result.Failure(e)
        }
    }
}