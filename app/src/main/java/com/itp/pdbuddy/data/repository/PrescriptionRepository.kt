package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.data.remote.PrescriptionDataSource
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

class PrescriptionRepository @Inject constructor(
    private val prescriptionDataSource: PrescriptionDataSource
) {
    suspend fun getLatestPrescription(username: String): Result<Prescription> {
        return prescriptionDataSource.getLatestPrescription(username)
    }

    suspend fun getAllPrescriptions(username: String): Result<List<Prescription>> {
        return prescriptionDataSource.getAllPrescriptions(username)
    }

    suspend fun savePrescription(username: String, prescription: Prescription): Result<Unit> {
        return prescriptionDataSource.savePrescription(username, prescription)
    }
}