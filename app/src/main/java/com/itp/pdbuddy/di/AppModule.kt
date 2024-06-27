package com.itp.pdbuddy.di

import android.content.Context
import androidx.room.Room
import com.itp.pdbuddy.data.remote.AuthDataSource
import com.itp.pdbuddy.data.remote.RecordDataSource
import com.itp.pdbuddy.data.remote.firebase.FirebaseAuthDataSource
import com.itp.pdbuddy.data.remote.firebase.FirebaseRecordDataSource
import com.itp.pdbuddy.data.remote.api.ExternalAPIDataSource
import com.itp.pdbuddy.data.remote.APIDataSource
import com.itp.pdbuddy.data.remote.NotificationDAO
import com.itp.pdbuddy.data.remote.PrescriptionDataSource
import com.itp.pdbuddy.data.remote.database.NotificationRoomDatabase
import com.itp.pdbuddy.data.remote.firebase.FirebasePrescriptionDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    abstract fun bindPrescriptionDataSource(
        firebasePrescriptionDataSource: FirebasePrescriptionDataSource
    ): PrescriptionDataSource

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

    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {
        @Singleton
        @Provides
        fun provideDataBase(@ApplicationContext context: Context): NotificationRoomDatabase {
            return Room.databaseBuilder(
                context,
                NotificationRoomDatabase::class.java,
                "notification_database.db"
            ).build()
        }

        @Provides
        fun provideWordDao(database: NotificationRoomDatabase): NotificationDAO = database.notificationDAO()

    }
}
