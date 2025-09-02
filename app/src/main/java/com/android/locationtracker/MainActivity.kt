package com.android.locationtracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.android.locationtracker.navigation.NavGraph
import com.android.locationtracker.presentation.PingsViewModel
import com.android.locationtracker.worker.LocationScheduler
import com.android.locationtracker.worker.LocationWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // schedule periodic (no constraints) and run immediate
        LocationScheduler.schedule(this) // ensure your schedule builds a periodic request WITHOUT network constraint

// force immediate one-shot to verify worker created
//        runImmediateLocationWorker(this)

// dump status a few seconds later
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(5000)
//            dumpLocationWorkInfo(this@MainActivity)
//        }

        setContent {
            val vm = hiltViewModel<PingsViewModel>()
            NavGraph(vm)
        }
    }

    fun runImmediateLocationWorker(context: Context) {
        val req = OneTimeWorkRequestBuilder<LocationWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // optional: allows faster execution if supported
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("LocationWorkNow", ExistingWorkPolicy.REPLACE, req)

        // optional: observe or dump status
//        dumpLocationWorkInfo(context)
    }

    // call from a background thread (or coroutine)
    fun dumpLocationWorkInfo(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val infos = WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWork("LocationWork")
                    .get() // blocking call — ok on IO dispatcher

                if (infos.isEmpty()) {
                    Log.d("Testing1", "No WorkInfos for 'LocationWork' (maybe never enqueued)")
                } else {
                    infos.forEach { info ->
                        Log.d("Testing1", "WorkInfo id=${info.id} state=${info.state} attempts=${info.runAttemptCount} output=${info.outputData}")
                        info.outputData.getString("error")?.let { Log.e("Testing1", "WorkInfo failure: $it") } // if available
                    }
                }
            } catch (e: Exception) {
                Log.d("Testing1", "Error getting WorkInfo", e)
            }
        }
    }
}
