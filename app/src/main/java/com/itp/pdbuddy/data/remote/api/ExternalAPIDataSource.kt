package com.itp.pdbuddy.data.remote.api

import com.itp.pdbuddy.data.remote.APIDataSource
import com.itp.pdbuddy.data.remote.RecordDataSource
import javax.inject.Inject

class ExternalAPIDataSource @Inject constructor(): APIDataSource {

    override suspend fun getBP(): String{
        return "120/80"
    }

    override suspend fun getHR(): String{
        return "75"
    }

    override suspend fun getWeight(): String{
        return "75"
    }

    override suspend fun getTimeOn(): String{
        return "22/06/2024 3:0"
    }

    override suspend fun getTimeOff(): String{
        return "22/06/2024 10:0"
    }
}