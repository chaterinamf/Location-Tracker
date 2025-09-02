package com.android.locationtracker.di

import com.android.locationtracker.data.LocationDao
import com.android.locationtracker.data.PingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePingRepository(dao: LocationDao): PingRepository = PingRepository(dao)
}