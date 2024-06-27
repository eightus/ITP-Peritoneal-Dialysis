package com.itp.pdbuddy.data.remote

import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.utils.Result

interface PrescriptionDataSource {
    suspend fun getLatestPrescription(username: String): Result<Prescription>
    suspend fun getAllPrescriptions(username: String): Result<List<Prescription>>
    suspend fun savePrescription(username: String, prescription: Prescription): Result<Unit>
}