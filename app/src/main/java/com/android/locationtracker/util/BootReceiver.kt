package com.android.locationtracker.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.locationtracker.worker.LocationScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            LocationScheduler.schedule(context)
        }
    }
}
