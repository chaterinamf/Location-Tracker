package com.android.locationtracker.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.locationtracker.data.Ping
import com.android.locationtracker.data.PingRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PingRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("Testing1", "LocationWorker.doWork started")
        val fusedClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        // Check permissions explicitly
        val hasFine = ActivityCompat.checkSelfPermission(
            applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ActivityCompat.checkSelfPermission(
            applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasBackground = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else true

//        Log.d("Testing1", "doWork LocationWorker is called\nhasFine: $hasFine\nhasCoarse: $hasCoarse")
        if (!hasFine && !hasCoarse) {
            // No permission → fail gracefully (or retry later if you expect user to grant it)
            return Result.failure()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasBackground) {
//            Log.d("Testing1", "Missing background location permission -> failing")
            return Result.failure()
        }

        return try {
            val location = fusedClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                null
            ).await()

//            Log.d("Testing1", "doWork location: $location")
            location?.let {
                val ping = Ping(
                    lat = it.latitude,
                    lng = it.longitude,
                    accuracy = it.accuracy,
                    timestamp = System.currentTimeMillis()
                )
//                Log.d("Testing1", "doWork ping: $ping")
                repository.insert(ping)
            }

            Result.success()
        } catch (se: SecurityException) {
            // User revoked permission while worker is running
//            Log.d("Testing1", "doWork SecurityException: ${se.stackTrace}")
            Result.failure()
        } catch (e: Exception) {
//            Log.d("Testing1", "doWork Exception: ${e.stackTrace}")
            Result.retry()
        }
    }
}