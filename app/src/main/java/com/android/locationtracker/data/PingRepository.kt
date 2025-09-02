package com.android.locationtracker.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PingRepository @Inject constructor(private val pingDao: LocationDao) {
    fun getPings(): Flow<List<Ping>> = pingDao.getAllLocation()

    suspend fun insert(ping: Ping) = pingDao.insert(ping)
}