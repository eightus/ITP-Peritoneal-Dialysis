package com.itp.pdbuddy.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.itp.pdbuddy.data.remote.RecordDataSource
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRecordDataSource @Inject constructor(): RecordDataSource {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun getRecords(name: String): Result<List<Map<String, Any>>> {
        return try {
            val querySnapshot = db.collection("Record")
                .whereEqualTo("Name", name)
                .orderBy("RecordDate", Query.Direction.DESCENDING)
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

    override suspend fun submitRecord(name: String, data: List<Any>): Result<Boolean> {
        Log.d("Record", data.toString())
        return try {
            val record = hashMapOf(
                "Name" to name,
                "RecordDate" to data[0],
                "Blood Pressure" to data[1],
                "Heart Rate" to data[2],
                "Weight" to data[3],
                "Urine Out" to data[4],
                "Time On" to data[5],
                "Time Off" to data[6],
                "Heater Bag Type" to data[7],
                "Heater Bag Amount" to data[8],
                "White Bag Type" to data[9],
                "White Bag Amount" to data[10],
                "Blue Bag Type" to data[11],
                "Blue Bag Amount" to data[12],
                "Blue Bag Type (Others)" to data[13],
                "Type of Therapy" to data[14],
                "Total Volume" to data[15],
                "Target UF" to data[16],
                "Therapy Time" to data[17],
                "Fill Volume" to data[18],
                "Last Fill volume" to data[19],
                "Dextrose % Conc." to data[20],
                "No. Of Cycles" to data[21],
                "Initial Drain" to data[22],
                "Avg Dwell Time" to data[23],
                "Color of Drainage" to data[24],
                "Total UF" to data[25],
                "Nett UF" to data[26],
                "Remarks" to data[27]
            )
            db.collection("Record").document()
                .set(record)
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

            Result.Success(true)
        } catch (e: Exception) {
            Log.d("Record", "Error")
            e.message?.let { Log.d("Login", it) }
            Result.Failure(e)
        }
    }
}
