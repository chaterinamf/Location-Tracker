package com.android.locationtracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ping::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun dao(): LocationDao
}
