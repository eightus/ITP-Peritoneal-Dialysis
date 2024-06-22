package com.itp.pdbuddy.data.remote

interface APIDataSource {

    suspend fun getBP(): String

    suspend fun getHR(): String

    suspend fun getWeight(): String

    suspend fun getTimeOn(): String

    suspend fun getTimeOff(): String
}