package com.android.locationtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ping(
    val lat: Double,
    val lng: Double,
    val accuracy: Float,
    val timestamp: Long,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)