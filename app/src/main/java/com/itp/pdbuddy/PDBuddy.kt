package com.itp.pdbuddy

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import androidx.work.Configuration

@HiltAndroidApp
class PDBuddy : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}