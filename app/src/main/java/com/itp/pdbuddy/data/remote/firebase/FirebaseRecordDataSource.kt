package com.itp.pdbuddy.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.data.remote.RecordDataSource
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

class FirebaseRecordDataSource @Inject constructor(): RecordDataSource{

    private val db = FirebaseFirestore.getInstance()

    override suspend fun submitRecord(name: String, data: List<Any>): Result<Boolean> {
        Log.d("Record", data.toString())
        return try {
            val record = hashMapOf(
                "Name" to name,
                "Record Date" to data[0],
                "Blood Pressure" to data[1],
                "Heart Rate" to data[2],
                "Weight" to data[3],
                "Urine Out" to data[4],
                "Time On" to data[5],
                "Time Off" to data[6],
                "Heat Bag 1.5 Dext. (Amount)" to data[7],
                "Heat Bag 2.5 Dext. (Amount)" to data[8],
                "Heat Bag 4.25 Dext. (Amount)" to data[9],
                "Supply Bag 1.5 Dext. (Amount)" to data[10],
                "Supply Bag 2.5 Dext. (Amount)" to data[11],
                "Supply Bag 4.25 Dext. (Amount)" to data[12],
                "Last Bag 1.5 Dext. (Amount)" to data[13],
                "Last Bag 2.5 Dext. (Amount)" to data[14],
                "Last Bag 4.25 Dext. (Amount)" to data[15],
                "Others" to data[16],
                "Type of Therapy" to data[17],
                "Total Volume" to data[18],
                "Target UF" to data[19],
                "Therapy Time" to data[20],
                "Fill Volume" to data[21],
                "Last Fill volume" to data[22],
                "Dextrose % Conc." to data[23],
                "No. Of Cycles" to data[24],
                "Initial Drain" to data[25],
                "Avg Dwell Time" to data[26],
                "Color of Drainage" to data[27],
                "Total UF" to data[28],
                "Nett UF" to data[29],
                "Remarks" to data[30]
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