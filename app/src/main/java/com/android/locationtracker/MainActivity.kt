package com.android.locationtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.locationtracker.navigation.NavGraph
import com.android.locationtracker.presentation.PingsViewModel
import com.android.locationtracker.worker.LocationScheduler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationScheduler.schedule(this)

        setContent {
            val vm = hiltViewModel<PingsViewModel>()
            NavGraph(vm)
        }
    }
}
