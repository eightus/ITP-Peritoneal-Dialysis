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
        return "71.3"
    }

    override suspend fun getTimeOn(): String{
        return "22/06/2024 07:00 PM"
    }

    override suspend fun getTimeOff(): String{
        return "23/06/2024 05:00 AM"
    }
}