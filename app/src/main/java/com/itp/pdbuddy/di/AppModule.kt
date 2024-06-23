package com.itp.pdbuddy.di

import com.itp.pdbuddy.data.remote.AuthDataSource
import com.itp.pdbuddy.data.remote.RecordDataSource
import com.itp.pdbuddy.data.remote.firebase.FirebaseAuthDataSource
import com.itp.pdbuddy.data.remote.firebase.FirebaseRecordDataSource
import com.itp.pdbuddy.data.remote.api.ExternalAPIDataSource
import com.itp.pdbuddy.data.remote.APIDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        firebaseAuthDataSource: FirebaseAuthDataSource
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindRecordDataSource(
        firebaseRecordDataSource: FirebaseRecordDataSource
    ): RecordDataSource

    @Binds
    @Singleton
    abstract fun APIDataSource(
        externalAPIDataSource: ExternalAPIDataSource
    ): APIDataSource
}