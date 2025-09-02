package com.android.locationtracker

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class LocationTrackerApplication : Application() {

    @Inject lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(hiltWorkerFactory)
                .setMinimumLoggingLevel(Log.VERBOSE)
                .build()
        )
    }
}
