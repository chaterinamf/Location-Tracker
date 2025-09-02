package com.android.locationtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ping: Ping)

    @Query("SELECT * FROM Ping ORDER BY timestamp DESC")
    fun getAllLocation(): Flow<List<Ping>>
}
