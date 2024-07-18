package com.itp.pdbuddy.data.repository

import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.data.remote.APIDataSource
import com.itp.pdbuddy.data.remote.PrescriptionDataSource
import com.itp.pdbuddy.data.remote.RecordDataSource
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

class RecordRepository @Inject constructor(
    private val recordDataSource: RecordDataSource,
    private val apiDataSource: APIDataSource,
    private val prescriptionDataSource: PrescriptionDataSource
) {

    suspend fun getRecords(name: String): Result<List<Map<String, Any>>> {
        return recordDataSource.getRecords(name)
    }

    suspend fun submitRecord(name: String, data: List<Any>): Result<Boolean> {
        return recordDataSource.submitRecord(name, data)
    }

    suspend fun getAutoRecord(): List<Any>{
        return listOf(
            apiDataSource.getBP(),
            apiDataSource.getHR(),
            apiDataSource.getWeight(),
            apiDataSource.getTimeOn(),
            apiDataSource.getTimeOff())
    }

    suspend fun getLatestPrescription(username: String): Result<Prescription> {
        return prescriptionDataSource.getLatestPrescription(username)
    }
}