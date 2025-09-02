package com.android.locationtracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.locationtracker.data.Ping
import com.android.locationtracker.data.PingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PingsViewModel @Inject constructor(
    private val repository: PingRepository
) : ViewModel() {

    val pings: StateFlow<List<Ping>> = repository.getPings()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addPing(ping: Ping) {
        viewModelScope.launch {
            repository.insert(ping)
        }
    }
}