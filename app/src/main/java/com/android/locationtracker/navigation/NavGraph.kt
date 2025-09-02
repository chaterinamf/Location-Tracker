package com.android.locationtracker.navigation

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.locationtracker.presentation.ListScreen
import com.android.locationtracker.presentation.MapScreen
import com.android.locationtracker.presentation.PingsViewModel
import com.android.locationtracker.worker.LocationScheduler

@Composable
fun NavGraph(vm: PingsViewModel) {
    PermissionLauncher()

    val nav = rememberNavController()
    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedIndex.intValue == Destination.MAP.ordinal,
                    onClick = {
                        nav.navigate(Destination.MAP.route)
                        selectedIndex.intValue = Destination.MAP.ordinal
                    },
                    label = { Text(Destination.MAP.label) },
                    icon = {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_dialog_map),
                            contentDescription = Destination.MAP.description
                        )
                    }
                )
                NavigationBarItem(
                    selected = selectedIndex.intValue == Destination.LIST.ordinal,
                    onClick = {
                        nav.navigate(Destination.LIST.route)
                        selectedIndex.intValue = Destination.LIST.ordinal
                    },
                    label = { Text(Destination.LIST.label) },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = Destination.LIST.description
                        )
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Destination.MAP.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destination.MAP.route) { MapScreen(vm) }
            composable(Destination.LIST.route) { ListScreen(vm) }
        }
    }
}

@Composable
private fun PermissionLauncher() {
    val context = LocalContext.current

    // Fine/Coarse permissions launcher
    val fineLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Background permission launcher
    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Toast.makeText(context, "Background location granted", Toast.LENGTH_SHORT).show()
            LocationScheduler.schedule(context)
        } else {
            Toast.makeText(context, "Background location denied", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        fineLocationLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }
}
