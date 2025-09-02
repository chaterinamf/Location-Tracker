package com.android.locationtracker.di

import android.content.Context
import androidx.room.Room
import com.android.locationtracker.data.LocationDao
import com.android.locationtracker.data.LocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocationDatabase =
        Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            "pings.db"
        ).build()

    @Provides
    fun providePingDao(db: LocationDatabase): LocationDao = db.dao()
}
