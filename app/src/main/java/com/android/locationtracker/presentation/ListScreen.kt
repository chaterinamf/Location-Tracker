package com.android.locationtracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.locationtracker.R
import com.android.locationtracker.util.TimeFormatter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun ListScreen(vm: PingsViewModel) {
    val pings by vm.pings.collectAsState()
    LazyColumn {
        items(pings) { ping ->
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = stringResource(
                            R.string.location_date_time,
                            TimeFormatter.format(ping.timestamp)
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(text = stringResource(R.string.location_latitude, ping.lat.toString()))
                    Text(text = stringResource(R.string.location_longitude, ping.lng.toString()))
                    Text(text = stringResource(R.string.location_accuracy, ping.accuracy))
                }
                val latLng = LatLng(ping.lat, ping.lng)
                val camera = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(latLng, 14f)
                }
                val uiSettings = remember { MapUiSettings(
                    zoomControlsEnabled = false,
                    zoomGesturesEnabled = false
                ) }
                GoogleMap(
                    modifier = Modifier.size(100.dp),
                    cameraPositionState = camera,
                    uiSettings = uiSettings
                ) {
                    Marker(state = rememberMarkerState(position = latLng))
                }
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
