package com.android.locationtracker.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(vm: PingsViewModel) {
    val pings by vm.pings.collectAsState()

    val camera = rememberCameraPositionState()
    pings.firstOrNull()?.let {
        camera.position = CameraPosition.fromLatLngZoom(LatLng(it.lat, it.lng), 10f)
    }
    GoogleMap(cameraPositionState = camera) {
        pings.forEach {
            Marker(
                state = MarkerState(position = LatLng(it.lat, it.lng)),
                title = "Ping ${it.id}",
                snippet = "±${it.accuracy}m"
            )
        }
    }
}
